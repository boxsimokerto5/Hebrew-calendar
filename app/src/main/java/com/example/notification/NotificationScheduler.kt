package com.example.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.calendar.HebrewCalendarEngine
import com.example.data.PlannerEvent
import com.example.localization.AppLanguage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

object NotificationScheduler {

    fun scheduleActivityReminder(context: Context, event: PlannerEvent) {
        if (!event.hasReminder) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Parse date and time
        val dateParts = event.gregorianDate.split("-").mapNotNull { it.toIntOrNull() }
        if (dateParts.size < 3) return
        val localDate = LocalDate.of(dateParts[0], dateParts[1], dateParts[2])

        val timeParts = if (event.time.contains(":")) {
            event.time.split(":").mapNotNull { it.toIntOrNull() }
        } else {
            listOf(9, 0) // Default 09:00 if no time set
        }

        val localTime = if (timeParts.size >= 2) {
            LocalTime.of(timeParts[0], timeParts[1])
        } else {
            LocalTime.of(9, 0)
        }

        var triggerDateTime = LocalDateTime.of(localDate, localTime)
        if (event.reminderTimeMinutes > 0) {
            triggerDateTime = triggerDateTime.minusMinutes(event.reminderTimeMinutes.toLong())
        }

        val triggerMillis = triggerDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        if (triggerMillis < System.currentTimeMillis()) return // Already in the past

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_TYPE, NotificationReceiver.TYPE_ACTIVITY)
            putExtra(NotificationReceiver.EXTRA_ID, (event.id % 100000).toInt())
            putExtra(NotificationReceiver.EXTRA_TITLE, event.title)
            putExtra(NotificationReceiver.EXTRA_MESSAGE, "${event.time.ifEmpty { "Today" }} - ${event.description.ifEmpty { event.hebrewDateString }}")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (100000 + event.id).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
            }
        } catch (_: SecurityException) {
            // Handled safely
        }
    }

    fun scheduleUpcomingHolidayReminders(context: Context, language: AppLanguage) {
        val today = LocalDate.now()
        val upcoming = HebrewCalendarEngine.getUpcomingHolidays(today, count = 10)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (instance in upcoming) {
            // Schedule reminder for 08:00 AM on the day of holiday
            val holidayDate = instance.gregorianDate
            val reminderTime = LocalDateTime.of(holidayDate, LocalTime.of(8, 0))
            val triggerMillis = reminderTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

            if (triggerMillis < System.currentTimeMillis()) continue

            val holiday = instance.holiday
            val notifId = (holiday.id.hashCode() and 0x7FFFFFFF) % 50000

            val intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(NotificationReceiver.EXTRA_TYPE, NotificationReceiver.TYPE_HOLIDAY)
                putExtra(NotificationReceiver.EXTRA_ID, notifId)
                putExtra(NotificationReceiver.EXTRA_TITLE, "${holiday.getName(language)} (${holiday.nameHe})")
                val greeting = holiday.getGreeting(language).ifEmpty { "Chag Sameach!" }
                putExtra(NotificationReceiver.EXTRA_MESSAGE, "$greeting - ${holiday.getCategoryName(language)}")
                putExtra(NotificationReceiver.EXTRA_DETAILS, holiday.getDescription(language))
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                notifId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerMillis, pendingIntent)
                }
            } catch (_: SecurityException) {
                // Ignore security exceptions if exact alarms are restricted
            }
        }
    }
}
