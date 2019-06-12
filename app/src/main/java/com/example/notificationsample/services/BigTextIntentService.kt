package com.example.notificationsample.services

import android.app.IntentService
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsample.ui.MainActivity
import com.example.notificationsample.utils.GlobalNotificationBuilder
import java.util.concurrent.TimeUnit

class BigTextIntentService : IntentService("BigTextIntentService") {

    override fun onHandleIntent(intent: Intent?) {
        intent?.run {
            val action = this.action
            when {
                ACTION_DISMISS == action -> handleActionDismiss()
                ACTION_SNOOZE == action -> handleActionSnooze()
            }
        }
    }

    private fun handleActionDismiss() {
        val notificationManagerCompat = NotificationManagerCompat.from(applicationContext)
        notificationManagerCompat.cancel(MainActivity.NOTIFICATION_ID)
    }

    private fun handleActionSnooze() {
        val notificationCompatBuilder = GlobalNotificationBuilder.getNotificationCompatBuilderInstance()

        if (notificationCompatBuilder == null) {
//            notificationCompatBuilder =
        }
    }

    /**
     * Recreate builder from persistent state if app process is killed
     */
//    private fun recreateBuilderWithBigTextStyle(): NotificationCompat.Builder {
//        val bigTextData = MockDatabase.getBigTextStyleData()
//        val bigTextStyle = NotificationCompat.BigTextStyle()
//            .bigText(bigTextData.getBigText())
//            .setBigContentTitle(bigTextData.getBigContentTitle())
//            .setSummaryText(bigTextData.getSumaryText())
//
//        val notifyIntent = Intent(this, BigTextActivity::class.java)
//        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//
//        val notifyPendingIntent = PendingIntent.getActivity(
//            this,
//            0,
//            notifyIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        // Action Snooze
//        val snoozeIntent = Intent(this, BigTextIntentService::class.java)
//        snoozeIntent.action = ACTION_SNOOZE
//        val snoozePendingIntent = PendingIntent.getService(this, 0, snoozeIntent, 0)
//        val snoozeAction = NotificationCompat.Action.Builder(
//            R.drawable.ic_alarm_white_48dp,
//            "Snooze",
//            snoozePendingIntent
//        ).build()
//
//        // Action Dismiss
//        val dismissIntent = Intent(this, BigTextIntentService::class.java)
//        dismissIntent.action = ACTION_DISMISS
//        val dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, 0)
//        val dismissAction = NotificationCompat.Action.Builder(
//            R.drawable.ic_cancel_white_48dp,
//            "Dismiss",
//            dismissPendingIntent
//        ).build()
//
//        // Build and issue the notification
//        val notificationCompatBuilder = NotificationCompat.Builder(applicationContext,"")
//    }

    companion object {
        private const val ACTION_DISMISS = "com.example.notificationsample.services.action.DISMISS"
        private const val ACTION_SNOOZE = "com.example.notificationsample.services.action.SNOOZE"
        private val SNOOZE_TIME = TimeUnit.SECONDS.toMillis(5)
    }
}
