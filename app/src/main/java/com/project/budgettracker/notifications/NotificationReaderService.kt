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
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.project.budgettracker.MainActivity.Companion.CHANNEL_ID
import com.project.budgettracker.R


private const val TAG = "NotifReader"

class NotificationReaderService : NotificationListenerService() {

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d(TAG, "NotificationListenerService connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return

        // Ignore your own notifications (important!)
        if (sbn.packageName == packageName) {
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
        source: String?,
        notificationId: Int = DismissReceiver.DEFAULT_NOTIFICATION_ID
    ) {
        // 1) Build the Intent that opens MainActivity and signals navigation to AddExpense
        val intent = Intent(context, com.project.budgettracker.MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "add_expense")
            putExtra("expense_amount", amount)
        }

        val openPendingIntent = PendingIntent.getActivity(
            context,
            // unique request code for pending intent
            (notificationId xor 0x1000),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 2) Dismiss action
        val dismissIntent = Intent(context, DismissReceiver::class.java).apply {
            putExtra("notification_id", notificationId)
        }
        val dismissPendingIntent = PendingIntent.getBroadcast(
            context,
            (notificationId xor 0x2000),
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3) Build the notification text
        val title = "Budget tracker : Expense Detected"
        val content = "₹${amount} from ${source ?: "unknown"} — Add to tracker?"

        // 4) Permission guard for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val hasPerm = ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!hasPerm) {
                Log.w("NotifReader", "POST_NOTIFICATIONS missing; skipping notification.")
                return
            }
        }

        // 5) Build heads-up notification using NotificationCompat
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_stat_name) // replace with your app icon if available
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .setFullScreenIntent(openPendingIntent, true) // helps with heads-up
            .addAction(android.R.drawable.ic_input_add, "Add", openPendingIntent)
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Dismiss", dismissPendingIntent)

        // 6) Show the notification via NotificationManagerCompat
        try {
            NotificationManagerCompat.from(context).notify(notificationId, builder.build())
        } catch (se: SecurityException) {
            Log.w("NotifReader", "SecurityException posting notification: ${se.message}")
        }
    }


}
