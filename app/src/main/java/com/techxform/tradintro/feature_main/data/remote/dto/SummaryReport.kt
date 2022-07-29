package com.techxform.tradintro.feature_main.data.remote.dto

import androidx.annotation.ColorInt
import com.google.gson.annotations.SerializedName

data class SummaryReport(
    @SerializedName("realizedProfitLossValue"             ) var realizedProfitLossValue             : Float? = null,
    @SerializedName("realizedProfitLossValuePercentage"   ) var realizedProfitLossValuePercentage   : Float? = null,
    @SerializedName("unRealizedProfitLossValue"           ) var unRealizedProfitLossValue           : Float? = null,
    @SerializedName("unRealizedProfitLossValuePercentage" ) var unRealizedProfitLossValuePercentage : Float? = null,
    @SerializedName("totalInvestmentValue"                ) var totalInvestmentValue                : Float? = null
)


data class TotalPrices(@ColorInt val colorInt: Int, val amount: Float, val subtitle:String, val title:String){}