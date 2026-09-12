package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.PhraseCategory
import com.example.data.model.PhraseItem
import com.example.data.model.TranslationRecord
import com.example.data.model.TranslationResult
import com.example.data.repository.TranslationRepository
import com.example.engine.OfflinePhrasebookData
import com.example.engine.OfflineTranslationEngine
import com.example.service.GeminiTranslationService
import com.example.service.TtsManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TranslatorUiState(
    val inputText: String = "",
    val sourceLang: String = "ar", // "ar" or "en"
    val targetLang: String = "en", // "en" or "ar"
    val result: TranslationResult? = null,
    val isAutoDetect: Boolean = false,
    val selectedTab: Int = 0, // 0 = Translate, 1 = Phrasebook, 2 = History/Favorites, 3 = Info
    val selectedPhraseCategory: PhraseCategory = PhraseCategory.GREETINGS,
    val phraseFilterQuery: String = "",
    val historyFilterQuery: String = "",
    val showOnlyFavorites: Boolean = false,
    val isCurrentSaved: Boolean = false,
    val toastMessage: String? = null,
    val isAiLoading: Boolean = false
)

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {

    private val engine = OfflineTranslationEngine()
    private val geminiService = GeminiTranslationService()
    private val database = AppDatabase.getInstance(application)
    private val repository = TranslationRepository(database.translationDao())
    val ttsManager = TtsManager(application)

    private val _uiState = MutableStateFlow(TranslatorUiState())
    val uiState: StateFlow<TranslatorUiState> = _uiState.asStateFlow()

    private var translateJob: Job? = null
    private var saveHistoryJob: Job? = null

    val history: StateFlow<List<TranslationRecord>> = repository.allHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favorites: StateFlow<List<TranslationRecord>> = repository.favorites
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Initial sample greeting
        translateDirect("مرحباً بك في المترجم الفوري دون إنترنت")
    }

    fun onInputTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text, isCurrentSaved = false)

        translateJob?.cancel()
        if (text.trim().isEmpty()) {
            _uiState.value = _uiState.value.copy(result = null)
            return
        }

        translateJob = viewModelScope.launch {
            // Instant real-time local translation (small debounce 120ms for typing smoothness)
            delay(120)
            executeTranslation(text)
        }
    }

    fun swapLanguages() {
        val current = _uiState.value
        val newSource = current.targetLang
        val newTarget = current.sourceLang
        val previousResultText = current.result?.translatedText ?: ""

        _uiState.value = current.copy(
            sourceLang = newSource,
            targetLang = newTarget,
            inputText = previousResultText,
            isCurrentSaved = false
        )

        if (previousResultText.isNotEmpty()) {
            executeTranslation(previousResultText)
        }
    }

    fun setLanguagePair(source: String, target: String) {
        _uiState.value = _uiState.value.copy(
            sourceLang = source,
            targetLang = target
        )
        if (_uiState.value.inputText.isNotEmpty()) {
            executeTranslation(_uiState.value.inputText)
        }
    }

    fun setSelectedTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun setSelectedPhraseCategory(category: PhraseCategory) {
        _uiState.value = _uiState.value.copy(selectedPhraseCategory = category)
    }

    fun setPhraseFilterQuery(query: String) {
        _uiState.value = _uiState.value.copy(phraseFilterQuery = query)
    }

    fun setHistoryFilterQuery(query: String) {
        _uiState.value = _uiState.value.copy(historyFilterQuery = query)
    }

    fun setShowOnlyFavorites(onlyFavorites: Boolean) {
        _uiState.value = _uiState.value.copy(showOnlyFavorites = onlyFavorites)
    }

    fun clearInput() {
        _uiState.value = _uiState.value.copy(
            inputText = "",
            result = null,
            isCurrentSaved = false
        )
    }

    fun selectPhrase(phrase: PhraseItem) {
        val isCurrentArSource = _uiState.value.sourceLang == "ar"
        val textToUse = if (isCurrentArSource) phrase.arabic else phrase.english
        _uiState.value = _uiState.value.copy(
            inputText = textToUse,
            selectedTab = 0
        )
        executeTranslation(textToUse)
    }

    fun selectHistoryItem(item: TranslationRecord) {
        _uiState.value = _uiState.value.copy(
            inputText = item.sourceText,
            sourceLang = item.sourceLang,
            targetLang = item.targetLang,
            selectedTab = 0,
            isCurrentSaved = item.isFavorite
        )
        executeTranslation(item.sourceText)
    }

    fun speakSource() {
        val text = _uiState.value.inputText
        if (text.isNotEmpty()) {
            ttsManager.speak(text, _uiState.value.sourceLang)
        }
    }

    fun speakTranslation() {
        val text = _uiState.value.result?.translatedText ?: ""
        if (text.isNotEmpty()) {
            ttsManager.speak(text, _uiState.value.targetLang)
        }
    }

    fun toggleCurrentFavorite() {
        val current = _uiState.value
        val res = current.result ?: return
        val newFavoriteState = !current.isCurrentSaved
        _uiState.value = current.copy(isCurrentSaved = newFavoriteState)

        viewModelScope.launch {
            repository.saveTranslation(
                sourceText = current.inputText,
                translatedText = res.translatedText,
                sourceLang = current.sourceLang,
                targetLang = current.targetLang,
                phonetic = res.phonetic
            )
            val existing = database.translationDao().findExisting(current.inputText.trim(), current.sourceLang)
            if (existing != null) {
                database.translationDao().setFavorite(existing.id, newFavoriteState)
            }
        }
    }

    fun toggleFavorite(record: TranslationRecord) {
        viewModelScope.launch {
            repository.toggleFavorite(record)
        }
    }

    fun deleteHistoryRecord(id: Long) {
        viewModelScope.launch {
            repository.deleteRecord(id)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistoryOnly()
            showToast("تم مسح السجل")
        }
    }

    fun showToast(message: String) {
        _uiState.value = _uiState.value.copy(toastMessage = message)
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }

    private fun translateDirect(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
        executeTranslation(text)
    }

    private fun executeTranslation(text: String) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) return

        val sourceLang = _uiState.value.sourceLang
        val targetLang = _uiState.value.targetLang

        val result = engine.translate(
            rawText = trimmed,
            requestedSourceLang = sourceLang,
            requestedTargetLang = targetLang
        )

        _uiState.value = _uiState.value.copy(result = result)

        // Persist to history in background after brief pause
        saveHistoryJob?.cancel()
        saveHistoryJob = viewModelScope.launch {
            delay(1500)
            if (trimmed.isNotEmpty() && result.translatedText.isNotEmpty()) {
                repository.saveTranslation(
                    sourceText = trimmed,
                    translatedText = result.translatedText,
                    sourceLang = sourceLang,
                    targetLang = targetLang,
                    phonetic = result.phonetic
                )
            }
        }
    }

    fun translateWithAi() {
        val current = _uiState.value
        val text = current.inputText.trim()
        if (text.isEmpty()) return

        _uiState.value = current.copy(isAiLoading = true)

        viewModelScope.launch {
            val res = geminiService.translateWithAi(
                text = text,
                sourceLang = current.sourceLang,
                targetLang = current.targetLang
            )

            res.fold(
                onSuccess = { aiTranslated ->
                    val updatedResult = current.result?.copy(
                        translatedText = aiTranslated,
                        isExactMatch = true
                    ) ?: TranslationResult(
                        translatedText = aiTranslated,
                        phonetic = "",
                        detectedSourceLang = current.sourceLang,
                        detectedTargetLang = current.targetLang,
                        isExactMatch = true
                    )
                    _uiState.value = _uiState.value.copy(
                        result = updatedResult,
                        isAiLoading = false
                    )
                    showToast("تمت الترجمة الذكية عبر Gemini AI")

                    repository.saveTranslation(
                        sourceText = text,
                        translatedText = aiTranslated,
                        sourceLang = current.sourceLang,
                        targetLang = current.targetLang,
                        phonetic = ""
                    )
                },
                onFailure = { err ->
                    _uiState.value = _uiState.value.copy(isAiLoading = false)
                    showToast(err.message ?: "تعذر إتمام الترجمة الذكية")
                }
            )
        }
    }

    fun getFilteredPhrases(): List<PhraseItem> {
        val state = _uiState.value
        val categoryPhrases = OfflinePhrasebookData.phrases.filter { it.category == state.selectedPhraseCategory }
        val query = state.phraseFilterQuery.trim()
        if (query.isEmpty()) return categoryPhrases

        val normQuery = engine.normalizeArabic(query)
        val normEnQuery = engine.normalizeEnglish(query)

        return OfflinePhrasebookData.phrases.filter {
            engine.normalizeArabic(it.arabic).contains(normQuery) ||
            engine.normalizeEnglish(it.english).contains(normEnQuery) ||
            it.phonetic.lowercase().contains(normEnQuery) ||
            it.context.contains(query)
        }
    }

    override fun onCleared() {
        super.onCleared()
        ttsManager.shutdown()
    }
}
