package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planner_events")
data class PlannerEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val gregorianDate: String, // Format: yyyy-MM-dd
    val hebrewDateString: String,
    val title: String,
    val description: String = "",
    val time: String = "", // e.g. "18:30" or ""
    val category: String = "PERSONAL", // HOLIDAY, RELIGIOUS, FAMILY, WORK, PERSONAL
    val isCompleted: Boolean = false,
    val hasReminder: Boolean = false,
    val reminderTimeMinutes: Int = 15,
    val createdAt: Long = System.currentTimeMillis()
)
