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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.PlannerEvent
import com.example.localization.AppLanguage
import com.example.localization.StringResources
import com.example.ui.MainViewModel
import com.example.ui.components.AddEditEventDialog
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PlannerScreen(
    viewModel: MainViewModel,
    onNavigateToCalendar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val selectedHebrewDate by viewModel.selectedHebrewDate.collectAsStateWithLifecycle()
    val eventsOnDate by viewModel.eventsOnSelectedDate.collectAsStateWithLifecycle()
    val allUpcomingEvents by viewModel.allUpcomingEvents.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    var eventToEdit by remember { mutableStateOf<PlannerEvent?>(null) }

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
                                    Icons.Default.EventNote,
                                    contentDescription = null,
                                    tint = Color(0xFF0288D1),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = StringResources.get("tab_planner", language),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = if (language == AppLanguage.HEBREW) "משימות, אירועים ולוח זמנים"
                                    else if (language == AppLanguage.INDONESIAN) "Jadwal, Kegiatan & Pengingat"
                                    else "Schedule, Activities & Reminders",
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

                    // Selected Date Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = selectedDate.format(
                                    DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", when (language) {
                                        AppLanguage.INDONESIAN -> Locale("id")
                                        AppLanguage.HEBREW -> Locale("he")
                                        AppLanguage.ENGLISH -> Locale.ENGLISH
                                    })
                                ),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = selectedHebrewDate.getFormattedHebrew(language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFFFFEB3B),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        IconButton(
                            onClick = onNavigateToCalendar,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                        ) {
                            Icon(Icons.Default.CalendarMonth, contentDescription = "Open Calendar", tint = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Add Activity Button
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        modifier = Modifier
                            .clickable { showAddDialog = true }
                            .testTag("add_event_main_button")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = Color(0xFF0288D1),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = StringResources.get("add_activity", language),
                                style = MaterialTheme.typography.labelLarge,
                                color = Color(0xFF0288D1),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Section Title: Kegiatan untuk Tanggal Terpilih
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = StringResources.get("activities_title", language),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF263238)
                )

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFFE1F5FE)
                ) {
                    Text(
                        text = "${eventsOnDate.size} kegiatan",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF0288D1),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
            }
        }

        if (eventsOnDate.isEmpty()) {
            item {
                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = StringResources.get("no_activities", language),
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF90A4AE)
                            )
                        }
                    }
                }
            }
        } else {
            items(eventsOnDate) { event ->
                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                    PlannerEventCardBright(
                        event = event,
                        language = language,
                        onToggleComplete = { viewModel.toggleEventCompleted(event) },
                        onEdit = { eventToEdit = event },
                        onDelete = { viewModel.deletePlannerEvent(event) }
                    )
                }
            }
        }

        // Section: Semua Agenda Rencana Mendatang
        if (allUpcomingEvents.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = StringResources.get("all_plans", language),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0288D1)
                    )
                }
            }

            items(allUpcomingEvents.filter { it.gregorianDate != selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE) }) { event ->
                Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                    PlannerEventCardBright(
                        event = event,
                        language = language,
                        showDate = true,
                        onToggleComplete = { viewModel.toggleEventCompleted(event) },
                        onEdit = { eventToEdit = event },
                        onDelete = { viewModel.deletePlannerEvent(event) }
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }

    if (showAddDialog) {
        AddEditEventDialog(
            dateLabel = selectedDate.format(DateTimeFormatter.ISO_LOCAL_DATE) + " (" + selectedHebrewDate.getFormattedHebrew(language) + ")",
            language = language,
            onDismiss = { showAddDialog = false },
            onSave = { title, desc, time, category, hasReminder ->
                viewModel.addPlannerEvent(title, desc, time, category, hasReminder)
                showAddDialog = false
            }
        )
    }

    eventToEdit?.let { event ->
        AddEditEventDialog(
            initialEvent = event,
            dateLabel = event.gregorianDate + " (" + event.hebrewDateString + ")",
            language = language,
            onDismiss = { eventToEdit = null },
            onSave = { title, desc, time, category, hasReminder ->
                viewModel.updatePlannerEvent(
                    event.copy(
                        title = title,
                        description = desc,
                        time = time,
                        category = category,
                        hasReminder = hasReminder
                    )
                )
                eventToEdit = null
            }
        )
    }
}

@Composable
fun PlannerEventCardBright(
    event: PlannerEvent,
    language: AppLanguage,
    showDate: Boolean = false,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isCompleted) Color(0xFFECEFF1) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            if (showDate) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.gregorianDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF0288D1),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = event.hebrewDateString,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFF57C00),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = event.isCompleted,
                    onCheckedChange = { onToggleComplete() }
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (event.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (event.isCompleted) Color(0xFF90A4AE) else Color(0xFF263238)
                    )

                    if (event.time.isNotEmpty()) {
                        Text(
                            text = event.time,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF0288D1),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (event.description.isNotEmpty()) {
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF546E7A)
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (event.hasReminder) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Reminder",
                            tint = Color(0xFFF57C00),
                            modifier = Modifier
                                .size(18.dp)
                                .padding(end = 4.dp)
                        )
                    }
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color(0xFF0288D1), modifier = Modifier.size(18.dp))
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color(0xFFE53935), modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}
