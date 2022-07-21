package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SummaryReport(
    @SerializedName("realizedProfitLossValue"             ) var realizedProfitLossValue             : Number? = null,
    @SerializedName("realizedProfitLossValuePercentage"   ) var realizedProfitLossValuePercentage   : Number? = null,
    @SerializedName("unRealizedProfitLossValue"           ) var unRealizedProfitLossValue           : Number? = null,
    @SerializedName("unRealizedProfitLossValuePercentage" ) var unRealizedProfitLossValuePercentage : Number? = null,
    @SerializedName("totalInvestmentValue"                ) var totalInvestmentValue                : Number? = null
)
