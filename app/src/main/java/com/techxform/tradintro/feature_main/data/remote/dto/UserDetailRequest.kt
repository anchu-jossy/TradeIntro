package com.techxform.tradintro.feature_main.data.remote.dto

import com.google.gson.annotations.SerializedName


data class UserDetailRequest (

  @SerializedName("user_id"             ) var userId            : Boolean? = null,
  @SerializedName("user_type"           ) var userType          : Boolean? = null,
  @SerializedName("admin_user_id"       ) var adminUserId       : Boolean? = null,
  @SerializedName("secondary_admin"     ) var secondaryAdmin    : Boolean? = null,
  @SerializedName("user_name"           ) var userName          : Boolean? = null,
  @SerializedName("user_last_name"      ) var userLastName      : Boolean? = null,
  @SerializedName("user_phone"          ) var userPhone         : Boolean? = null,
  @SerializedName("user_image"          ) var userImage         : Boolean? = null,
  @SerializedName("user_email"          ) var userEmail         : Boolean? = null,
  @SerializedName("user_password"       ) var userPassword      : Boolean? = null,
  @SerializedName("user_margin"         ) var userMargin        : Boolean? = null,
  @SerializedName("tree_level"          ) var treeLevel         : Boolean? = null,
  @SerializedName("user_login_time"     ) var userLoginTime     : Boolean? = null,
  @SerializedName("user_logout_time"    ) var userLogoutTime    : Boolean? = null,
  @SerializedName("user_status"         ) var userStatus        : Boolean? = null,
  @SerializedName("user_created_on"     ) var userCreatedOn     : Boolean? = null,
  @SerializedName("user_type_change_on" ) var userTypeChangeOn  : Boolean? = null,
  @SerializedName("verification_code"   ) var verificationCode  : Boolean? = null,
  @SerializedName("referal_code"        ) var referalCode       : Boolean? = null,
  @SerializedName("refered_by_user_id"  ) var referedByUserId   : Boolean? = null,
  @SerializedName("daily_login_status"  ) var dailyLoginStatus  : Boolean? = null,
  @SerializedName("user_login_count"    ) var userLoginCount    : Boolean? = null,
  @SerializedName("previous_tree_level" ) var previousTreeLevel : Boolean? = null,
  @SerializedName("help_popup"          ) var helpPopup         : Boolean? = null,
  @SerializedName("invite_email_status" ) var inviteEmailStatus : Boolean? = null

)