package com.techxform.tradintro.feature_main.domain.util

import android.view.View
import okhttp3.internal.trimSubstring
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun formatStringToTwoDecimals(num: String): String {
        var second = "0"
        val splittedNum = num.split(".")
        val first = splittedNum[0]
        return if (splittedNum[1].length > 2) {
            second = splittedNum[1].trimSubstring(0, 2)
            "$first.$second"
        } else {
            num
        }
    }
    fun formatPercentageWithoutDecimals(num :String): String {
       return num.split(".")[0]
    }
    fun formatBigDecimalIntoTwoDecimal(num :BigDecimal): BigDecimal {
     val df=   DecimalFormat("0.00")
        df.roundingMode = RoundingMode.DOWN;
        return df.format(num).toBigDecimal()

    }




    fun View.setVisible(){
        this.visibility=View.VISIBLE
    }
    fun View.setVisibiltyGone(){
        this.visibility=View.GONE
    }

    fun formatDateTime(formattedDate:String?): Pair<String, String> {
        if(formattedDate.isNullOrEmpty())
            return Pair("","")
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(formattedDate)
        val date = SimpleDateFormat("yyyy-MM-dd").format(d)
        val time = SimpleDateFormat("hh:mm aa").format(d)

        return Pair(date, time)
    }

    fun formatDateTimeString(formattedDate:String?): String {
        if(formattedDate.isNullOrEmpty())
            return ""
        val pair = formatDateTime(formattedDate)
        return pair.first + " " + pair.second
    }
    fun formatCurrentDate(): String {
        return  SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(Date());
    }
}