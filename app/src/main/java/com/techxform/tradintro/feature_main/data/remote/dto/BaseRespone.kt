package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

class BaseResponse<T> (
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: Boolean,
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("data") val data: T,
    @SerializedName("error") val error: ErrorBody?=null
)  {

}

class ErrorBody(
    @SerializedName("statusCode") val statusCode: Int,
    @SerializedName("message") val message: String,
    @SerializedName("name") val name: String,
)
