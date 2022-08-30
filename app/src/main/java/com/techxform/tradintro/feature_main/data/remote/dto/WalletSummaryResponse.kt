package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName



data class WalletSummaryResponse (
  @SerializedName("user_id") var userId : Int?    = null,
  @SerializedName("last_allocation_amount") var lastRechargeAmount  : Double? = null,
  @SerializedName("trade_money_balance" ) var tradeMoneyBalance : Double?    = null,
  @SerializedName("last_allocated_on"   ) var lastRechargeOn   : String? = null
)