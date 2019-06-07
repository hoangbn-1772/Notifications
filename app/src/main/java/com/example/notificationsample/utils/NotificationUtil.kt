package com.example.notificationsample.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.example.notificationsample.data.db.MockDatabase

object NotificationUtil {
    fun createNotificationChannel(
        context: Context,
        mockNotificationData: MockDatabase.MockNotificationData
    ): String? {
        // NotificationChannels are required for Notifications on O (API 26) and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = mockNotificationData.mChannelId
            val channelName = mockNotificationData.mChannelName
            val channelDescription = mockNotificationData.mChannelDescription
            val channelImportance = mockNotificationData.mChannelImportance
            val channelEnableVibrate = mockNotificationData.mChannelEnableVibrate
            val channelLockScreenVisibility = mockNotificationData.mChannelLockScreenVisibility

            // Initializes NotificationChannel
            val notificationChannel = NotificationChannel(channelId, channelName, channelImportance)
            notificationChannel.description = channelDescription
            notificationChannel.enableVibration(channelEnableVibrate)
            notificationChannel.lockscreenVisibility = channelLockScreenVisibility

            // Adds NotificationChannel to System
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)

            return channelId
        }
        return null
    }
}
