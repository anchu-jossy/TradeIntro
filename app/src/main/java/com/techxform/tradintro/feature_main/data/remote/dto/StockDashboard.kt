package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName


data class StockDashboard(
    @SerializedName("avgPurchasePrice") val avgPurchasePrice: Float?,
    @SerializedName("currentPrice") val currentPrice: Float?,
    @SerializedName("gainLossPercentage") val gainLossPercentage: Float?,
    @SerializedName("gainLossValue") val gainLossValue: Float?,
    @SerializedName("totalPrice") val totalPrice: Float?,
    @SerializedName("totalValue") val totalValue: Float?,
    @SerializedName("qty") val qty: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("stockName") val stockName: String?,
    @SerializedName("stockCode") val stockCode: String?,
    @SerializedName("alertPrice") val alertPrice: Float?,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(avgPurchasePrice)
        parcel.writeValue(currentPrice)
        parcel.writeValue(gainLossPercentage)
        parcel.writeValue(gainLossValue)
        parcel.writeValue(totalPrice)
        parcel.writeValue(totalValue)
        parcel.writeInt(qty)
        parcel.writeInt(userId)
        parcel.writeString(stockName)
        parcel.writeString(stockCode)
        parcel.writeValue(alertPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StockDashboard> {
        override fun createFromParcel(parcel: Parcel): StockDashboard {
            return StockDashboard(parcel)
        }

        override fun newArray(size: Int): Array<StockDashboard?> {
            return arrayOfNulls(size)
        }
    }
}