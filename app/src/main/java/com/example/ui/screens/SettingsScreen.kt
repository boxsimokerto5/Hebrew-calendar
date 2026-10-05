package com.example.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.localization.AppLanguage
import com.example.localization.StringResources
import com.example.notification.NotificationHelper
import com.example.ui.MainViewModel

@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onNavigateToAboutUs: () -> Unit = {},
    onNavigateToPrivacyPolicy: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val isHolidayNotif by viewModel.isHolidayNotifEnabled.collectAsStateWithLifecycle()
    val isActivityNotif by viewModel.isActivityNotifEnabled.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Notification permission denied.", Toast.LENGTH_SHORT).show()
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA)),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Top Sky Blue Banner
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.9f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = StringResources.get("tab_settings", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (language == AppLanguage.HEBREW) "הגדרות, שפה והתראות"
                                    else if (language == AppLanguage.INDONESIAN) "Bahasa, Notifikasi & Info Kalender"
                                    else "Language, Notifications & Info",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                val nextLang = when (language) {
                                    AppLanguage.INDONESIAN -> AppLanguage.HEBREW
                                    AppLanguage.HEBREW -> AppLanguage.ENGLISH
                                    AppLanguage.ENGLISH -> AppLanguage.INDONESIAN
                                }
                                viewModel.setLanguage(nextLang)
                            },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Text(
                                text = when (language) {
                                    AppLanguage.HEBREW -> "עב"
                                    AppLanguage.INDONESIAN -> "ID"
                                    AppLanguage.ENGLISH -> "EN"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // 1. Language Card
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE1F5FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Language,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = StringResources.get("settings_language_title", language),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238)
                                )
                                Text(
                                    text = StringResources.get("settings_language_desc", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF78909C),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        AppLanguage.entries.forEach { appLang ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.setLanguage(appLang)
                                    }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = language == appLang,
                                    onClick = { viewModel.setLanguage(appLang) },
                                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF0288D1)),
                                    modifier = Modifier.testTag("lang_radio_${appLang.code}")
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = appLang.nativeName,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (language == appLang) FontWeight.Bold else FontWeight.Normal,
                                        color = if (language == appLang) Color(0xFF0288D1) else Color(0xFF263238)
                                    )
                                    if (appLang.nativeName != appLang.displayName) {
                                        Text(
                                            text = appLang.displayName,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF78909C)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Notification Preferences Card
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFFFF3E0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = Color(0xFFF57C00),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = StringResources.get("settings_notifications_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Permission Notice if needed on Android 13+
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !NotificationHelper.hasNotificationPermission(context)) {
                            Surface(
                                color = Color(0xFFFFEBEE),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = "Izin Notifikasi Diperlukan",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color(0xFFC62828),
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Izinkan notifikasi agar aplikasi dapat memberikan pengingat otomatis menjelang hari besar Yahudi.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFFC62828)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Izinkan Notifikasi")
                                    }
                                }
                            }
                        }

                        // Holiday Notif Switch
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = StringResources.get("settings_holiday_notif", language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF263238)
                                )
                                Text(
                                    text = StringResources.get("settings_holiday_notif_desc", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF78909C)
                                )
                            }
                            Switch(
                                checked = isHolidayNotif,
                                onCheckedChange = { viewModel.setHolidayNotifEnabled(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF0288D1), checkedTrackColor = Color(0xFFB3E5FC)),
                                modifier = Modifier.testTag("holiday_notif_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Activity Notif Switch
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = StringResources.get("settings_activity_notif", language),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF263238)
                                )
                                Text(
                                    text = StringResources.get("settings_activity_notif_desc", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF78909C)
                                )
                            }
                            Switch(
                                checked = isActivityNotif,
                                onCheckedChange = { viewModel.setActivityNotifEnabled(it) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF0288D1), checkedTrackColor = Color(0xFFB3E5FC)),
                                modifier = Modifier.testTag("activity_notif_switch")
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Test Notification Button
                        Button(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !NotificationHelper.hasNotificationPermission(context)) {
                                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                } else {
                                    viewModel.sendTestNotification()
                                    Toast.makeText(context, StringResources.get("test_notification_success", language), Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("test_notification_button"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(StringResources.get("test_notification", language), color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 3. About Calendar Info Card
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
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF43A047),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = StringResources.get("calendar_info_title", language),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF263238)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = StringResources.get("calendar_info_desc", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF546E7A),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // 4. About Us Card Row
        item {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToAboutUs() }
                        .testTag("settings_about_us_row")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE1F5FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = StringResources.get("about_us", language),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238)
                                )
                                Text(
                                    text = StringResources.get("about_us_desc", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF78909C),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF0288D1),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // 5. Privacy Policy Card Row
        item {
            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToPrivacyPolicy() }
                        .testTag("settings_privacy_policy_row")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE8F5E9)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color(0xFF43A047),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = StringResources.get("privacy_policy", language),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF263238)
                                )
                                Text(
                                    text = StringResources.get("privacy_policy_desc", language),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF78909C),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color(0xFF43A047),
                            modifier = Modifier.size(20.dp)
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
