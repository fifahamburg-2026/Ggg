package com.example.data.repository

import com.example.data.local.TranslationDao
import com.example.data.model.TranslationRecord
import kotlinx.coroutines.flow.Flow

class TranslationRepository(private val dao: TranslationDao) {
    val allHistory: Flow<List<TranslationRecord>> = dao.getAllHistory()
    val favorites: Flow<List<TranslationRecord>> = dao.getFavorites()

    fun search(query: String): Flow<List<TranslationRecord>> = dao.searchHistory(query)

    suspend fun saveTranslation(
        sourceText: String,
        translatedText: String,
        sourceLang: String,
        targetLang: String,
        phonetic: String = "",
        category: String = "general"
    ): Long {
        val trimmedSource = sourceText.trim()
        val trimmedTarget = translatedText.trim()
        if (trimmedSource.isEmpty() || trimmedTarget.isEmpty()) return -1L

        val existing = dao.findExisting(trimmedSource, sourceLang)
        return if (existing != null) {
            val updated = existing.copy(
                translatedText = trimmedTarget,
                timestamp = System.currentTimeMillis(),
                phonetic = if (phonetic.isNotEmpty()) phonetic else existing.phonetic
            )
            dao.update(updated)
            existing.id
        } else {
            val newRecord = TranslationRecord(
                sourceText = trimmedSource,
                translatedText = trimmedTarget,
                sourceLang = sourceLang,
                targetLang = targetLang,
                phonetic = phonetic,
                category = category
            )
            dao.insert(newRecord)
        }
    }

    suspend fun toggleFavorite(record: TranslationRecord) {
        dao.setFavorite(record.id, !record.isFavorite)
    }

    suspend fun deleteRecord(id: Long) {
        dao.deleteById(id)
    }

    suspend fun clearHistoryOnly() {
        dao.clearHistoryNonFavorites()
    }

    suspend fun clearAll() {
        dao.clearAll()
    }
}
