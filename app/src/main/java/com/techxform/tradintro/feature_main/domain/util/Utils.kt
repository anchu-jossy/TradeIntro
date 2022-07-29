package com.techxform.tradintro.feature_main.domain.util

import okhttp3.internal.trimSubstring

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
}