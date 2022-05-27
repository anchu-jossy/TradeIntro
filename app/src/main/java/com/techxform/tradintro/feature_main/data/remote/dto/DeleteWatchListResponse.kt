package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class DeleteWatchListResponse (

  @SerializedName("message"    ) var message    : String?  = null,
  @SerializedName("status"     ) var status     : Boolean? = null,
  @SerializedName("statusCode" ) var statusCode : Int?     = null,
  @SerializedName("data"       ) var data       : Data1?    = Data1()

)
data class Data1 (

  @SerializedName("count" ) var count : Int? = null

)