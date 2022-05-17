package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


data class Stock(
    @SerializedName("stock_id") val stockId: Int,
    @SerializedName("stock_code") val stockCode: String,
    @SerializedName("stock_name") val stockName: String,
    @SerializedName("stock_api_code") val stockApiCode: String,
    @SerializedName("stock_type") val stockType: Int,
    @SerializedName("history") val history: MutableList<StockHistory>,

) {}

data class StockHistory(
    @SerializedName("stock_history_id") val stockHistoryId: Int,
    @SerializedName("stock_history_code") val stockHistoryCode: String,
    @SerializedName("stock_history_date") val stockHistoryDate: String,
    @SerializedName("stock_history_open") val stockHistoryOpen: Float,
    @SerializedName("stock_history_close") val stockHistoryClose: Float,
    @SerializedName("stock_history_high") val stockHistoryHigh: Float,
    @SerializedName("stock_history_low") val stockHistoryLow: Float,
){}