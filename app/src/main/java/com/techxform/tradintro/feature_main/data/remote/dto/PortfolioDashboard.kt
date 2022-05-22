package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PortfolioDashboard(
    @SerializedName("holding_value") val holdingValue: Float?,
    @SerializedName("holding_value_diff") val holdingValueDiff: Float?,
    @SerializedName("holding_value_diff_percentage") val holdingValueDiffPer: Float?,
    @SerializedName("trade_money_balance") val tradeMoneyBalance: Float?,
    @SerializedName("total_investment") val totalInvestment: Float?,
    @SerializedName("user_id") val userId: Int,
) :Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(holdingValue)
        parcel.writeValue(holdingValueDiff)
        parcel.writeValue(holdingValueDiffPer)
        parcel.writeValue(tradeMoneyBalance)
        parcel.writeValue(totalInvestment)
        parcel.writeInt(userId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PortfolioDashboard> {
        override fun createFromParcel(parcel: Parcel): PortfolioDashboard {
            return PortfolioDashboard(parcel)
        }

        override fun newArray(size: Int): Array<PortfolioDashboard?> {
            return arrayOfNulls(size)
        }
    }
}