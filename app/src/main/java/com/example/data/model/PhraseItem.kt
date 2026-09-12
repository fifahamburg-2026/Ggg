package com.example.data.model

data class PhraseItem(
    val id: String,
    val category: PhraseCategory,
    val arabic: String,
    val english: String,
    val phonetic: String = "",
    val context: String = ""
)

enum class PhraseCategory(val titleAr: String, val titleEn: String, val iconName: String) {
    GREETINGS("التحيات والتعارف", "Greetings", "waving_hand"),
    TRAVEL("السفر والمواصلات", "Travel & Transit", "flight"),
    DINING("المطاعم والطعام", "Dining & Food", "restaurant"),
    SHOPPING("التسوق والمال", "Shopping & Money", "shopping_cart"),
    EMERGENCY("الطوارئ والصحة", "Emergency & Health", "local_hospital"),
    HOTEL("الفندق والإقامة", "Hotel & Stay", "hotel"),
    BUSINESS("العمل والاجتماعات", "Work & Business", "work"),
    NUMBERS_TIME("الأرقام والوقت", "Numbers & Time", "schedule")
}

data class TranslationResult(
    val translatedText: String,
    val phonetic: String = "",
    val detectedSourceLang: String,
    val detectedTargetLang: String,
    val partOfSpeech: String = "",
    val alternativeMeanings: List<String> = emptyList(),
    val exampleSentenceSource: String = "",
    val exampleSentenceTranslation: String = "",
    val isExactMatch: Boolean = false,
    val processingTimeMs: Long = 0
)
