package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat

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


) {

    fun formatDateTime(): Pair<String, String> {

        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(orderExecutedOn)
        val date = SimpleDateFormat("yyyy-MM-dd").format(d)
        val time = SimpleDateFormat("HH:mm:ss").format(d)

        return Pair(date, time)
    }

    fun perDiff(): Float {
        val currentValue = market.currentValue()
        return (((currentValue - orderPrice) /
                ((currentValue + orderPrice) / 2)) * 100)
    }

}

data class UpdatePortfolioRequest(@SerializedName("alert_price")val alert_price :Number,@SerializedName("order_qty")val order_qty:Number)