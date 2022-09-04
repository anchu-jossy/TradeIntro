package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserLevelHistoryResponse (
  @SerializedName("myLevel"  ) var myLevel  : Int?              = null,
  @SerializedName("myPoints" ) var myPoints : Int?              = null,
  @SerializedName("levels"   ) var levels   : ArrayList<Level> = arrayListOf()


)

data class Level (

  @SerializedName("points_id"         ) var pointsId        : Int?    = null,
  @SerializedName("points_user_id"    ) var pointsUserId    : Int?    = null,
  @SerializedName("points_user_level" ) var pointsUserLevel : Int?    = null,
  @SerializedName("points_type"       ) var pointsType      : String? = null,
  @SerializedName("points_per_unit"   ) var pointsPerUnit   : Int?    = null,
  @SerializedName("unit"              ) var unit            : Int?    = null,
  @SerializedName("total_value"       ) var totalValue      : Int?    = null,
  @SerializedName("total_points"      ) var totalPoints     : Int?    = null,
  @SerializedName("points_date"       ) var pointsDate      : String? = null

)

