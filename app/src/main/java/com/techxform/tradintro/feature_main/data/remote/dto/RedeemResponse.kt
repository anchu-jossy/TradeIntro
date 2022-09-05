package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


data class RedeemResponse(


    @SerializedName("voucher_id"         ) var voucherId        : Int?    = null,
    @SerializedName("voucher_code"       ) var voucherCode      : String? = null,
    @SerializedName("voucher_user_type"  ) var voucherUserType  : String? = null,
    @SerializedName("voucher_amount"     ) var voucherAmount    : Int?    = null,
    @SerializedName("voucher_valid_from" ) var voucherValidFrom : String? = null,
    @SerializedName("voucher_valid_to"   ) var voucherValidTo   : String? = null,
    @SerializedName("voucher_type"       ) var voucherType      : Int?    = null,
    @SerializedName("voucher_remarks"    ) var voucherRemarks   : String? = null,
    @SerializedName("voucher_created_on" ) var voucherCreatedOn : String? = null
)


