package com.techxform.tradintro.feature_main.data.remote.dto

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat


data class Notifications(
    @SerializedName("notification_id") val notificationId: Int,
    @SerializedName("notification_news_id") val notificationNewsId: Int,
    @SerializedName("notification_user_id") val notificationUserId: Int,
    @SerializedName("notification_user_type") val notificationUserType: String?,
    @SerializedName("notification_price") val notificationPrice: Float,
    @SerializedName("notification_heading") val notificationHeading: String?,
    @SerializedName("notification_text") val notificationText: String?,
    @SerializedName("notification_stock_id") val notificationStockId: Int,
    @SerializedName("notification_type") val notificationType: Int,
    @SerializedName("notification_start_date") val notificationStartDate: String?,
    @SerializedName("notification_execute_date") val notificationExecuteDate: String?,
    @SerializedName("notification_shedule_date") val notificationSheduleDate: String?,
    @SerializedName("notification_status") val notificationStatus: Int,
    @SerializedName("alert_popup_status") val alertPopupStatus: Int,
    @SerializedName("market") val market: Stock?,

    ) : Parcelable {
    fun heading() :String
    {
        if(notificationType != 3 && notificationHeading.isNullOrEmpty())
        {
            return market?.stockName ?: ""
        }
        return notificationHeading ?: ""
    }


    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readParcelable(Stock::class.java.classLoader)
    ) {
    }

    fun formatDate(): String {
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(notificationExecuteDate)
        return SimpleDateFormat("dd/MM/yyyy").format(d)
    }
    fun formatTime(): String {
        val d = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(notificationExecuteDate)
        return SimpleDateFormat("HH:mm:ss").format(d)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(notificationId)
        parcel.writeInt(notificationNewsId)
        parcel.writeInt(notificationUserId)
        parcel.writeString(notificationUserType)
        parcel.writeFloat(notificationPrice)
        parcel.writeString(notificationHeading)
        parcel.writeString(notificationText)
        parcel.writeInt(notificationStockId)
        parcel.writeInt(notificationType)
        parcel.writeString(notificationStartDate)
        parcel.writeString(notificationExecuteDate)
        parcel.writeString(notificationSheduleDate)
        parcel.writeInt(notificationStatus)
        parcel.writeInt(alertPopupStatus)
        parcel.writeParcelable(market, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Notifications> {
        override fun createFromParcel(parcel: Parcel): Notifications {
            return Notifications(parcel)
        }

        override fun newArray(size: Int): Array<Notifications?> {
            return arrayOfNulls(size)
        }
    }

}

data class DeleteNotificationResponse( @SerializedName("count") val count: Int,){}
