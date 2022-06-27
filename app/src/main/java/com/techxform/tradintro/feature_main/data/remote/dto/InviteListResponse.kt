package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName






data class InviteData (

    @SerializedName("invite_id"           ) var inviteId          : Int?    = null,
    @SerializedName("invite_from"         ) var inviteFrom        : String? = null,
    @SerializedName("invite_from_member"  ) var inviteFromMember  : Int?    = null,
    @SerializedName("invite_to"           ) var inviteTo          : String? = null,
    @SerializedName("invite_email"        ) var inviteEmail       : String? = null,
    @SerializedName("invite_status"       ) var inviteStatus      : Int?    = null,
    @SerializedName("invite_email_status" ) var inviteEmailStatus : Int?    = null,
    @SerializedName("invite_date"         ) var inviteDate        : String? = null

)

