package com.example.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_TYPE = "extra_notification_type"
        const val EXTRA_ID = "extra_notification_id"
        const val EXTRA_TITLE = "extra_notification_title"
        const val EXTRA_MESSAGE = "extra_notification_message"
        const val EXTRA_DETAILS = "extra_notification_details"

        const val TYPE_HOLIDAY = "TYPE_HOLIDAY"
        const val TYPE_ACTIVITY = "TYPE_ACTIVITY"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE) ?: TYPE_HOLIDAY
        val notifId = intent.getIntExtra(EXTRA_ID, (System.currentTimeMillis() % 100000).toInt())
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Reminder"
        val message = intent.getStringExtra(EXTRA_MESSAGE) ?: "You have an upcoming event."
        val details = intent.getStringExtra(EXTRA_DETAILS) ?: ""

        if (type == TYPE_HOLIDAY) {
            NotificationHelper.showHolidayNotification(context, notifId, title, message, details)
        } else {
            NotificationHelper.showActivityNotification(context, notifId, title, message)
        }
    }
}
