package com.techxform.tradintro.core

import android.content.Context
import android.provider.SyncStateContract
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.techxform.tradintro.BuildConfig
import com.techxform.tradintro.R
import dagger.hilt.android.HiltAndroidApp
import org.acra.ACRA
import org.acra.ReportField
import org.acra.config.MailSenderConfigurationBuilder
import org.acra.config.mailSender
import org.acra.config.mailSenderConfiguration
import org.acra.data.StringFormat
import org.acra.ktx.initAcra

@HiltAndroidApp
class TradIntroApp: MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        initAcra {
            mailSender {
                //required
                mailTo = "anchujossy@gmail.com"
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
                mailTo ="anchujossy@gmail.com"
               reportAsFile=true
            }


        }
    }
}
