package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateWalletResponse(
    @SerializedName("status") var status: String? = null,
    @SerializedName("paymentLink") var paymentLink: String? = null,

    ) {
}