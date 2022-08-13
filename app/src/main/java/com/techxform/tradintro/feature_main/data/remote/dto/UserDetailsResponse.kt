package com.techxform.tradintro.feature_main.data.remote.dto

import UserLevel
import com.google.gson.annotations.SerializedName

data class UserDetailsResponse (

  @SerializedName("user_id"             ) var userId            : Int?    = null,
  @SerializedName("user_type"           ) var userType          : String? = null,
  @SerializedName("admin_user_id"       ) var adminUserId       : Int?    = null,
  @SerializedName("secondary_admin"     ) var secondaryAdmin    : Int?    = null,
  @SerializedName("user_name"           ) var userName          : String? = null,
  @SerializedName("user_last_name"      ) var userLastName      : String? = null,
  @SerializedName("user_phone"          ) var userPhone         : String? = null,
  @SerializedName("user_image"          ) var userImage         : String? = null,
  @SerializedName("user_email"          ) var userEmail         : String? = null,
  @SerializedName("user_password"       ) var userPassword      : String? = null,
  @SerializedName("user_margin"         ) var userMargin        : Int?    = null,
  @SerializedName("tree_level"          ) var treeLevel         : Int?    = null,
  @SerializedName("user_login_time"     ) var userLoginTime     : String? = null,
  @SerializedName("user_logout_time"    ) var userLogoutTime    : String? = null,
  @SerializedName("user_status"         ) var userStatus        : Int?    = null,
  @SerializedName("user_created_on"     ) var userCreatedOn     : String? = null,
  @SerializedName("user_type_change_on" ) var userTypeChangeOn  : String? = null,
  @SerializedName("verification_code"   ) var verificationCode  : String? = null,
  @SerializedName("referal_code"        ) var referalCode       : String? = null,
  @SerializedName("refered_by_user_id"  ) var referedByUserId   : Int?    = null,
  @SerializedName("daily_login_status"  ) var dailyLoginStatus  : Int?    = null,
  @SerializedName("user_login_count"    ) var userLoginCount    : Int?    = null,
  @SerializedName("previous_tree_level" ) var previousTreeLevel : Int?    = null,
  @SerializedName("help_popup"          ) var helpPopup         : Int?    = null,
  @SerializedName("invite_email_status" ) var inviteEmailStatus : Int?    = null,
  @SerializedName("userLevel") val userLevel : UserLevel

)
