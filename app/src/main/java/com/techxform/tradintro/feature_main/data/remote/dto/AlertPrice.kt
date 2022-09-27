package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AlertPriceResponse(@SerializedName("count") val count: Int)
{}

data class DeleteAlertPriceResponse(@SerializedName("count") val count: Int)
{}


data class AlertPriceRequest(@SerializedName("amount") val amount: Double)
{}



