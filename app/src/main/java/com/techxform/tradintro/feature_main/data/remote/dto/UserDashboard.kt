package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDashboard(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("level_badge_image") val levelBadgeImage: String,
    @SerializedName("user_level") val userLevel: Int,
    @SerializedName("last_login") val lastLogin: String,
    @SerializedName("current_points") val currentPoints: Int,
)
