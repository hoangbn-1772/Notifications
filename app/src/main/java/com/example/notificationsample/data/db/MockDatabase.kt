package com.example.notificationsample.data.db

import android.annotation.TargetApi
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat

object MockDatabase {

    fun getBigTextStyleData(): BigTextStyleReminderAppData {
        return MockDatabase.BigTextStyleReminderAppData.getInstance()
    }


    @TargetApi(Build.VERSION_CODES.N)
    class BigTextStyleReminderAppData : MockNotificationData() {

        private var mBigContentTitle = ""
        private var mBigText = ""
        private var mSummaryText = ""

        init {
            // Title for API < 16
            mContentTitle = "Don't forget to..."
            //Content for API < 24
            mContentText = "Feed Dogs and check garage!"
            mPriority = NotificationCompat.PRIORITY_DEFAULT

            /*BigText Style Notification values*/
            mBigContentTitle = "Don't forget to..."
            mBigText = "... feed the dogs before you leave for work, and check the garage to..."
            mSummaryText = "Dogs and Garage"

            /*Notification channel values (target API 26 and above)*/
            mChannelId = "channel_reminder_1"
            mChannelName = "Sample Reminder"
            mChannelDescription = "Sample Reminder Notifications"
            mChannelImportance = NotificationManager.IMPORTANCE_DEFAULT
            mChannelEnableVibrate = false
            mChannelLockScreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
        }

        fun getBigContentTitle(): String {
            return mBigContentTitle
        }

        fun getBigText(): String {
            return mBigText
        }

        fun getSumaryText(): String {
            return mSummaryText
        }

        companion object {
            private var instance: BigTextStyleReminderAppData? = null

            /*SingleTon*/
            fun getInstance(): BigTextStyleReminderAppData = instance ?: synchronized(this) {
                instance ?: BigTextStyleReminderAppData()
            }
        }
    }

    /**
     * Represents standard data needed for a Notification.
     */
    open class MockNotificationData {

        // Standard notification values
        var mContentTitle: String = ""
        var mContentText: String = ""
        var mPriority: Int = -1

        // Notification channel values (0 and above)
        var mChannelId: String = ""
        var mChannelName: CharSequence = ""
        var mChannelDescription: String = ""
        var mChannelImportance: Int = -1
        var mChannelEnableVibrate: Boolean = false
        var mChannelLockScreenVisibility: Int = -1
    }
}
