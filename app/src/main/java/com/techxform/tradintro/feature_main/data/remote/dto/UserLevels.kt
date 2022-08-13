package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.techxform.tradintro.R

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
    var userLevel: Int? = null,

):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {

    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(userLevelId)
        parcel.writeString(userLevelName)
        parcel.writeString(userLevelImage)
        parcel.writeValue(userLevelPoints)
        parcel.writeString(userLevelFeature)
        parcel.writeString(earnPoints)
        parcel.writeValue(levelPosition)
        parcel.writeValue(loginPoint)
        parcel.writeValue(maxLoginPoint)
        parcel.writeValue(rechargePts)
        parcel.writeValue(maxRechargePts)
        parcel.writeValue(rechargeUnit)
        parcel.writeValue(ptsPerBuy)
        parcel.writeValue(maxPtsPerBuy)
        parcel.writeValue(ptsPerUnitBuyTradeValue)
        parcel.writeValue(unitBuyTradeValue)
        parcel.writeValue(maxPtsBuyTradeValue)
        parcel.writeValue(ptsPerStock)
        parcel.writeValue(maxPtsPerStock)
        parcel.writeValue(ptsPerReferal)
        parcel.writeValue(maxPtsPerReferal)
        parcel.writeValue(ptsPerWatchlist)
        parcel.writeValue(maxPtsPerWatchlist)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Levels> {
        override fun createFromParcel(parcel: Parcel): Levels {
            return Levels(parcel)
        }

        override fun newArray(size: Int): Array<Levels?> {
            return arrayOfNulls(size)
        }
    }

}