package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat


@Parcelize
data class Stock(
    @SerializedName("stock_id") val stockId: Int,
    @SerializedName("stock_code") val stockCode: String?,
    @SerializedName("stock_name") val stockName: String?,
    @SerializedName("stock_api_code") val stockApiCode: String?,
    @SerializedName("stock_type") val stockType: Int,
    @SerializedName("history") val history: ArrayList<StockHistory>,
    @SerializedName("watchlist") val watchList: WatchList?,
    var totalPrice: Int = 0

):Parcelable {
    fun currentValue(): Float {

        return if(history!=null){
            (history.firstOrNull()?.stockHistoryOpen?: 0 +
            history.firstOrNull()?.stockHistoryClose!! ?: 0.0f) / 2
        } else 0.0f

    }

    fun perDiff(): Float {

        val todayPrice = currentValue()
        val size = history?.size ?: 0
        if (size > 1) {
            val openPrice2 =
                history?.get(1)?.stockHistoryOpen ?: 0.0f
            val closePrice2 =
                history?.get(1)?.stockHistoryClose ?: 0.0f
            val totalPrice2 = (openPrice2 + closePrice2)
            val yesterdayPrice = (totalPrice2 / 2)

            if (todayPrice != 0.0f && yesterdayPrice != 0.0f)
                return ((todayPrice - yesterdayPrice) / ((todayPrice + yesterdayPrice) / 2)) * 100;
        }
        return 0.0f
    }


}

@Parcelize
data class StockHistory(
    @SerializedName("stock_history_id") val stockHistoryId: Int,
    @SerializedName("stock_history_code") val stockHistoryCode: String?,
    @SerializedName("stock_history_date") val stockHistoryDate: String?,
    @SerializedName("stock_history_open") val stockHistoryOpen: Float,
    @SerializedName("stock_history_close") val stockHistoryClose: Float,
    @SerializedName("stock_history_high") val stockHistoryHigh: Float,
    @SerializedName("stock_history_low") val stockHistoryLow: Float,
):Parcelable {

    fun formatDate(): String {
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(stockHistoryDate)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(d)
    }
}

@Parcelize
data class WatchList(
    @SerializedName("watchlist_id") val watchlistId: Int,
    @SerializedName("watchlist_stock_id") val watchlistStockId: String?,
    @SerializedName("watchlist_user_id") val watchlistUserId: String?,
    @SerializedName("watch_stock_price") val watchStockPrice: Float,
    @SerializedName("watchlist_date") val watchlistDate: String?,
    @SerializedName("market") val market: Stock?,
) :Parcelable {

    fun formatDate(): String {
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(watchlistDate)
        return SimpleDateFormat("dd/MM/yyyy").format(d)
    }
    fun formatTime(): String {
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(watchlistDate)
        return SimpleDateFormat("HH:mm:ss").format(d)
    }

    fun perDiff(): Float? {
        val currentValue = market?.currentValue()
        if (currentValue != null) {
            return (((currentValue - watchStockPrice) /
                    ((currentValue + watchStockPrice) / 2)) * 100)
        }
        return null
    }


}