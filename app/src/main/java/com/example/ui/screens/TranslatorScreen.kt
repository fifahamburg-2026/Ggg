package com.example.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.EngineInfoView
import com.example.ui.components.HistoryView
import com.example.ui.components.OfflineHeader
import com.example.ui.components.PhrasebookView
import com.example.ui.components.TranslationCard
import com.example.ui.viewmodel.TranslatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(
    viewModel: TranslatorViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val historyList by viewModel.history.collectAsStateWithLifecycle()
    val favoritesList by viewModel.favorites.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    // Toast event listener
    LaunchedEffect(uiState.toastMessage) {
        uiState.toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "المترجم الفوري (بدون إنترنت)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("bottom_nav_bar")
            ) {
                // Tab 0: Translate
                NavigationBarItem(
                    selected = uiState.selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 0) Icons.Filled.Translate else Icons.Outlined.Translate,
                            contentDescription = "الترجمة"
                        )
                    },
                    label = { Text("المترجم", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_translate")
                )

                // Tab 1: Phrasebook
                NavigationBarItem(
                    selected = uiState.selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 1) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                            contentDescription = "دليل العبارات"
                        )
                    },
                    label = { Text("العبارات", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_phrasebook")
                )

                // Tab 2: History & Saved
                NavigationBarItem(
                    selected = uiState.selectedTab == 2,
                    onClick = { viewModel.setSelectedTab(2) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 2) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = "المحفوظات"
                        )
                    },
                    label = { Text("المحفوظات", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_history")
                )

                // Tab 3: Engine & Privacy
                NavigationBarItem(
                    selected = uiState.selectedTab == 3,
                    onClick = { viewModel.setSelectedTab(3) },
                    icon = {
                        Icon(
                            imageVector = if (uiState.selectedTab == 3) Icons.Filled.Info else Icons.Outlined.Info,
                            contentDescription = "الخصوصية والمحرك"
                        )
                    },
                    label = { Text("الخصوصية", fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    modifier = Modifier.testTag("nav_tab_privacy")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (uiState.selectedTab) {
                0 -> {
                    // MAIN TRANSLATE TAB
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        OfflineHeader(
                            sourceLang = uiState.sourceLang,
                            targetLang = uiState.targetLang,
                            onSwapLanguages = { viewModel.swapLanguages() },
                            onSelectLanguagePair = { src, tgt -> viewModel.setLanguagePair(src, tgt) }
                        )

                        TranslationCard(
                            inputText = uiState.inputText,
                            result = uiState.result,
                            sourceLang = uiState.sourceLang,
                            targetLang = uiState.targetLang,
                            isCurrentSaved = uiState.isCurrentSaved,
                            onInputChanged = { viewModel.onInputTextChanged(it) },
                            onClearInput = { viewModel.clearInput() },
                            onSpeakSource = { viewModel.speakSource() },
                            onSpeakTranslation = { viewModel.speakTranslation() },
                            onToggleFavorite = { viewModel.toggleCurrentFavorite() },
                            onShowToast = { viewModel.showToast(it) },
                            isAiLoading = uiState.isAiLoading,
                            onTranslateWithAi = { viewModel.translateWithAi() }
                        )
                    }
                }

                1 -> {
                    // PHRASEBOOK TAB
                    PhrasebookView(
                        phrases = viewModel.getFilteredPhrases(),
                        selectedCategory = uiState.selectedPhraseCategory,
                        searchQuery = uiState.phraseFilterQuery,
                        onCategorySelected = { viewModel.setSelectedPhraseCategory(it) },
                        onSearchQueryChanged = { viewModel.setPhraseFilterQuery(it) },
                        onPhraseSelected = { viewModel.selectPhrase(it) },
                        onSpeakText = { text, lang -> viewModel.ttsManager.speak(text, lang) },
                        onShowToast = { viewModel.showToast(it) }
                    )
                }

                2 -> {
                    // HISTORY & SAVED TAB
                    HistoryView(
                        historyList = historyList,
                        favoritesList = favoritesList,
                        searchQuery = uiState.historyFilterQuery,
                        showOnlyFavorites = uiState.showOnlyFavorites,
                        onSearchQueryChanged = { viewModel.setHistoryFilterQuery(it) },
                        onToggleShowOnlyFavorites = { viewModel.setShowOnlyFavorites(it) },
                        onItemSelected = { viewModel.selectHistoryItem(it) },
                        onToggleFavorite = { viewModel.toggleFavorite(it) },
                        onDeleteItem = { viewModel.deleteHistoryRecord(it) },
                        onClearAllHistory = { viewModel.clearHistory() },
                        onSpeakText = { text, lang -> viewModel.ttsManager.speak(text, lang) },
                        onShowToast = { viewModel.showToast(it) }
                    )
                }

                3 -> {
                    // ENGINE INFO & PRIVACY GUARANTEE TAB
                    EngineInfoView()
                }
            }
        }
    }
}
