package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter


data class Stock(
    @SerializedName("stock_id") val stockId: Int,
    @SerializedName("stock_code") val stockCode: String,
    @SerializedName("stock_name") val stockName: String,
    @SerializedName("stock_api_code") val stockApiCode: String,
    @SerializedName("stock_type") val stockType: Int,
    @SerializedName("history") val history: MutableList<StockHistory>,
    @SerializedName("watchlist") val watchList: WatchList

) {}

data class StockHistory(
    @SerializedName("stock_history_id") val stockHistoryId: Int,
    @SerializedName("stock_history_code") val stockHistoryCode: String,
    @SerializedName("stock_history_date") val stockHistoryDate: String,
    @SerializedName("stock_history_open") val stockHistoryOpen: Float,
    @SerializedName("stock_history_close") val stockHistoryClose: Float,
    @SerializedName("stock_history_high") val stockHistoryHigh: Float,
    @SerializedName("stock_history_low") val stockHistoryLow: Float,
){
    fun formatDate():String
    {
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(stockHistoryDate)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d)
    }
}

data class WatchList(
    @SerializedName("watchlist_id") val watchlistId: Int,
    @SerializedName("watchlist_stock_id") val watchlistStockId: String,
    @SerializedName("watchlist_user_id") val watchlistUserId: String,
    @SerializedName("watch_stock_price") val watchStockPrice: Float,
    @SerializedName("watchlist_date") val watchlistDate: String,
){
    fun formatDate():String
    {
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(watchlistDate)
        return SimpleDateFormat("dd/MM/yyyy").format(d)
    }

}