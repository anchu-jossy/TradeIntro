package com.techxform.tradintro.feature_main.presentation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.techxform.tradintro.R
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var timerTask: TimerTask? = null;
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)
        startTimer()
    }

    private fun startMainActivity(){
        if(!isFinishing) {
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun startTimer() {
        cancelTimer()
        timer = Timer()
        timerTask = object : TimerTask() {
            override fun run() {
                startMainActivity()
            }
        }
        timer?.schedule(timerTask, 2000);
    }

    private fun cancelTimer() {
        timer?.cancel()
        timer?.purge()
        timerTask?.cancel()
    }

    override fun onStop() {
        super.onStop()
        cancelTimer()
    }

}