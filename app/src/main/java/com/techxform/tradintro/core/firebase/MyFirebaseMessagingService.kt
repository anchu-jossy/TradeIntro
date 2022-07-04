package com.techxform.tradintro.core.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techxform.tradintro.R
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.fcmToken
import com.techxform.tradintro.core.utils.PreferenceHelper.isFcmTokenSync
import com.techxform.tradintro.feature_main.presentation.SplashScreenActivity
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "trade_intro_channel"
        const val TYPE = "key_1"
        const val TITLE = "title"
        const val BODY = "body"
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("onMessageReceived", "msg: ${message.data}")

        createNotification(message)
    }

    private fun createNotification(message: RemoteMessage) {
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance).apply {
                description = getString(R.string.channel_description)
            }

            notificationManager.createNotificationChannel(channel)

        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(message.data[TITLE])
            .setContentText(message.data[BODY])
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationIntent = Intent(this, SplashScreenActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        notificationIntent.putExtra(SplashScreenActivity.IS_NOTIFICATION, true)
        notificationIntent.putExtra(SplashScreenActivity.NOTIFICATION_TYPE, message.data[TYPE])

        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.setContentIntent(pendingIntent)

        NotificationManagerCompat.from(this).apply {
            notificationManager.notify(Random().nextInt(), builder.build())
        }
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.e("Token", "MyToken : $token")
        val pref = PreferenceHelper.customPreference(this)
        pref.fcmToken = token
        pref.isFcmTokenSync = false
    }

}