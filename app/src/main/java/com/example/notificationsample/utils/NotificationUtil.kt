package com.example.notificationsample.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsample.R
import com.example.notificationsample.data.model.Channel
import com.example.notificationsample.data.model.Notificator
import com.example.notificationsample.ui.BigTextActivity
import com.example.notificationsample.ui.MainActivity

object NotificationUtil {

    fun startPushNotification(context: Context, notificator: Notificator, channel: Channel) {

        createNotificationChannel(context, channel)

        val intent = Intent(context, BigTextActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = NotificationCompat.Builder(context, MainActivity.CHANNEL_PUSH_ID)
            .setSmallIcon(R.drawable.ic_alarm_white_48dp)
            .setContentTitle(notificator.contentTitle)
            .setContentText(notificator.contentText)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        /*Show notification*/
        with(NotificationManagerCompat.from(context)) {
            notify(notificator.notificationId, builder.build())
        }
    }

    private fun createNotificationChannel(context: Context, chan: Channel) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            /*Initializes NotificationChannel*/
            val channel = NotificationChannel(
                chan.channelId,
                chan.channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = chan.desc
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            /*Register the channel with the system*/
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
