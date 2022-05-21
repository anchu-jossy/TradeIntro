package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class Notifications(
    @SerializedName("notification_id") val notificationId: Int,
    @SerializedName("notification_news_id") val notificationNewsId: Int,
    @SerializedName("notification_user_id") val notificationUserId: Int,
    @SerializedName("notification_user_id") val notificationUserType: String,
    @SerializedName("notification_price") val notificationPrice: Float,
    @SerializedName("notification_heading") val notificationHeading: String,
    @SerializedName("notification_text") val notificationText: String,
    @SerializedName("notification_stock_id") val notificationStockId: Int,
    @SerializedName("notification_stock_id") val notificationType: Int,
    @SerializedName("notification_start_date") val notificationStartDate: String,
    @SerializedName("notification_execute_date") val notificationExecuteDate: String,
    @SerializedName("notification_shedule_date") val notificationSheduleDate: String,
    @SerializedName("notification_status") val notificationStatus: Int,
    @SerializedName("alert_popup_status") val alertPopupStatus: Int,
    @SerializedName("market") val market: Stock,

    )
