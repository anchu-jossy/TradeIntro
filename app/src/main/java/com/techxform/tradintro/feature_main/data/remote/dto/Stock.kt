package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class Stock(
    @SerializedName("stock_id") val stockId: Int,
    @SerializedName("stock_code") val stockCode: String?,
    @SerializedName("stock_name") val stockName: String?,
    @SerializedName("stock_api_code") val stockApiCode: String?,
    @SerializedName("stock_type") val stockType: Int,
    @SerializedName("history") val history: ArrayList<StockHistory>,
    @SerializedName("watchlist") var watchList: WatchList?,
    var totalPrice: Int = 0

) : Parcelable {
    fun currentValue(): Float {
        return if (history.isNullOrEmpty()) 0f
        else (history.first().stockHistoryHigh +
                history.first().stockHistoryLow) / 2

    }

    fun perDiff(): Float {
        if (!history.isNullOrEmpty()) {
            val todayPrice = currentValue()
            val size = history.size ?: 0
            if (size > 1) {
                /*     val openPrice2 =
                     history[1].stockHistoryHigh
                 val closePrice2 =
                     history[1].stockHistoryLow
                 val totalPrice2 = (openPrice2 + closePrice2)
                 val yesterdayPrice = (totalPrice2 / 2)

                 if (todayPrice != 0.0f && yesterdayPrice != 0.0f)
                     return ((todayPrice - yesterdayPrice) / ((todayPrice + yesterdayPrice) / 2)) * 100;*/
                val change = todayPrice - history.first().stockHistoryOpen
                return (change / history.first().stockHistoryOpen) * 100
            }
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
) : Parcelable {

}

@Parcelize
data class WatchList(
    @SerializedName("watchlist_id") val watchlistId: Int,
    @SerializedName("watchlist_stock_id") val watchlistStockId: String?,
    @SerializedName("watchlist_user_id") val watchlistUserId: String?,
    @SerializedName("watch_stock_price") val watchStockPrice: Float,
    @SerializedName("watchlist_date") val watchlistDate: String?,
    @SerializedName("market") val market: Stock?,
) : Parcelable {

    fun perDiff(): Float? {
        val currentValue = market?.currentValue()
        if (currentValue != null) {
            return (((currentValue - watchStockPrice) /
                    ((currentValue + watchStockPrice) / 2)) * 100)
        }
        return null
    }

    fun formatCode(): String {
        return market?.stockApiCode?.split('.')?.get(1).toString()
    }


}