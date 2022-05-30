package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserLevels(
    @SerializedName("myLevel") var myLevel: Int? = null,
    @SerializedName("myPoints") var myPoints: Float? = null,
    @SerializedName("levels") var levels: ArrayList<Levels>
) {
}


data class Levels(
    @SerializedName("user_level_id") var userLevelId: Int? = null,
    @SerializedName("user_level_name") var userLevelName: String? = null,
    @SerializedName("user_level_image") var userLevelImage: String? = null,
    @SerializedName("user_level_points") var userLevelPoints: Float? = null,
    @SerializedName("user_level_feature") var userLevelFeature: String? = null,
    @SerializedName("earn_points") var earnPoints: String? = null,
    @SerializedName("level_position") var levelPosition: Int? = null,
    @SerializedName("login_point") var loginPoint: Int? = null,
    @SerializedName("max_login_point") var maxLoginPoint: Int? = null,
    @SerializedName("recharge_pts") var rechargePts: Int? = null,
    @SerializedName("max_recharge_pts") var maxRechargePts: Int? = null,
    @SerializedName("recharge_unit") var rechargeUnit: Int? = null,
    @SerializedName("pts_per_buy") var ptsPerBuy: Int? = null,
    @SerializedName("max_pts_per_buy") var maxPtsPerBuy: Int? = null,
    @SerializedName("pts_per_unit_buy_trade_value") var ptsPerUnitBuyTradeValue: Int? = null,
    @SerializedName("unit_buy_trade_value") var unitBuyTradeValue: Int? = null,
    @SerializedName("max_pts_buy_trade_value") var maxPtsBuyTradeValue: Int? = null,
    @SerializedName("pts_per_stock") var ptsPerStock: Int? = null,
    @SerializedName("max_pts_per_stock") var maxPtsPerStock: Int? = null,
    @SerializedName("pts_per_referal") var ptsPerReferal: Int? = null,
    @SerializedName("max_pts_per_referal") var maxPtsPerReferal: Int? = null,
    @SerializedName("pts_per_watchlist") var ptsPerWatchlist: Int? = null,
    @SerializedName("max_pts_per_watchlist") var maxPtsPerWatchlist: Int? = null,

){}