package com.example

import com.example.engine.OfflineTranslationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class OfflineTranslationEngineTest {

    private lateinit var engine: OfflineTranslationEngine

    @Before
    fun setUp() {
        engine = OfflineTranslationEngine()
    }

    @Test
    fun testLanguageDetection() {
        assertEquals("ar", engine.detectLanguage("مرحباً بك"))
        assertEquals("en", engine.detectLanguage("Hello world"))
        assertEquals("ar", engine.detectLanguage("السلام عليكم"))
        assertEquals("en", engine.detectLanguage("How are you?"))
    }

    @Test
    fun testArabicToEnglishPhraseTranslation() {
        val result1 = engine.translate("صباح الخير")
        assertEquals("Good morning", result1.translatedText)
        assertEquals("ar", result1.detectedSourceLang)
        assertEquals("en", result1.detectedTargetLang)

        val result2 = engine.translate("كيف حالك")
        assertEquals("How are you", result2.translatedText)

        val result3 = engine.translate("شكرا جزيلا")
        assertEquals("Thank you very much", result3.translatedText)
    }

    @Test
    fun testEnglishToArabicPhraseTranslation() {
        val result1 = engine.translate("good morning")
        assertEquals("صباح الخير", result1.translatedText)
        assertEquals("en", result1.detectedSourceLang)
        assertEquals("ar", result1.detectedTargetLang)

        val result2 = engine.translate("thank you very much")
        assertEquals("شكرا جزيلا", result2.translatedText)
    }

    @Test
    fun testMorphologyPrefixStripping() {
        // "والكتاب" -> should identify "و" (and) + "ال" (the) + "كتاب" (book)
        val res = engine.translate("كتاب")
        assertTrue(res.translatedText.lowercase().contains("book"))
    }

    @Test
    fun testEnglishAdjectiveNounInversion() {
        // "big house" in English -> should translate as "بيت كبير" (noun + adj)
        val res = engine.translate("big house")
        assertEquals("بيت كبير", res.translatedText)
    }

    @Test
    fun testPhoneticGeneration() {
        val phonetic = engine.generateArabicPhonetic("مرحبا")
        assertNotNull(phonetic)
        assertTrue(phonetic.isNotEmpty())
    }
}
