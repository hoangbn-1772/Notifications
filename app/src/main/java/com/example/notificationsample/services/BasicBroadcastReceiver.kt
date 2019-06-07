package com.example.notificationsample.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.notificationsample.ui.MainActivity

class BasicBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            MainActivity.ACTION_SNOOZE -> Log.d("TAG", intent.action.toString())
        }
    }
}
