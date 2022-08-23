package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("error") val error: BaseError,
    ) {
}

data class BaseError(
    @SerializedName("message") val message: String,
    @SerializedName("name") val status: Boolean,
    @SerializedName("statusCode") val statusCode: Int,
){}