package com.example.notificationsample.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.notificationsample.R
import com.example.notificationsample.data.db.MockDatabase
import com.example.notificationsample.services.BasicBroadcastReceiver
import com.example.notificationsample.utils.NotificationUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener, View.OnClickListener {

    private val mNotificationManagerCompat by lazy { NotificationManagerCompat.from(applicationContext) }
    private var mSelectedNotification = 0
    private val basicBroadcastReceiver = BasicBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initComponents()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonSubmit -> createProgressBarInNotification()
            R.id.buttonNotificationBasic -> startNotificationBasic()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        // Do nothing
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        mSelectedNotification = position

        textViewNotificationDetails?.text = NOTIFICATION_STYLES_DESCRIPTION[mSelectedNotification]
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(basicBroadcastReceiver)
        super.onDestroy()
    }

    private fun initComponents() {

        buttonNotificationBasic.setOnClickListener(this)
        buttonSubmit.setOnClickListener(this)

        /**
         * Create spinner
         */
        val adapter: ArrayAdapter<CharSequence> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            NOTIFICATION_STYLES
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerNotificationStyle?.run {
            setAdapter(adapter)
            onItemSelectedListener = this@MainActivity
        }

        /* Context-register receivers*/
        val filter = IntentFilter().apply {
            addAction(ACTION_SNOOZE)
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(basicBroadcastReceiver, filter)
    }

    /**
     * Notification basic
     */
    private fun startNotificationBasic() {
        createNotificationChannel()

        /* Set the notification's tap action */
        val intent = Intent(this, BigTextActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        /**
         * Add action buttons:except instead of launching an activity, can start service/broadcast...
         */
        val snoozeIntent = Intent(this, BasicBroadcastReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(CHANNEL_ID, 100)
        }
        val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        /**
         * CHANNEL_ID: required begin Android 8.0 and higher
         * setPriority: Android 7.1 and lower
         */
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm_white_48dp)
            .setContentTitle("Notification")
            .setContentText("content notification basic")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_cancel_white_48dp, getString(R.string.action_snooze), snoozePendingIntent)
            .setAutoCancel(true)

        /*Show notification*/
        with(NotificationManagerCompat.from(this)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    /**
     * Add a direct reply action
     * Begin Android 7.0
     */
    private fun createReplyDirect() {
        var replyLabel = resources.getString(R.string.reply_label)
        var remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
            setLabel(replyLabel)
            build()
        }
    }

    /**
     * Add a progress bar
     */
    private fun createProgressBarInNotification() {
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(R.drawable.ic_cancel_white_48dp)
            setPriority(NotificationCompat.PRIORITY_LOW)
        }
        val PROGRESS_MAX = 100
        val PREGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            // Issue the initial notification with zero progress
            builder.setProgress(PROGRESS_MAX, PREGRESS_CURRENT, false)
            notify(NOTIFICATION_ID, builder.build())

            /*When done, update the notification one more time to remove the progress bar*/
            builder.setContentText("Download complete").setProgress(0, 0, false)
            notify(NOTIFICATION_ID, builder.build())
        }
    }

    /**
     * Create a channel and set the importance
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = CHANNEL_ID
            val channelName = getString(R.string.channel_name)
            val channelDescription = getString(R.string.channel_description_basic)
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            // Initializes NotificationChannel
            val channel = NotificationChannel(channelId, channelName, channelImportance).apply {
                description = channelDescription
                lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun startNotification() {
        val areNotificationEnabled = mNotificationManagerCompat.areNotificationsEnabled()

        /**
         * re-enable notifications for thí application again if it disable
         */
        if (!areNotificationEnabled) {
            /**
             * Show notifications to users
             */
            Snackbar
                .make(
                    mainRelativeLayout,
                    "You need to enable notifications for this app",
                    Snackbar.LENGTH_LONG
                )
                .setAction("ENABLE") {
                    openNotificationSettingsForApp()
                }
                .show()
            return
        }

        when (NOTIFICATION_STYLES[mSelectedNotification]) {
            BIG_TEXT_STYLE -> generateBigTextStyleNotification()
        }

    }

    /**
     * Links to this app's notification settings
     */
    private fun openNotificationSettingsForApp() {
        Intent().apply {
            action = "android.settings.APP_NOTIFICATION_SETTINGS"
            putExtra("app_package", packageName)
            putExtra("app_uid", applicationInfo.uid)
            startActivity(this)
        }
    }

    private fun generateBigTextStyleNotification() {
        /*Get data*/
        val bigTextStyleReminderAppData = MockDatabase.getBigTextStyleData()

        /* Create Notification Channel for O and beyond devices (26+) */
        val notificationChannelId = NotificationUtil.createNotificationChannel(this, bigTextStyleReminderAppData)

        /*Build the BIG_TEXT_STYLE*/
        val bigTextStyle = NotificationCompat.BigTextStyle()
            .bigText(bigTextStyleReminderAppData.getBigText())
            .setBigContentTitle(bigTextStyleReminderAppData.getBigContentTitle())
            .setSummaryText(bigTextStyleReminderAppData.getSumaryText())

        /*Set up main Intent for notification*/
        val notifyIntent = Intent(this, BigTextActivity::class.java)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val notifyPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        /* Create additional Actions for Notification*/
//        val snoozeIntent = Intent(this, Big)
    }

    companion object {
        val NOTIFICATION_ID = 888

        val BIG_TEXT_STYLE = "BIG_TEXT_STYLE"
        val BIG_PICTURE_STYLE = "BIG_PICTURE_STYLE"
        val INBOX_STYLE = "INBOX_STYLE"
        val MESSAGING_STYLE = "MESSAGING_STYLE"

        val NOTIFICATION_STYLES = arrayOf(
            BIG_TEXT_STYLE,
            BIG_PICTURE_STYLE,
            INBOX_STYLE,
            MESSAGING_STYLE
        )

        val NOTIFICATION_STYLES_DESCRIPTION = arrayOf(
            "Demos reminder type app using BIG_TEXT_STYLE",
            "Demos social type app using BIG_PICTURE_STYLE + inline notification response",
            "Demos email type app using INBOX_STYLE",
            "Demos messaging app using MESSAGING_STYLE + inline notification responses"
        )

        const val CHANNEL_ID = "notification_basic"
        const val ACTION_SNOOZE = "com.example.notificationsample.ui.SNOOZE"
        const val KEY_TEXT_REPLY = "key_text_reply"
    }
}
