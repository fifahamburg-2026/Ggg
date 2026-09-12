package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TranslationResult
import com.example.ui.theme.StarGold
import com.example.ui.theme.TealAccent

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TranslationCard(
    inputText: String,
    result: TranslationResult?,
    sourceLang: String,
    targetLang: String,
    isCurrentSaved: Boolean,
    onInputChanged: (String) -> Unit,
    onClearInput: () -> Unit,
    onSpeakSource: () -> Unit,
    onSpeakTranslation: () -> Unit,
    onToggleFavorite: () -> Unit,
    onShowToast: (String) -> Unit,
    isAiLoading: Boolean = false,
    onTranslateWithAi: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // SOURCE INPUT CARD
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            shadowElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Input header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (sourceLang == "ar") "النص بالعربية" else "English Text",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (inputText.isEmpty()) {
                            // Paste button
                            IconButton(
                                onClick = {
                                    val sysClipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                    val clip = sysClipboard?.primaryClip
                                    if (clip != null && clip.itemCount > 0) {
                                        val pasteText = clip.getItemAt(0).text?.toString() ?: ""
                                        if (pasteText.isNotEmpty()) {
                                            onInputChanged(pasteText)
                                            onShowToast("تم لصق النص")
                                        }
                                    }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "لصق",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            // Clear button
                            IconButton(
                                onClick = onClearInput,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("clear_input_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "مسح",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (inputText.isNotEmpty()) {
                            // Audio speak source
                            IconButton(
                                onClick = onSpeakSource,
                                modifier = Modifier
                                    .size(36.dp)
                                    .testTag("speak_source_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "استماع للنص الأصلي",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Input text field
                OutlinedTextField(
                    value = inputText,
                    onValueChange = onInputChanged,
                    placeholder = {
                        Text(
                            text = if (sourceLang == "ar")
                                "اكتب هنا للترجمة الفورية المدمجة بدون إنترنت..."
                            else
                                "Type here for instant offline embedded translation...",
                            fontSize = 15.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 100.dp)
                        .testTag("translation_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 17.sp,
                        lineHeight = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                // Char count
                if (inputText.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "${inputText.length} حرفاً",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // TRANSLATION RESULT CARD
        AnimatedVisibility(
            visible = result != null && result.translatedText.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            result?.let { res ->
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    tonalElevation = 4.dp,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("translation_result_card")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        // Header row with language name and speed pill
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (targetLang == "ar") "الترجمة (العربية)" else "Translation (English)",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Instant local speed badge
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TealAccent.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlashOn,
                                    contentDescription = null,
                                    tint = TealAccent,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "فوري محلي (${res.processingTimeMs} ms)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TealAccent
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Translated text (large and prominent)
                        Text(
                            text = res.translatedText,
                            fontSize = if (targetLang == "ar") 22.sp else 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = if (targetLang == "ar") 32.sp else 28.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("translated_text_output")
                        )

                        // Phonetic transliteration / pronunciation guide
                        if (res.phonetic.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "🗣️ ${res.phonetic}",
                                fontSize = 13.sp,
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Dictionary synonyms / alternative meanings
                        if (res.alternativeMeanings.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "معانٍ ومرادفات أخرى:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                res.alternativeMeanings.forEach { alt ->
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surface)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = alt,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Action buttons row: Listen, Copy, Star, Share
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // TTS Speak
                            IconButton(
                                onClick = onSpeakTranslation,
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("speak_translation_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "استماع للنطق",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Copy to clipboard
                            IconButton(
                                onClick = {
                                    val clip = ClipData.newPlainText("translation", res.translatedText)
                                    (context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager)?.setPrimaryClip(clip)
                                    onShowToast("تم نسخ الترجمة إلى الحافظة")
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("copy_translation_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "نسخ",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(19.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Favorite / Bookmark
                            IconButton(
                                onClick = onToggleFavorite,
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("favorite_button")
                            ) {
                                Icon(
                                    imageVector = if (isCurrentSaved) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = "حفظ في المفضلة",
                                    tint = if (isCurrentSaved) StarGold else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(21.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            // Share Intent
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "${inputText}\n⬇️\n${res.translatedText}\n\n[مترجم أوفلاين مدمج]"
                                        )
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "مشاركة الترجمة"))
                                },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = "مشاركة",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(19.dp)
                                )
                            }
                        }

                        // Smart AI translation banner for long text or deep context
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isAiLoading) {
                                    onTranslateWithAi()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FlashOn,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isAiLoading) "جاري الترجمة السحابية..." else "ترجمة ذكية متقدمة (Gemini AI)",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Text(
                                    text = if (isAiLoading) "انتظر لحظة..." else "اضغط هنا ✨",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick suggestions when input is empty
        if (inputText.isEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            QuickSuggestionChips(
                sourceLang = sourceLang,
                onSelect = onInputChanged
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickSuggestionChips(
    sourceLang: String,
    onSelect: (String) -> Unit
) {
    val suggestions = if (sourceLang == "ar") {
        listOf(
            "صباح الخير",
            "كيف حالك؟",
            "شكراً جزيلاً",
            "أين أقرب مستشفى؟",
            "كم سعر هذا؟",
            "أنا أحتاج إلى مساعدة",
            "أريد كوب ماء من فضلك"
        )
    } else {
        listOf(
            "Good morning",
            "How are you?",
            "Thank you very much",
            "Where is the airport?",
            "How much is this?",
            "I need help",
            "A cup of coffee, please"
        )
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "عبارات سريعة مقترحة:",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            suggestions.forEach { phrase ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onSelect(phrase) }
                ) {
                    Text(
                        text = phrase,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                    )
                }
            }
        }
    }
}
