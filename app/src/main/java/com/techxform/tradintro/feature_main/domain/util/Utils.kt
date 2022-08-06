package com.techxform.tradintro.feature_main.domain.util

import android.widget.Toast
import okhttp3.internal.trimSubstring
import java.math.RoundingMode
import java.text.DecimalFormat

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
    fun formatFloatIntoTwoDecimal(num :Float): Float {
     val df=   DecimalFormat("0.00")
        df.roundingMode = RoundingMode.DOWN;
        return df.format(num).toFloat()



    }
}