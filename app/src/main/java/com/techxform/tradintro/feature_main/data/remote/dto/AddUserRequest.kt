package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


data class AddUserRequest(

    @SerializedName("user_id") var userId: Number? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("email") var email: String? = null

)
data class AddUserResponse(

    @SerializedName("status") var status: String? = null,


)

data class EditUserProfileReq(
    @SerializedName("image") var image: String? = null,
    @SerializedName("user_name") var userName: String? = null,
    @SerializedName("user_last_name") var lastName: String? = null,
    @SerializedName("user_phone") var userPhone: String? = null
)
{}