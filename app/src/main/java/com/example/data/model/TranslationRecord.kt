package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String, // "ar" or "en"
    val targetLang: String, // "en" or "ar"
    val phonetic: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val category: String = "general"
)
