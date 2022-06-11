package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


data class UpdateWalletRequest(

    @SerializedName("user_id") var userId: Number? = null,
    @SerializedName("total_amount") var totalAmount: Number? = null,
    @SerializedName("recharge_amount") var rechargeAmount: Number? = null,
    @SerializedName("gst_amount") var gstAmount: Number? = null,
    @SerializedName("other_recharge_amount") var otherRechargeAmount: Number? = null,

    )