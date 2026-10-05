package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.localization.AppLanguage
import com.example.localization.StringResources

@Composable
fun PrivacyPolicyScreen(
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
                                .testTag("privacy_back_button")
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
                                text = StringResources.get("privacy_policy", language),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Komitmen Keamanan & Perlindungan Privasi",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // Section 1: Introduction & Non-Collection Guarantee
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "מדיניות אפס איסוף מידע"
                                else if (language == AppLanguage.INDONESIAN) "Jaminan Nol Pengumpulan Data"
                                else "Zero Data Collection Commitment",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.HEBREW)
                                "אנו מחויבים באופן מלא להגנת פרטיותכם. אפליקציית 'לוח שנה עברי' אינה אוספת, אינה עוקבת ואינה משדרת אף מידע אישי מזהה (PII) לשרתים חיצוניים. היישום פועל במתכונת עצמאית לחלוטין וללא צורך בהרשמה או התחברות."
                            else if (language == AppLanguage.INDONESIAN)
                                "Kami berkomitmen penuh untuk melindungi privasi Anda. Aplikasi 'Luach Hebrew Calendar' TIDAK mengumpulkan, melacak, mentransmisikan, atau menjual data pribadi Anda (seperti nama, email, lokasi, kontak, atau nomor telepon) ke server eksternal mana pun. Aplikasi ini dapat digunakan secara penuh tanpa memerlukan pendaftaran akun atau koneksi internet."
                            else
                                "We are completely committed to protecting your privacy. The 'Luach Hebrew Calendar' app does NOT collect, track, transmit, or sell your personal identifiable information (PII) to any external servers. The app operates standalone without requiring user registration or account login.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF455A64),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Section 2: Local Storage (Room Database)
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE1F5FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "אחסון נתונים מקומי בלבד"
                                else if (language == AppLanguage.INDONESIAN) "Penyimpanan Data Lokal di Perangkat"
                                else "Strict On-Device Data Storage",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.HEBREW)
                                "כל המשימות, האירועים וההערות שאתם רושמים נשמרים אך ורק בתוך מסד הנתונים הפנימי של המכשיר (Room SQLite Sandbox). לאף גורם חיצוני או יישום אחר אין גישה לנתונים אלו."
                            else if (language == AppLanguage.INDONESIAN)
                                "Seluruh catatan, judul kegiatan, dan jadwal yang Anda masukkan disimpan secara eksklusif di dalam memori internal perangkat Anda menggunakan basis data terenkripsi lokal Android (Room SQLite Sandbox). Tidak ada pihak ketiga yang dapat mengakses data tersebut."
                            else
                                "All appointments, notes, and activity schedules entered by the user are stored solely within the device's local application sandbox (Room SQLite). No third party or external service has access to your local data.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF455A64),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Section 3: Device Permissions Explained
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF3E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = Color(0xFFF57C00),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "פירוט הרשאות המכשיר"
                                else if (language == AppLanguage.INDONESIAN) "Penjelasan Izin Perangkat"
                                else "Explanation of Device Permissions",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        val permissions = if (language == AppLanguage.HEBREW) listOf(
                            "POST_NOTIFICATIONS: משמש אך ורק לשליחת תזכורות אוטומטיות לחגים עבריים ולמשימות שקבעתם. איננו שולחים פרסומות או התראות שיווקיות.",
                            "VIBRATE: מאפשר רטט עדין בהתרעה כדי להבטיח שלא תפספסו את מועד החג.",
                            "ההרשאות נתונות לשליטתכם המלאה וניתן לבטלן בכל עת בהגדרות המכשיר."
                        ) else if (language == AppLanguage.INDONESIAN) listOf(
                            "POST_NOTIFICATIONS: Digunakan semata-mata untuk membunyikan pengingat jadwal hari raya Yahudi dan agenda kegiatan yang Anda tentukan sendiri. Kami tidak pernah mengirimkan iklan promosi atau spam.",
                            "VIBRATE: Digunakan untuk memberikan getaran saat pengingat waktu kegiatan tiba.",
                            "Izin ini sepenuhnya berada dalam kendali Anda dan dapat dimatikan kapan saja melalui menu Pengaturan."
                        ) else listOf(
                            "POST_NOTIFICATIONS: Exclusively used to deliver holiday alarms and user-scheduled reminders. Never used for marketing or ads.",
                            "VIBRATE: Provides gentle haptic feedback during alerts.",
                            "You may grant or revoke these permissions at any time through system settings."
                        )

                        permissions.forEach { perm ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = perm,
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

        // Section 4: Data Retention & User Control
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFEBEE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = null,
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "מחיקה ושליטה מלאה בנתונים"
                                else if (language == AppLanguage.INDONESIAN) "Hak Hapus & Kontrol Pengguna"
                                else "Data Deletion & User Rights",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.HEBREW)
                                "אתם בעלי השליטה המלאים בנתוניכם. ניתן לערוך או למחוק כל אירוע בלחיצת כפתור. מחיקת האפליקציה מהמכשיר או ניקוי נתוניה תמחק את כל המידע המקומי לצמיתות באופן מיידי."
                            else if (language == AppLanguage.INDONESIAN)
                                "Anda memegang kendali penuh atas data Anda. Setiap kegiatan dapat diedit atau dihapus kapan saja dengan menekan tombol hapus. Menghapus aplikasi dari perangkat atau melakukan 'Hapus Data' pada Pengaturan Android akan melenyapkan seluruh data secara permanen seketika."
                            else
                                "You retain 100% control over your data. You can delete or edit any event at any time. Uninstalling the application or clearing app data in Android Settings instantly and permanently removes all stored data.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF455A64),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Section 5: Children's Privacy & Play Policy Compliance
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEDE7F6)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color(0xFF5E35B1),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "פרטיות ילדים ותאימות מלאה"
                                else if (language == AppLanguage.INDONESIAN) "Privasi Anak & Kepatuhan Kebijakan"
                                else "Children's Privacy & Compliance",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = if (language == AppLanguage.HEBREW)
                                "היישום מתאים לכל הגילאים, אינו מכיל תכנים בלתי הולמים, ועומד במלוא דרישות מדיניות Google Play Developer Program ו-COPPA."
                            else if (language == AppLanguage.INDONESIAN)
                                "Aplikasi ini aman digunakan oleh semua kelompok usia. Kami mematuhi seluruh panduan Google Play Developer Program dan COPPA, serta tidak menyajikan konten yang membahayakan anak-anak."
                            else
                                "The app is family-friendly, suitable for all ages, and complies with Google Play Developer Program policies and COPPA requirements.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF455A64),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Section 6: Contact & Date of Policy
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
                            text = if (language == AppLanguage.HEBREW) "תאריך עדכון אחרון: אוקטובר 2026"
                            else if (language == AppLanguage.INDONESIAN) "Terakhir Diperbarui: Oktober 2026"
                            else "Last Updated: October 2026",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0288D1)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Email Dukungan: support@luachcalendar.local",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF546E7A)
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
