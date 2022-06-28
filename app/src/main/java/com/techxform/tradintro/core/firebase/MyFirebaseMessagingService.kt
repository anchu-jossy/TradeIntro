package com.techxform.tradintro.core.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
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
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID = "channel_1"



    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.e("onMessageReceived", "msg: ${message.notification?.title}")

        createNotification(message)
    }

    private fun createNotification(message: RemoteMessage)
    {
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            //val name = getString(R.string.channel_name)
            val descriptionText = "channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance).apply {
                description = descriptionText
            }

            notificationManager.createNotificationChannel(channel)

        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

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