package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


//data class WalletSummaryResponse (
//  @SerializedName("message"    ) var message    : String?  = null,
//  @SerializedName("data"       ) var data       : Data?    = Data(),
//  @SerializedName("status"     ) var status     : Boolean? = null,
//  @SerializedName("statusCode" ) var statusCode : Int?     = null
//
//
//)
data class WalletSummaryResponse (

  @SerializedName("user_id"             ) var userId            : Int?    = null,
  @SerializedName("balance"             ) var balance           : Double? = null,
  @SerializedName("trade_money_balance" ) var tradeMoneyBalance : Int?    = null,
  @SerializedName("last_allocated_on"   ) var lastAllocatedOn   : String? = null

)