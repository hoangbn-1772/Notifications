package com.example.notificationsample.utils

import android.annotation.SuppressLint
import androidx.core.app.NotificationCompat

object GlobalNotificationBuilder {

    @SuppressLint("StaticFieldLeak")
    private var sGlobalNotificationCompatBuilder : NotificationCompat.Builder? = null

    fun setNotificationCompatBuilderInstance(builder : NotificationCompat.Builder){
        sGlobalNotificationCompatBuilder = builder
    }

    fun getNotificationCompatBuilderInstance() : NotificationCompat.Builder? {
        return sGlobalNotificationCompatBuilder
    }
}
