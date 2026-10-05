package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.PlannerEvent
import com.example.localization.AppLanguage
import com.example.localization.StringResources

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddEditEventDialog(
    initialEvent: PlannerEvent? = null,
    dateLabel: String,
    language: AppLanguage,
    onDismiss: () -> Unit,
    onSave: (title: String, desc: String, time: String, category: String, hasReminder: Boolean) -> Unit
) {
    var title by remember { mutableStateOf(initialEvent?.title ?: "") }
    var description by remember { mutableStateOf(initialEvent?.description ?: "") }
    var time by remember { mutableStateOf(initialEvent?.time ?: "") }
    var category by remember { mutableStateOf(initialEvent?.category ?: "PERSONAL") }
    var hasReminder by remember { mutableStateOf(initialEvent?.hasReminder ?: true) }
    var titleError by remember { mutableStateOf(false) }

    val categories = listOf(
        "HOLIDAY" to StringResources.get("cat_holiday", language),
        "RELIGIOUS" to StringResources.get("cat_religious", language),
        "FAMILY" to StringResources.get("cat_family", language),
        "WORK" to StringResources.get("cat_work", language),
        "PERSONAL" to StringResources.get("cat_personal", language)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = if (initialEvent == null) StringResources.get("add_activity", language) else StringResources.get("edit_activity", language),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = dateLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) titleError = false
                    },
                    label = { Text(StringResources.get("activity_title_hint", language)) },
                    leadingIcon = { Icon(Icons.Default.Title, contentDescription = null) },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Title cannot be empty") }
                    } else null,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_title_input")
                )

                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text(StringResources.get("time_hint", language)) },
                    leadingIcon = { Icon(Icons.Default.AccessTime, contentDescription = null) },
                    placeholder = { Text("09:00, 18:30") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_time_input")
                )

                Text(
                    text = StringResources.get("category", language),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    categories.forEach { (catKey, catLabel) ->
                        FilterChip(
                            selected = category == catKey,
                            onClick = { category = catKey },
                            label = { Text(catLabel) }
                        )
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(StringResources.get("activity_desc_hint", language)) },
                    leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_desc_input")
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = StringResources.get("enable_reminder", language),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Switch(
                        checked = hasReminder,
                        onCheckedChange = { hasReminder = it },
                        modifier = Modifier.testTag("reminder_switch")
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                    } else {
                        onSave(title, description, time, category, hasReminder)
                    }
                },
                modifier = Modifier.testTag("save_event_button")
            ) {
                Text(StringResources.get("save", language))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_event_button")
            ) {
                Text(StringResources.get("cancel", language))
            }
        }
    )
}
