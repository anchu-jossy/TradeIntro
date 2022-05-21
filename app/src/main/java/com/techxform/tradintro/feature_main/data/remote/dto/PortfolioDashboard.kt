package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class PortfolioDashboard(
    @SerializedName("holding_value") val holdingValue: Float?,
    @SerializedName("holding_value_diff") val holdingValueDiff: Float?,
    @SerializedName("holding_value_diff_percentage") val holdingValueDiffPer: Float?,
    @SerializedName("trade_money_balance") val tradeMoneyBalance: Float?,
    @SerializedName("total_investment") val totalInvestment: Float?,
    @SerializedName("user_id") val userId: Int,
) {
}