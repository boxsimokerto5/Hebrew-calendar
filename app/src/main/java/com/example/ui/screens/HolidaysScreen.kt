package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calendar.HolidayCategory
import com.example.calendar.HolidayInstance
import com.example.calendar.JewishHoliday
import com.example.localization.AppLanguage
import com.example.localization.StringResources
import com.example.ui.HolidayFilterTab
import com.example.ui.MainViewModel
import com.example.ui.components.HolidayDetailDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun HolidaysScreen(
    viewModel: MainViewModel,
    onNavigateToDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val upcomingHolidays by viewModel.upcomingHolidays.collectAsStateWithLifecycle()
    val currentFilter by viewModel.holidayFilter.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    var holidayToShowDetail by remember { mutableStateOf<JewishHoliday?>(null) }
    var holidayInstanceDetail by remember { mutableStateOf<HolidayInstance?>(null) }

    val today = LocalDate.now()

    val filteredList = remember(upcomingHolidays, currentFilter, searchQuery, language) {
        upcomingHolidays.filter { instance ->
            val h = instance.holiday
            val matchesFilter = when (currentFilter) {
                HolidayFilterTab.ALL -> true
                HolidayFilterTab.MAJOR -> h.category == HolidayCategory.MAJOR_YOM_TOV
                HolidayFilterTab.FASTS -> h.category == HolidayCategory.FAST_DAY
                HolidayFilterTab.MINOR -> h.category == HolidayCategory.MINOR_HOLIDAY || h.category == HolidayCategory.MODERN_MEMORIAL || h.category == HolidayCategory.ROSH_CHODESH
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                h.nameEn.contains(searchQuery, ignoreCase = true) ||
                h.nameId.contains(searchQuery, ignoreCase = true) ||
                h.nameHe.contains(searchQuery, ignoreCase = true) ||
                h.getDescription(language).contains(searchQuery, ignoreCase = true)
            }
            matchesFilter && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA)),
        verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                    Icons.Default.Celebration,
                                    contentDescription = null,
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = StringResources.get("tab_holidays", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (language == AppLanguage.HEBREW) "חגים, צומות ומועדי ישראל"
                                    else if (language == AppLanguage.INDONESIAN) "Hari Raya, Puasa & Peringatan Yahudi"
                                    else "Jewish Holidays & Observances",
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // Search input styled brightly
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setSearchQuery(it) },
                        placeholder = {
                            Text(
                                StringResources.get("search_holidays_hint", language),
                                color = Color(0xFF78909C),
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF0288D1))
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedBorderColor = Color(0xFFFFEB3B),
                            unfocusedBorderColor = Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("holiday_search_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }
        }

        // Filter chips bar
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = currentFilter == HolidayFilterTab.ALL,
                    onClick = { viewModel.setHolidayFilter(HolidayFilterTab.ALL) },
                    label = { Text(StringResources.get("filter_all", language), fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF0288D1),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_all_chip")
                )
                FilterChip(
                    selected = currentFilter == HolidayFilterTab.MAJOR,
                    onClick = { viewModel.setHolidayFilter(HolidayFilterTab.MAJOR) },
                    label = { Text(StringResources.get("filter_major", language), fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFF57C00),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_major_chip")
                )
                FilterChip(
                    selected = currentFilter == HolidayFilterTab.FASTS,
                    onClick = { viewModel.setHolidayFilter(HolidayFilterTab.FASTS) },
                    label = { Text(StringResources.get("filter_fast", language), fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFD32F2F),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_fast_chip")
                )
                FilterChip(
                    selected = currentFilter == HolidayFilterTab.MINOR,
                    onClick = { viewModel.setHolidayFilter(HolidayFilterTab.MINOR) },
                    label = { Text(StringResources.get("filter_minor", language), fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF43A047),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.testTag("filter_minor_chip")
                )
            }
        }

        // List of holidays with cheerful cards
        items(filteredList) { instance ->
            val holiday = instance.holiday
            val daysUntil = ChronoUnit.DAYS.between(today, instance.gregorianDate)

            val countdownBadgeText = when {
                daysUntil == 0L -> StringResources.get("today", language)
                daysUntil == 1L -> if (language == AppLanguage.HEBREW) "מחר" else if (language == AppLanguage.INDONESIAN) "Besok" else "Tomorrow"
                daysUntil > 1L -> String.format(StringResources.get("in_days", language), daysUntil)
                else -> String.format(StringResources.get("days_ago", language), -daysUntil)
            }

            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            holidayToShowDetail = holiday
                            holidayInstanceDetail = instance
                        }
                        .testTag("holiday_card_${holiday.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = holiday.getName(language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = when (holiday.category) {
                                        HolidayCategory.MAJOR_YOM_TOV -> Color(0xFFE65100)
                                        HolidayCategory.FAST_DAY -> Color(0xFFC62828)
                                        else -> Color(0xFF0288D1)
                                    }
                                )
                                Text(
                                    text = holiday.nameHe,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color(0xFF78909C),
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (daysUntil <= 1L) Color(0xFFFFF9C4) else Color(0xFFE1F5FE)
                            ) {
                                Text(
                                    text = countdownBadgeText,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (daysUntil <= 1L) Color(0xFFF57F17) else Color(0xFF0288D1),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Dates row (Masehi & Ibrani)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = instance.hebrewDate.getFormattedHebrew(language),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF0288D1),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = instance.gregorianDate.format(
                                    DateTimeFormatter.ofPattern("d MMMM yyyy", when (language) {
                                        AppLanguage.INDONESIAN -> Locale("id")
                                        AppLanguage.HEBREW -> Locale("he")
                                        AppLanguage.ENGLISH -> Locale.ENGLISH
                                    })
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF78909C)
                            )
                        }

                        // Summary description
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = holiday.getDescription(language),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF455A64),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFECEFF1)
                            ) {
                                Text(
                                    text = holiday.getCategoryName(language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF546E7A),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .clickable {
                                        onNavigateToDate(instance.gregorianDate)
                                    }
                                    .padding(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = Color(0xFF0288D1)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = StringResources.get("tab_calendar", language),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF0288D1),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    holidayToShowDetail?.let { holiday ->
        HolidayDetailDialog(
            holiday = holiday,
            instance = holidayInstanceDetail,
            language = language,
            onDismiss = {
                holidayToShowDetail = null
                holidayInstanceDetail = null
            }
        )
    }
}
