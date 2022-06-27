package com.techxform.tradintro.feature_main.data.remote

import com.google.gson.annotations.SerializedName

data class FcmTokenRegReq(@SerializedName("fcmToken") val token: String,
                          @SerializedName("deviceId") val deviceId: String,
                          @SerializedName("device") val device: String)
