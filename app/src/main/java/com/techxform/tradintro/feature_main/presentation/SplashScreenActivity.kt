package com.techxform.tradintro.feature_main.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.techxform.tradintro.R
import com.techxform.tradintro.feature_account.presentation.signin.LoginFragment
import com.techxform.tradintro.feature_main.domain.util.Utils.showShortToast
import com.techxform.tradintro.feature_main.presentation.landing.LandingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_REQUEST_CODE = 100
        const val IS_NOTIFICATION = "isNotification"
        const val NOTIFICATION_TYPE = "notification_type"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)
        if (Build.VERSION.SDK_INT >= 33) {
            askNotificationPermission()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val fragment = getFragment()
        val type = intent?.getStringExtra(NOTIFICATION_TYPE) ?: ""

        if (fragment is LoginFragment) {
            fragment.isNotification(true, type)

        } else if (fragment is LandingFragment) {
            fragment.redirectToNotification(type)
        }
    }

    private fun getFragment(): Fragment? {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        return navHost?.childFragmentManager?.fragments?.get(0)
    }

    @RequiresApi(33)
    private fun askNotificationPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED -> {
                // FCM SDK (and your app) can post notifications.
            }
            shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {

            }
            else -> {
                // Directly ask for the permission
                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    NOTIFICATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            NOTIFICATION_REQUEST_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    this.showShortToast(getString(R.string.notification_permission_denied_msg))
                }
                return
            }
        }
    }
}