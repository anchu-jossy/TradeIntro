package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BuyStockReq(
    @SerializedName("order_qty") val orderQty: Int,
    @SerializedName("order_execution_type") val orderExecutionType: Int,
    @SerializedName("stock_code") val stockCode: String,
    @SerializedName("order_validity") val orderValidity: Int,
    @SerializedName("alert_price") val alertPrice: Float,
    @SerializedName("order_validity_date") val orderValidityDate: String,
    )
