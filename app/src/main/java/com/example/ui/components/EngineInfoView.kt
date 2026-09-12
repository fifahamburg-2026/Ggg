package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.OfflineGreen
import com.example.ui.theme.TealAccent

@Composable
fun EngineInfoView(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            // Main Hero Banner
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(OfflineGreen.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = null,
                            tint = OfflineGreen,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "محرك ترجمة مدمج 100% محلي",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "صُمم هذا النظام ليعمل بالكامل بدون اتصال بالإنترنت وبدون أي تنزيلات إضافية، لحماية خصوصيتك وضمان السرعة اللحظية في أي مكان.",
                        fontSize = 13.sp,
                        lineHeight = 20.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }

        item {
            // Section 1: Core Advantages
            Text(
                text = "ميزات المحرك المدمج",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            )
        }

        item {
            FeatureInfoCard(
                icon = Icons.Default.DownloadDone,
                iconColor = TealAccent,
                title = "صفر تنزيلات (Zero Downloads)",
                description = "قاموس اللغتين العربية والإنجليزية مدمج مباشرة داخل حزمة التطبيق. لا يتطلب تحميل نماذج ضخمة أو حزم لغات عبر الشبكة."
            )
        }

        item {
            FeatureInfoCard(
                icon = Icons.Default.Security,
                iconColor = OfflineGreen,
                title = "خصوصية تامة 100%",
                description = "نصوصك وترجماتك لا تغادر هاتفك مطلقاً. لا توجد خوادم وسيطة ولا تتبع ولا جمع بيانات على الإطلاق."
            )
        }

        item {
            FeatureInfoCard(
                icon = Icons.Default.Bolt,
                iconColor = Color(0xFFEAB308),
                title = "سرعة استجابة فائقة (< 5ms)",
                description = "تتم معالجة الترجمة في أجزاء من الثانية بفضل خوارزميات الفهرسة اللحظية، مما يسمح بالترجمة الحية أثناء الكتابة مباشرة."
            )
        }

        item {
            FeatureInfoCard(
                icon = Icons.Default.Psychology,
                iconColor = MaterialTheme.colorScheme.primary,
                title = "معالجة صرفية ونحوية ذكية",
                description = "تحليل السوابق واللواحق في العربية (الـ التعريف، حروف العطف، الضمائر المتصلة)، وتصريف الأفعال الإنجليزية، وترتيب الصفات والموصوفات تلقائياً."
            )
        }

        item {
            FeatureInfoCard(
                icon = Icons.Default.MenuBook,
                iconColor = Color(0xFF8B5CF6),
                title = "قاموس ثنائي ودليل عبارات",
                description = "يحتوي على آلاف المفردات الأساسية بالإضافة إلى دليل عبارات متكامل ومبوب للسفر، الطعام، الطوارئ، والتسوق."
            )
        }

        item {
            // Offline Stats Summary
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "إحصائيات النظام المحلي:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    StatRow(label = "حالة الاتصال المطلوبة:", value = "غير مطلوب (Offline)", isGreen = true)
                    StatRow(label = "الملفات المحمّلة من الإنترنت:", value = "0 ميجابايت", isGreen = true)
                    StatRow(label = "مستوى التشفير والخصوصية:", value = "محلي مشفر على جهازك فقط", isGreen = true)
                    StatRow(label = "دعم النطق الصوتي (TTS):", value = "مدمج عبر نظام Android", isGreen = false)
                }
            }
        }
    }
}

@Composable
private fun FeatureInfoCard(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    description: String
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String, isGreen: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (isGreen) OfflineGreen else MaterialTheme.colorScheme.primary
        )
    }
}
