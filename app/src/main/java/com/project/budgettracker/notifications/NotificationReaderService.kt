package com.project.budgettracker.notifications

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.app.Notification
import android.os.Bundle

private const val TAG = "NotifReader"

class NotificationReaderService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "NotificationListenerService connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return

        val packageName = sbn.packageName ?: "unknown_pkg"
        val tag = sbn.tag
        val notification = sbn.notification
        val extras: Bundle? = notification.extras
        val title = extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        val text = extras?.getCharSequence(Notification.EXTRA_TEXT)?.toString()
            ?: extras?.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
            ?: ""
        val parsedAmount = AmountParser.extractAmount(text)

        Log.d(TAG, "NOTIF POSTED from=$packageName tag=$tag title=$title text=$text parsedAmount=$parsedAmount")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn == null) return
        Log.d(TAG, "NOTIF REMOVED from=${sbn.packageName}")
    }
}
