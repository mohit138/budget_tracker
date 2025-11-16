package com.project.budgettracker.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat

class DismissReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        // Notification id used when showing notifications (use same id)
        val notifId = intent?.getIntExtra("notification_id", DEFAULT_NOTIFICATION_ID) ?: DEFAULT_NOTIFICATION_ID
        NotificationManagerCompat.from(context).cancel(notifId)
    }

    companion object {
        const val DEFAULT_NOTIFICATION_ID = 9999
    }
}
