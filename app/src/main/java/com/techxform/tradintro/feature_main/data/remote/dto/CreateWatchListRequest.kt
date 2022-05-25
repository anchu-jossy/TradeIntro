package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


public data class CreateWatchListRequest(
    @SerializedName("watchlist_stock_id") val watchListStockId: Int,
    @SerializedName("watch_stock_price") val watchListStockPrice: Number,
) {
}

data class CreateWatchListResponse
    (

    @SerializedName("message") var message: String? = null,
    @SerializedName("status") var status: Boolean? = null,
    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("data") var data: Data? = Data()

)

data class History(

    @SerializedName("stock_history_id") var stockHistoryId: Int? = null,
    @SerializedName("stock_history_code") var stockHistoryCode: String? = null,
    @SerializedName("stock_history_date") var stockHistoryDate: String? = null,
    @SerializedName("stock_history_open") var stockHistoryOpen: Int? = null,
    @SerializedName("stock_history_close") var stockHistoryClose: Int? = null,
    @SerializedName("stock_history_high") var stockHistoryHigh: Int? = null,
    @SerializedName("stock_history_low") var stockHistoryLow: Int? = null

)

data class Market(

    @SerializedName("stock_id") var stockId: Int? = null,
    @SerializedName("stock_code") var stockCode: String? = null,
    @SerializedName("stock_name") var stockName: String? = null,
    @SerializedName("stock_api_code") var stockApiCode: String? = null,
    @SerializedName("stock_type") var stockType: Int? = null,
    @SerializedName("history") var history: ArrayList<History> = arrayListOf(),
    @SerializedName("watchlist") var watchlist: String? = null

)

//data class Watchlist(
//
//    @SerializedName("watchlist_id") var watchlistId: Int? = null,
//    @SerializedName("watchlist_stock_id") var watchlistStockId: Int? = null,
//    @SerializedName("watchlist_user_id") var watchlistUserId: Int? = null,
//    @SerializedName("watch_stock_price") var watchStockPrice: Int? = null,
//    @SerializedName("watchlist_date") var watchlistDate: String? = null,
//    @SerializedName("market") var market: Market? = Market()
//
//)

data class Data(

    @SerializedName("watchlist_id") var watchlistId: Int? = null,
    @SerializedName("watchlist_stock_id") var watchlistStockId: Int? = null,
    @SerializedName("watchlist_user_id") var watchlistUserId: Int? = null,
    @SerializedName("watch_stock_price") var watchStockPrice: Int? = null,
    @SerializedName("watchlist_date") var watchlistDate: String? = null,
    @SerializedName("market") var market: Market? = Market()

)