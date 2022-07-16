package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
//    @SerializedName("confirm_password")
//    val confirmPassword: String
) {
}


