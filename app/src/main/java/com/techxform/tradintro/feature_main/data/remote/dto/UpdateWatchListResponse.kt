package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UpdateWatchListResponse (

  @SerializedName("message"    ) var message    : String?  = null,
  @SerializedName("status"     ) var status     : Boolean? = null,
  @SerializedName("statusCode" ) var statusCode : Int?     = null,
  @SerializedName("data"       ) var data       : UpdateData?    = UpdateData()

)
data class UpdateData (

  @SerializedName("count" ) var count : Int? = null

)