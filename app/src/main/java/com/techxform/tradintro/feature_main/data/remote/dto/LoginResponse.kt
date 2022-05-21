package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


data class LoginResponse(
    @SerializedName("token") val token: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("pkiKey") val pkiKey: String
) {
}

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
) {}