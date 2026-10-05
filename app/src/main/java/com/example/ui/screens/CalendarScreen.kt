package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.calendar.HebrewCalendarEngine
import com.example.calendar.HolidayCategory
import com.example.calendar.HolidayInstance
import com.example.calendar.JewishHoliday
import com.example.data.PlannerEvent
import com.example.localization.AppLanguage
import com.example.localization.StringResources
import com.example.ui.MainViewModel
import com.example.ui.components.AddEditEventDialog
import com.example.ui.components.HolidayDetailDialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedHebrewDate by viewModel.selectedHebrewDate.collectAsStateWithLifecycle()
    val viewYear by viewModel.viewYear.collectAsStateWithLifecycle()
    val viewMonth by viewModel.viewMonth.collectAsStateWithLifecycle()
    val holidaysOnDate by viewModel.holidaysOnSelectedDate.collectAsStateWithLifecycle()
    val eventsOnDate by viewModel.eventsOnSelectedDate.collectAsStateWithLifecycle()
    val datesWithEvents by viewModel.datesWithEvents.collectAsStateWithLifecycle()

    var showAddEventDialog by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<PlannerEvent?>(null) }
    var holidayToShowDetail by remember { mutableStateOf<JewishHoliday?>(null) }
    var holidayInstanceDetail by remember { mutableStateOf<HolidayInstance?>(null) }
    var showHintBanner by remember { mutableStateOf(true) }

    val currentYearMonth = YearMonth.of(viewYear, viewMonth)
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1)
    val startDayOffset = (firstDayOfMonth.dayOfWeek.value % 7) // 0 = Sunday, 6 = Saturday

    val firstHebrewDate = remember(viewYear, viewMonth) {
        HebrewCalendarEngine.fromLocalDate(firstDayOfMonth)
    }
    val lastHebrewDate = remember(viewYear, viewMonth) {
        HebrewCalendarEngine.fromLocalDate(currentYearMonth.atDay(daysInMonth))
    }

    // Next day sunset transition Hebrew Date (in Jewish reckoning, eve starts next Hebrew day)
    val eveningHebrewDate = remember(selectedDate) {
        HebrewCalendarEngine.fromLocalDate(selectedDate.plusDays(1))
    }

    val hebrewMonthHeader = remember(firstHebrewDate, lastHebrewDate, language) {
        val name1 = when (language) {
            AppLanguage.HEBREW -> firstHebrewDate.monthNameHe
            AppLanguage.INDONESIAN -> firstHebrewDate.monthNameId
            AppLanguage.ENGLISH -> firstHebrewDate.monthNameEn
        }
        val name2 = when (language) {
            AppLanguage.HEBREW -> lastHebrewDate.monthNameHe
            AppLanguage.INDONESIAN -> lastHebrewDate.monthNameId
            AppLanguage.ENGLISH -> lastHebrewDate.monthNameEn
        }
        val yearStr = if (language == AppLanguage.HEBREW) firstHebrewDate.hebrewYearGematria else firstHebrewDate.year.toString()
        if (firstHebrewDate.monthCode == lastHebrewDate.monthCode) {
            "${firstHebrewDate.day} $name1 - ${lastHebrewDate.day} $name1 $yearStr"
        } else {
            "${firstHebrewDate.day} $name1 - ${lastHebrewDate.day} $name2 $yearStr"
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FA)),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. SKY BLUE TOP BANNER (Header seperti di foto)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF0288D1),
                                Color(0xFF03A9F4),
                                Color(0xFF29B6F6),
                                Color(0xFF4FC3F7)
                            )
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Column {
                    // Profile & Top Icon row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_hebrew_calendar_logo_1791223312208),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(8.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (language == AppLanguage.HEBREW) "שָׁלוֹם עֲלֵיכֶם"
                                    else if (language == AppLanguage.INDONESIAN) "Shalom! Kalender Ibrani"
                                    else "Shalom! Hebrew Calendar",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Luach & Activity Planner",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
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

                            IconButton(
                                onClick = { showAddEventDialog = true },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    Icons.Default.EditNote,
                                    contentDescription = "Add Schedule",
                                    tint = Color.White
                                )
                            }

                            IconButton(
                                onClick = { viewModel.goToToday() },
                                modifier = Modifier.size(34.dp)
                            ) {
                                Icon(
                                    Icons.Default.Today,
                                    contentDescription = "Today",
                                    tint = Color(0xFFFFEB3B)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gregorian Date in Big White Font
                    Text(
                        text = selectedDate.format(
                            DateTimeFormatter.ofPattern("d MMMM yyyy", when (language) {
                                AppLanguage.INDONESIAN -> Locale("id")
                                AppLanguage.HEBREW -> Locale("he")
                                AppLanguage.ENGLISH -> Locale.ENGLISH
                            })
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Day Hebrew Date with Sun icon
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.WbSunny,
                            contentDescription = "Day",
                            tint = Color(0xFFFFEB3B),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${selectedHebrewDate.getDayOfWeekName(language)}, ${selectedHebrewDate.getFormattedHebrew(language)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFEB3B)
                        )
                    }

                    // Evening / Sunset Hebrew Date with Moon icon (Hebrew transition)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Icon(
                            Icons.Default.Bedtime,
                            contentDescription = "Night",
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (language == AppLanguage.HEBREW) "עֶרֶב: ${eveningHebrewDate.getFormattedHebrew(language)}"
                            else if (language == AppLanguage.INDONESIAN) "Sore/Malam: ${eveningHebrewDate.getFormattedHebrew(language)}"
                            else "Eve/Sunset: ${eveningHebrewDate.getFormattedHebrew(language)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFFFD54F),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick button: Catat Jadwal / Tanggal Janjian
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.25f),
                        modifier = Modifier
                            .clickable { showAddEventDialog = true }
                            .testTag("banner_catat_jadwal")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (language == AppLanguage.HEBREW) "הוסף פגישה / משימה חדשה"
                                else if (language == AppLanguage.INDONESIAN) "Catat Jadwal / Tanggal Janjian"
                                else "Add Schedule / Appointment",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 2. HELPFUL INFO STRIP (Biru muda seperti di foto)
        if (showHintBanner) {
            item {
                Surface(
                    color = Color(0xFF1976D2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (language == AppLanguage.HEBREW) "לחץ על כל תאריך בלוח כדי לצפות בחגים ולרשום משימות"
                            else if (language == AppLanguage.INDONESIAN) "Ketuk tanggal pada kalender untuk melihat hari raya & mencatat jadwal"
                            else "Tap any date to inspect Jewish holidays and record plans",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier.weight(1f),
                            fontSize = 11.sp
                        )
                        IconButton(
                            onClick = { showHintBanner = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close hint",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // 3. MONTH & YEAR NAVIGATION HEADER (Oranye tebal besar seperti di foto)
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.prevMonth() },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("prev_month_btn")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous Month",
                            tint = Color(0xFF37474F)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Month in BOLD ORANGE (seperti SEPTEMBER 2026 di foto)
                        Text(
                            text = currentYearMonth.month.getDisplayName(
                                TextStyle.FULL,
                                when (language) {
                                    AppLanguage.INDONESIAN -> Locale("id")
                                    AppLanguage.HEBREW -> Locale("he")
                                    AppLanguage.ENGLISH -> Locale.ENGLISH
                                }
                            ).uppercase() + " $viewYear",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFF57C00), // Vibrant Amber Orange
                            letterSpacing = 1.sp
                        )

                        // Hebrew months range subtitle
                        Text(
                            text = hebrewMonthHeader,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF263238)
                        )
                    }

                    IconButton(
                        onClick = { viewModel.nextMonth() },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("next_month_btn")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next Month",
                            tint = Color(0xFF37474F)
                        )
                    }
                }
            }
        }

        // 4. COLORFUL PILL DAY HEADERS (MIN, SEN, SEL, RAB, KAM, JUM, SAB)
        item {
            val weekDayPills = when (language) {
                AppLanguage.HEBREW -> listOf(
                    Triple("א׳", Color(0xFFE53935), "יום ראשון"), // Red
                    Triple("ב׳", Color(0xFF1E88E5), "יום שני"),   // Blue
                    Triple("ג׳", Color(0xFF1E88E5), "יום שלישי"), // Blue
                    Triple("ד׳", Color(0xFF1E88E5), "יום רביעי"), // Blue
                    Triple("ה׳", Color(0xFF1E88E5), "יום חמישי"), // Blue
                    Triple("ו׳", Color(0xFF43A047), "יום שישי"),  // Green (Erev Shabbat)
                    Triple("ש׳", Color(0xFF1565C0), "שַׁבָּת")     // Deep Blue (Shabbat)
                )
                AppLanguage.INDONESIAN -> listOf(
                    Triple("MIN", Color(0xFFE53935), "Minggu"), // Red
                    Triple("SEN", Color(0xFF1E88E5), "Senin"),  // Blue
                    Triple("SEL", Color(0xFF1E88E5), "Selasa"), // Blue
                    Triple("RAB", Color(0xFF1E88E5), "Rabu"),   // Blue
                    Triple("KAM", Color(0xFF1E88E5), "Kamis"),  // Blue
                    Triple("JUM", Color(0xFF43A047), "Jumat"),  // Green
                    Triple("SAB", Color(0xFF1565C0), "Sabtu")   // Deep Blue
                )
                AppLanguage.ENGLISH -> listOf(
                    Triple("SUN", Color(0xFFE53935), "Sunday"),
                    Triple("MON", Color(0xFF1E88E5), "Monday"),
                    Triple("TUE", Color(0xFF1E88E5), "Tuesday"),
                    Triple("WED", Color(0xFF1E88E5), "Wednesday"),
                    Triple("THU", Color(0xFF1E88E5), "Thursday"),
                    Triple("FRI", Color(0xFF43A047), "Friday"),
                    Triple("SAT", Color(0xFF1565C0), "Saturday")
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                weekDayPills.forEach { (label, pillColor, _) ->
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(26.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = pillColor
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        // 5. THE WALL CALENDAR GRID (Format persis seperti di screenshot)
        item {
            val totalCells = startDayOffset + daysInMonth
            val totalRows = (totalCells + 6) / 7
            val today = LocalDate.now()

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (rowIndex in 0 until totalRows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            for (colIndex in 0 until 7) {
                                val cellIndex = rowIndex * 7 + colIndex
                                val dayNumber = cellIndex - startDayOffset + 1

                                if (dayNumber in 1..daysInMonth) {
                                    val cellDate = currentYearMonth.atDay(dayNumber)
                                    val cellHebrew = HebrewCalendarEngine.fromLocalDate(cellDate)
                                    val isSelected = cellDate == selectedDate
                                    val isToday = cellDate == today
                                    val cellHolidays = HebrewCalendarEngine.getHolidaysForDate(cellDate)
                                    val hasMajorHoliday = cellHolidays.any { it.holiday.category == HolidayCategory.MAJOR_YOM_TOV || it.holiday.category == HolidayCategory.FAST_DAY }
                                    val isSunday = colIndex == 0
                                    val isFriday = colIndex == 5
                                    val isSaturday = colIndex == 6
                                    val hasUserEvents = datesWithEvents.contains(cellDate.format(DateTimeFormatter.ISO_LOCAL_DATE))

                                    // Color logic: Red for Sunday/Major Holidays, Green for Friday, Blue for Saturday, Dark grey for weekdays
                                    val numberColor = when {
                                        isSunday || hasMajorHoliday -> Color(0xFFD32F2F)
                                        isFriday -> Color(0xFF2E7D32)
                                        isSaturday -> Color(0xFF1565C0)
                                        else -> Color(0xFF263238)
                                    }

                                    // Subtitle text: Holiday name if any, otherwise Hebrew day or month
                                    val subText = when {
                                        cellHolidays.isNotEmpty() -> {
                                            val primary = cellHolidays.first().holiday
                                            when (primary.id) {
                                                "rosh_hashanah" -> if (language == AppLanguage.HEBREW) "ר״ה" else "R.Hashanah"
                                                "yom_kippur" -> if (language == AppLanguage.HEBREW) "כיפור" else "Y.Kippur"
                                                "sukkot" -> if (language == AppLanguage.HEBREW) "סוכות" else "Sukkot"
                                                "hanukkah" -> if (language == AppLanguage.HEBREW) "חנוכה" else "Hanukkah"
                                                "purim" -> if (language == AppLanguage.HEBREW) "פורים" else "Purim"
                                                "pesach" -> if (language == AppLanguage.HEBREW) "פסח" else "Pesach"
                                                "shavuot" -> if (language == AppLanguage.HEBREW) "שבועות" else "Shavuot"
                                                "tisha_bav" -> if (language == AppLanguage.HEBREW) "ט׳ באב" else "9 Av"
                                                else -> {
                                                    if (cellHebrew.day == 1) cellHebrew.monthNameId
                                                    else cellHebrew.monthNameId
                                                }
                                            }
                                        }
                                        cellHebrew.day == 1 -> cellHebrew.monthNameId
                                        else -> "${cellHebrew.day} ${cellHebrew.monthNameId.take(3)}"
                                    }

                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(0.85f)
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(
                                                when {
                                                    isSelected -> Color(0xFFE1F5FE)
                                                    isToday -> Color(0xFFFFF9C4)
                                                    else -> Color.Transparent
                                                }
                                            )
                                            .border(
                                                width = if (isSelected) 1.5.dp else if (isToday) 1.dp else 0.dp,
                                                color = if (isSelected) Color(0xFF0288D1) else if (isToday) Color(0xFFFBC02D) else Color.Transparent,
                                                shape = RoundedCornerShape(6.dp)
                                            )
                                            .clickable {
                                                viewModel.selectDate(cellDate)
                                            }
                                            .testTag("wall_calendar_day_$dayNumber"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 2.dp, vertical = 2.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            // Top Row: Gregorian Number + Hebrew Gematria Superscript
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.Top
                                            ) {
                                                // Main Gregorian Day (Bold & prominent)
                                                Text(
                                                    text = dayNumber.toString(),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 17.sp,
                                                    color = numberColor
                                                )

                                                // Superscript Hebrew Gematria Character (persis angka Arab kecil di foto)
                                                Text(
                                                    text = cellHebrew.hebrewDayLetter,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 10.sp,
                                                    color = if (hasMajorHoliday) Color(0xFFD32F2F) else Color(0xFF546E7A),
                                                    modifier = Modifier.padding(start = 1.dp, top = 0.dp)
                                                )
                                            }

                                            // Bottom Subtitle: Hebrew Month / Holiday Label (persis penanggalan Jawa di foto)
                                            Text(
                                                text = subText,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontSize = 8.5.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                fontWeight = if (cellHolidays.isNotEmpty()) FontWeight.Bold else FontWeight.Normal,
                                                color = if (cellHolidays.isNotEmpty()) Color(0xFFC62828) else Color(0xFF78909C),
                                                textAlign = TextAlign.Center
                                            )

                                            // Planned Activity Dot Indicator
                                            if (hasUserEvents) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(5.dp)
                                                        .clip(CircleShape)
                                                        .background(Color(0xFF0288D1))
                                                )
                                            } else {
                                                Spacer(modifier = Modifier.height(2.dp))
                                            }
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }
        }

        // 6. BOTTOM "CATAT JADWAL" CARD (Sama seperti kotak biru bawah di screenshot)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .clickable { showAddEventDialog = true }
                    .testTag("catat_jadwal_box")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Big round blue icon like in screenshot
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0288D1)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Catat Jadwal",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (language == AppLanguage.HEBREW) "רישום משימה ותוכניות"
                            else if (language == AppLanguage.INDONESIAN) "Catat Jadwal"
                            else "Record Schedule / Plan",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0288D1)
                        )
                        Text(
                            text = if (language == AppLanguage.HEBREW) "לחץ כאן כדי להוסיף פעילות לתאריך זה"
                            else if (language == AppLanguage.INDONESIAN) "Silakan Ketuk di sini untuk tanggal terpilih"
                            else "Tap here to plan for selected date",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF78909C)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFE1F5FE)
                    ) {
                        Text(
                            text = selectedDate.format(DateTimeFormatter.ofPattern("d MMM")),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0288D1),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // 7. HOLIDAYS DETAIL FOR SELECTED DATE (Jika ada hari raya)
        if (holidaysOnDate.isNotEmpty()) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = StringResources.get("jewish_holidays", language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC62828)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    holidaysOnDate.forEach { instance ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .clickable {
                                    holidayToShowDetail = instance.holiday
                                    holidayInstanceDetail = instance
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Celebration,
                                    contentDescription = null,
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = instance.holiday.getName(language),
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFE65100)
                                    )
                                    Text(
                                        text = instance.holiday.nameHe,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF8D6E63)
                                    )
                                }
                                Text(
                                    text = StringResources.get("view_details", language),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF0288D1),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // 8. SCHEDULED ACTIVITIES FOR SELECTED DATE
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = StringResources.get("activities_title", language) + " (${eventsOnDate.size})",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF37474F)
                )
            }
        }

        if (eventsOnDate.isEmpty()) {
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = StringResources.get("no_activities", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF90A4AE)
                        )
                    }
                }
            }
        } else {
            items(eventsOnDate) { event ->
                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                    EventItemCard(
                        event = event,
                        language = language,
                        onToggleComplete = { viewModel.toggleEventCompleted(event) },
                        onDelete = { viewModel.deletePlannerEvent(event) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showAddEventDialog) {
        AddEditEventDialog(
            dateLabel = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + " (" + selectedHebrewDate.getFormattedHebrew(language) + ")",
            language = language,
            onDismiss = { showAddEventDialog = false },
            onSave = { title, desc, time, category, hasReminder ->
                viewModel.addPlannerEvent(title, desc, time, category, hasReminder)
                showAddEventDialog = false
            }
        )
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

@Composable
fun EventItemCard(
    event: PlannerEvent,
    language: AppLanguage,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = event.isCompleted,
                onCheckedChange = { onToggleComplete() },
                modifier = Modifier.testTag("event_checkbox_${event.id}")
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (event.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                    color = if (event.isCompleted) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.onSurface
                )
                if (event.time.isNotEmpty() || event.description.isNotEmpty()) {
                    Text(
                        text = buildString {
                            if (event.time.isNotEmpty()) append("${event.time} • ")
                            append(event.description.ifEmpty { event.category })
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (event.hasReminder) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Reminder Active",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(end = 4.dp)
                )
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

