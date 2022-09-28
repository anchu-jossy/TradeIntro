package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcelable
import com.bumptech.glide.util.Util
import com.google.gson.annotations.SerializedName
import com.techxform.tradintro.feature_main.domain.util.Utils
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
    @SerializedName("alert") var watchListAlert: Notifications?,
    @SerializedName("portfolioItems") var portfolioItems: ArrayList<PortfolioItem>?,
    var totalPrice: Int = 0

) : Parcelable {

    fun apiCode(): String {
        if (stockApiCode.isNullOrEmpty())
            return ""
        val arr = stockApiCode.split(".")
        return if (arr.size > 1)
            arr[0] + "(" + arr[1] + ")"
        else
            arr[0]
    }

    fun currentValue(): Float {

        val amount = if (history.isEmpty()) 0f
        else (history.first().stockHistoryHigh +
                history.first().stockHistoryLow) / 2

        return Utils.formatBigDecimalIntoTwoDecimal(amount.toBigDecimal()).toFloat()

    }

    fun perDiff(): Float {
        if (!history.isNullOrEmpty()) {
            val todayPrice = currentValue()
            val size = history.size ?: 0
            if (size > 1) {
                val change = todayPrice - history.first().stockHistoryOpen
                return (change / history.first().stockHistoryOpen) * 100
            }
        }
        return 0.0f
    }

    fun asPercentageText(): String {
        val diff = perDiff()
        return "${Utils.formatStringToTwoDecimals(diff.toString())}%"
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

    fun formatCode(): String {
        val code = stockHistoryCode?.split('.')?.get(1).toString()
        return code.replaceFirstChar { char -> char.lowercase() }
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
    @SerializedName("alert") var alert: Notifications?,
) : Parcelable {

    fun perDiff(): Float {
        // watchlist price - current price (instead of open)
        market?.let {
            val change = gainLossDiffAmount()
            return (change / market.currentValue()) * 100
        }
        return 0.0f
    }

    fun gainLossDiffAmount():Float{
        market?.let {
            return watchStockPrice - it.currentValue()
        }
        return 0.0f
    }

    fun asPercentageText(): String {
        val diff = gainLossDiffAmount()
        return "${Utils.formatStringToTwoDecimals(diff.toString())}%"
    }

    fun formatCode(): String {
        val code = market?.stockApiCode?.split('.')?.get(1).toString()
        return code.replaceFirstChar { char -> char.lowercase() }
    }


}