package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.localization.StringResources

@Composable
fun AboutUsScreen(
    language: AppLanguage,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onBack() }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sky Blue Top Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0288D1),
                                Color(0xFF03A9F4),
                                Color(0xFF29B6F6)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                                .testTag("about_back_button")
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = StringResources.get("back", language),
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = StringResources.get("about_us", language),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Luach Hebrew Calendar & Planner",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Overview & Identity
        item {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_hebrew_calendar_logo_1791223312208),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.HEBREW) "אודות לוח השנה העברי"
                                    else if (language == AppLanguage.INDONESIAN) "Visi & Misi Aplikasi"
                                    else "Vision & Mission",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238)
                                )
                                Text(
                                    text = "Luach Hebrew Calendar",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF0288D1),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.HEBREW)
                                "אפליקציית 'לוח שנה עברי' (Luach) נבנתה במטרה להנגיש את חוכמת הלוח הירחי-שמשי המסורתי בדיוק מתמטי מושלם ובממשק מודרני, בהיר ונוח לשימוש. היישום משלב בין תאריכים לועזיים לתאריכים עבריים, מספק פירוט מלא של כל החגים והצומות, ומאפשר תכנון משימות ואירועים יומיים."
                            else if (language == AppLanguage.INDONESIAN)
                                "Aplikasi 'Luach Hebrew Calendar' dikembangkan untuk menghadirkan perhitungan kalender Ibrani (Lunisolar) tradisional secara matematis presisi dalam antarmuka modern yang cerah, ramah, dan mudah dipahami. Aplikasi ini memadukan penanggalan Gregorian dan Ibrani secara berdampingan, menyediakan katalog lengkap hari raya Yahudi beserta tradisinya, serta membantu Anda mengelola agenda kegiatan harian tanpa bergantung pada koneksi internet."
                            else
                                "The 'Luach Hebrew Calendar' application is designed to make traditional Hebrew lunisolar timekeeping accessible with mathematical precision and a bright, friendly, modern design. It pairs Gregorian and Hebrew dates side-by-side, provides a comprehensive directory of Jewish holidays and traditions, and empowers you to plan activities offline with total privacy.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF455A64),
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }

        // Section 2: Algorithmic Precision
        item {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF3E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFFF57C00),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "דיוק אלגוריתמי וחישוב מולדות"
                                else if (language == AppLanguage.INDONESIAN) "Akurasi Algoritma Kalender Ibrani"
                                else "Algorithmic Precision & Calculations",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val features = if (language == AppLanguage.HEBREW) listOf(
                            "חישוב מחזור מטוני בן 19 שנה עם 7 שנים מעוברות (אדר א׳ ואדר ב׳).",
                            "קביעת תחילת יום לפי שקיעת החמה וצאת הכוכבים (תצוגת יום וערב).",
                            "ארבע דחיות הלכתיות (לא אד\"ו ראש, לא בד\"ו פסח וכו׳).",
                            "המרת תאריכים מלאה לאותיות גימטריא עבריות (כ״ד, כ״ה, תשפ״ז)."
                        ) else if (language == AppLanguage.INDONESIAN) listOf(
                            "Siklus Metonik 19 tahun dengan 7 tahun kabisat (Adar I dan Adar II).",
                            "Perhitungan pergantian hari saat matahari terbenam (Tampilan Siang & Sore/Malam).",
                            "Penerapan aturan penundaan Dechiyot (Rosh Hashanah tidak jatuh pada hari Rabu, Jumat, atau Minggu).",
                            "Konversi otomatis angka aksara Ibrani (Gematria) untuk tanggal dan tahun."
                        ) else listOf(
                            "19-year Metonic cycle with 7 leap years (Adar I and Adar II).",
                            "Sunset day transitions (Daytime and Evening/Sunset dates).",
                            "Four traditional postponement rules (Dechiyot) strictly observed.",
                            "Full conversion to Hebrew Gematria letters for dates and years."
                        )

                        features.forEach { itemText ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF43A047),
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = itemText,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF455A64),
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Section 3: Cultural & Multilingual
        item {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Language,
                                    contentDescription = null,
                                    tint = Color(0xFF43A047),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "תמיכה רב-לשונית ו-RTL מלא"
                                else if (language == AppLanguage.INDONESIAN) "Multi-Bahasa & Tata Letak RTL Asli"
                                else "Multilingual & Native RTL Support",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.HEBREW)
                                "האפליקציה תומכת בשלוש שפות: עִבְרִית (עם כיווניות מימין לשמאל מלאה), אינדונזית ואנגלית. ניתן לעבור בין השפות באופן מיידי בכל עת מכל מסך."
                            else if (language == AppLanguage.INDONESIAN)
                                "Aplikasi mendukung 3 bahasa: Bahasa Indonesia, עברית (Bahasa Ibrani dengan tata letak Right-to-Left / kanan-ke-kiri penuh), dan English. Beralih bahasa dapat dilakukan secara instan dari layar mana saja."
                            else
                                "The app fully supports three languages: Hebrew (with complete Right-to-Left alignment), Indonesian, and English. Switch languages instantly anytime.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF455A64),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Section 4: Privacy & Offline Architecture
        item {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDE7F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF5E35B1),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "אבטחה ופרטיות ללא תלות ברשת"
                                else if (language == AppLanguage.INDONESIAN) "Privasi & Arsitektur Offline"
                                else "Privacy & Standalone Offline Architecture",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.HEBREW)
                                "כל המידע, לוחות הזמנים והמשימות שאתם רושמים נשמרים ישירות בזיכרון המקומי של המכשיר באמצעות מסד נתונים פנימי (Room/SQLite). אין צורך בהתחברות לחשבון ענן, אין איסוף נתונים ואין שיתוף מידע עם צד שלישי."
                            else if (language == AppLanguage.INDONESIAN)
                                "Semua agenda dan catatan yang Anda simpan tersimpan murni di memori internal perangkat ponsel menggunakan basis data lokal Room/SQLite. Tanpa perlu server cloud, tanpa akun login, dan bebas dari pelacakan eksternal."
                            else
                                "All appointments and planned activities remain strictly on your device using local SQLite/Room database. No cloud account required, zero tracking, and complete user privacy.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF455A64),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Section 5: Technical Details & Credits
        item {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE1F5FE).copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Luach Hebrew Calendar",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0288D1)
                        )
                        Text(
                            text = "${StringResources.get("app_version", language)} 1.0.0 (Release)",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF546E7A),
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Built with Kotlin, Jetpack Compose, Material Design 3, & Room",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF78909C)
                        )
                        Text(
                            text = "© 2026 Luach Calendar Project. All rights reserved.",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF90A4AE)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
