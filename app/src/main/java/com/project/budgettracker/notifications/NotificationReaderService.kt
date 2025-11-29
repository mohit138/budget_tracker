package com.project.budgettracker.notifications

import android.Manifest
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.project.budgettracker.MainActivity.Companion.CHANNEL_ID
import com.project.budgettracker.MainActivity.Companion.GROUP_KEY_EXPENSE
import com.project.budgettracker.MainActivity.Companion.SUMMARY_ID
import com.project.budgettracker.R


private const val TAG = "NotifReader"

class NotificationReaderService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "NotificationListenerService connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return
        val defaultSmsApp = Telephony.Sms.getDefaultSmsPackage(applicationContext)

        // Ignore non message related notifications (important!)
        if (!sbn.packageName.equals(defaultSmsApp)) {
            return
        }

        val packageName = sbn.packageName ?: "unknown_pkg"
        val tag = sbn.tag
        val notification = sbn.notification
        val extras: Bundle? = notification.extras
        val title = extras?.getCharSequence(Notification.EXTRA_TITLE)?.toString()
        val text = extras?.getCharSequence(Notification.EXTRA_TEXT)?.toString()
            ?: extras?.getCharSequence(Notification.EXTRA_BIG_TEXT)?.toString()
            ?: ""
        val parsedAmount = AmountParser.extractAmount(text)
        if (parsedAmount != null) {
            showExpenseDetectedNotification(this, parsedAmount, /*source*/ title ?: sbn.packageName)
        }

        Log.d(TAG, "NOTIF POSTED from=$packageName tag=$tag title=$title text=$text parsedAmount=$parsedAmount")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        if (sbn == null) return
        Log.d(TAG, "NOTIF REMOVED from=${sbn.packageName}")
    }

    private fun showExpenseDetectedNotification(
        context: Context,
        amount: Double,
        source: String?
    ) {
        // Permission guard for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.w("NotifReader", "POST_NOTIFICATIONS missing; skipping notification.")
            return
        }

        val notificationId = System.currentTimeMillis().toInt()
        val title = "Expense Detected"
        val content = "₹$amount from ${source ?: "unknown"} — Add to tracker?"

        val mainIntent = Intent(context, com.project.budgettracker.MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "add_expense")
            putExtra("expense_amount", amount)
        }

        val openPendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val dismissIntent = Intent(context, DismissReceiver::class.java).apply {
            putExtra("notification_id", notificationId)
        }

        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId + 1,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val childBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY_EXPENSE)
            .setContentIntent(openPendingIntent)
            .addAction(android.R.drawable.ic_input_add, "Add", openPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissPendingIntent)

        NotificationManagerCompat.from(context).notify(notificationId, childBuilder.build())
        // summary Notification
        postSummaryNotification(context)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun postSummaryNotification(context: Context) {
        val summary = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name)
            .setContentTitle("Detected Expenses")
            .setContentText("Review detected expenses")
            .setGroup(GROUP_KEY_EXPENSE)
            .setGroupSummary(true)
            .setOngoing(true)                      // <<< keeps summary alive like WhatsApp
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()

        NotificationManagerCompat.from(context).notify(SUMMARY_ID, summary)
    }
}
