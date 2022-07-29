package com.techxform.tradintro.core

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.techxform.tradintro.R
import dagger.hilt.android.HiltAndroidApp
import org.acra.config.mailSender
import org.acra.config.mailSenderConfiguration
import org.acra.ktx.initAcra

@HiltAndroidApp
class TradIntroApp: MultiDexApplication() {
val  emailID ="georgepj1991@gmail.com"
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        initAcra {
            mailSender {
                //required
                mailTo = emailID
                //defaults to true
                reportAsFile = true
                //defaults to ACRA-report.stacktrace
                reportFileName = "TradeAppCrash.txt"
                //defaults to "<applicationId> Crash Report"
                subject = getString(R.string.mail_subject)
                //defaults to empty
                body = getString(R.string.mail_body)
                enabled=true
            }
            mailSenderConfiguration {
                enabled=true
                mailTo =emailID
               reportAsFile=true
            }


        }
    }
}
