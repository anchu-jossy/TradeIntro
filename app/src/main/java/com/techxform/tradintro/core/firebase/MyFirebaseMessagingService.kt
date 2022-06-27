package com.techxform.tradintro.core.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.viewModelScope
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.techxform.tradintro.R
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.fcmToken
import com.techxform.tradintro.core.utils.PreferenceHelper.token
import com.techxform.tradintro.feature_main.data.remote.FcmTokenRegReq
import com.techxform.tradintro.feature_main.data.remote.dto.Result
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val CHANNEL_ID = "channel_1"

    @Inject
    lateinit var repository: ApiRepository
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
      //  Log.e("onMessageReceived", "msg: ${message.notification}")

        createNotification(message)
    }

    private fun createNotification(message: RemoteMessage)
    {
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
        sendRegistrationToServer(token)
    }
//TODO: make it common
    private fun sendRegistrationToServer(token: String) {
        val device = (Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + VERSION_CODES::class.java.fields[Build.VERSION.SDK_INT].name)

        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Installations", "Installation ID: " + task.result)
                val pref = PreferenceHelper.customPreference(this)
                pref.fcmToken = task.result
                val req = FcmTokenRegReq(token, task.result, device)
                scope.launch() {
                    repository.fcmTokenRegistration(req)
                }
            } else {
                Log.e("Installations", "Unable to get Installation ID")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}