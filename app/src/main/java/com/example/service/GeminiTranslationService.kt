package com.example.service

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiTranslationService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    fun isConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    suspend fun translateWithAi(
        text: String,
        sourceLang: String,
        targetLang: String
    ): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext Result.failure(
                IllegalStateException("لم يتم ضبط مفتاح Gemini API. يمكنك إضافته في لوحة Secrets للاستمتاع بالترجمة الذكية عبر السحابة.")
            )
        }

        val sourceLangName = if (sourceLang == "ar") "Arabic" else "English"
        val targetLangName = if (targetLang == "ar") "Arabic" else "English"

        val prompt = """
            You are a professional, expert bilingual Arabic-English translator.
            Translate the following text accurately from $sourceLangName to $targetLangName.
            Preserve all nuances, conversational tone, and exact meaning.
            Provide ONLY the translated text without any explanation, intro, notes, or quotes.

            Text to translate:
            $text
        """.trimIndent()

        try {
            val jsonPayload = JSONObject().apply {
                val contentsArray = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArray = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", prompt)
                            }
                            put(partObj)
                        }
                        put("parts", partsArray)
                    }
                    put(contentObj)
                }
                put("contents", contentsArray)

                // Optional system instruction
                val genConfig = JSONObject().apply {
                    put("temperature", 0.1)
                }
                put("generationConfig", genConfig)
            }

            val requestBody = jsonPayload.toString().toRequestBody(jsonMediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("فشل الاتصال بخدمة الترجمة الذكية (${response.code})")
                )
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val firstCandidate = candidates.getJSONObject(0)
                val content = firstCandidate.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    val translatedText = parts.getJSONObject(0).optString("text", "").trim()
                    if (translatedText.isNotEmpty()) {
                        return@withContext Result.success(translatedText)
                    }
                }
            }

            Result.failure(Exception("لم يتم تلقي ترجمة صالحة من النموذج"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
