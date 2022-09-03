package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("currentPassword") var currentPassword: String,
    @SerializedName("newPassword") var newPassword: String,
) {
}