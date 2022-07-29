package com.techxform.tradintro.core

import android.content.Context
import android.util.Log
import com.google.auto.service.AutoService
import org.acra.config.CoreConfiguration
import org.acra.data.CrashReportData
import org.acra.sender.ReportSender
import org.acra.sender.ReportSenderFactory

class MySender() : ReportSender {

    override fun send(context: Context, errorContent: CrashReportData) {
        Log.d("CRASH_", "Report Sent${errorContent.toJSON()}")
    }

}

@AutoService(ReportSenderFactory::class)
class MySenderFactory : ReportSenderFactory {
    override fun create(context: Context, config: CoreConfiguration): ReportSender {
        return MySender()
    }
}