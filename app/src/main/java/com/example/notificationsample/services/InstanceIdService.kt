package com.example.notificationsample.services

import android.util.Log
import com.example.notificationsample.data.model.Channel
import com.example.notificationsample.data.model.Notificator
import com.example.notificationsample.ui.MainActivity
import com.example.notificationsample.utils.NotificationUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class InstanceIdService : FirebaseMessagingService() {

    /*get token*/
    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.d("TAG", "Token; $token")
    }

    /*Receive the message*/
    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)
        val notificator =
            message?.notification?.body?.let { Notificator("FCM", it, MainActivity.NOTIFICATION_PUSH_ID) }
        val pushChannel =
            Channel(MainActivity.CHANNEL_PUSH_ID, MainActivity.CHANNEL_PUSH_NAME, MainActivity.CHANNEL_PUSH_NAME)
        notificator?.let { NotificationUtil.startPushNotification(this, it, pushChannel) }
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }
}
