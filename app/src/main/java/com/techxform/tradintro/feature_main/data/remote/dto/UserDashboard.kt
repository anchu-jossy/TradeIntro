package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Build
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

data class UserDashboard(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("user_name") val userName: String,
    @SerializedName("level_badge_image") val levelBadgeImage: String,
    @SerializedName("user_level") val userLevel: Int,
    @SerializedName("last_login") val lastLogin: String,
    @SerializedName("current_points") val currentPoints: Float,
) {

    /*fun formatLastLogin(): String {
        val givenPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val displayPattern = "dd-MM-yyyy | HH:mm aa"

        val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //2022-05-21T16:04:26.998Z
           // val firstApiFormat = DateTimeFormatter.ofPattern(givenPattern)
           // val d = LocalDate.parse(lastLogin, firstApiFormat)
            val secTimeFormat = DateTimeFormatter.ofPattern(displayPattern)
            LocalDate.parse(lastLogin, secTimeFormat).toString()

        } else {
            val dateFormat = SimpleDateFormat(
                givenPattern, Locale.ENGLISH
            )
            val d = dateFormat.parse(lastLogin)
            return SimpleDateFormat(displayPattern).format(d)
        }
        return date
    }*/
}
