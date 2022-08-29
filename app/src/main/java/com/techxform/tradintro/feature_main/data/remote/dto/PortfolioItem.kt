package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PortfolioItem(
    @SerializedName("order_id") val orderId: Int,
    @SerializedName("order_stock_id") val orderStockId: Int,
    @SerializedName("order_user_id") val orderUserId: Int,
    @SerializedName("order_no") val orderNo: String,
    @SerializedName("order_price") val orderPrice: Float,
    @SerializedName("total_stock_value") val totalStockValue: Float,
    @SerializedName("brokerage") val brokerage: Float,
    @SerializedName("transaction_charge") val transactionCharge: Float,
    @SerializedName("order_total") val orderTotal: Float,
    @SerializedName("order_qty") val orderQty: Int,
    @SerializedName("order_createdon") val orderCreatedOn: String,
    @SerializedName("order_executed_on") val orderExecutedOn: String,
    @SerializedName("order_type") val orderType: Int,
    @SerializedName("order_execution_type") val orderExecutionType: Int,
    @SerializedName("order_status") val orderStatus: Int,
    @SerializedName("market_status") val marketStatus: Int,
    @SerializedName("portfolio_status") val portfolioStatus: Int,
    @SerializedName("order_validity") val orderValidity: Int,
    @SerializedName("order_validity_date") val orderValidityDate: String,
    @SerializedName("order_email_status") val orderEmailStatus: Int,
    @SerializedName("market") val market: Stock,
    @SerializedName("isEditEnabled") val isEditEnabled: Boolean,
    @SerializedName("isPriceEditEnabled") val isPriceEnabled: Boolean,
    @SerializedName("gainLossPercentage") val gainLossPercentage: Float?,
    @SerializedName("alertPrice") val alertPrice: Float,
):Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readParcelable(Stock::class.java.classLoader)!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readValue(Float::class.java.classLoader) as? Float,
        parcel.readFloat()
    ) {
    }

    fun perDiff(): Float {
       return gainLossPercentage ?: orderQty.toFloat()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(orderId)
        parcel.writeInt(orderStockId)
        parcel.writeInt(orderUserId)
        parcel.writeString(orderNo)
        parcel.writeFloat(orderPrice)
        parcel.writeFloat(totalStockValue)
        parcel.writeFloat(brokerage)
        parcel.writeFloat(transactionCharge)
        parcel.writeFloat(orderTotal)
        parcel.writeInt(orderQty)
        parcel.writeString(orderCreatedOn)
        parcel.writeString(orderExecutedOn)
        parcel.writeInt(orderType)
        parcel.writeInt(orderExecutionType)
        parcel.writeInt(orderStatus)
        parcel.writeInt(marketStatus)
        parcel.writeInt(portfolioStatus)
        parcel.writeInt(orderValidity)
        parcel.writeString(orderValidityDate)
        parcel.writeInt(orderEmailStatus)
        parcel.writeParcelable(market, flags)
        parcel.writeByte(if (isEditEnabled) 1 else 0)
        parcel.writeByte(if (isPriceEnabled) 1 else 0)
        parcel.writeValue(gainLossPercentage)
        parcel.writeFloat(alertPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PortfolioItem> {
        override fun createFromParcel(parcel: Parcel): PortfolioItem {
            return PortfolioItem(parcel)
        }

        override fun newArray(size: Int): Array<PortfolioItem?> {
            return arrayOfNulls(size)
        }
    }

}

data class UpdatePortfolioRequest(
    @SerializedName("alert_price") val alert_price: Number,
    @SerializedName("order_qty") val order_qty: Number
)