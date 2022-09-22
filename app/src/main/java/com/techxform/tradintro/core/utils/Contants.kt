package com.techxform.tradintro.core.utils

object Contants {
    const val BASE_URL = "https://api.tradintro.com/"
    const val IMAGE_BASE_URL = "https://www.tradintro.com//uploads/"
    const val PREFERENCE = "Trade"
    const val PREF_TOKEN_KEY = "token"
    const val PREF_USERNAME_KEY = "username"
    const val PREF_PASSWORD_KEY = "password"
    const val USER_ID_KEY = "USER_ID"
    const val PREF_FCM_TOKEN_KEY = "fcm_token"
    const val PREF_FCM_TOKEN_SYNC_KEY = "isFcmTokenSync"
    const val PREF_REFRESH_TOKEN_KEY = "refresh_token"
    const val PREF_USER_FULL_NAME_KEY = "user_full_name"
   // private const val DEV_URL = "https://dev.tradintro.com/"
    //TODO() this is commented only for  testing the build
     private const val DEV_URL = "https://stage.tradintro.com/"


    const val RECHARGE_URL = DEV_URL+"index.php/api/update_wallet"
    const val ADD_USER_URL = DEV_URL+"index.php/api/add_invite_user"
    const val FORGOT_PASSWORD = DEV_URL + "index.php/api/forgot_password"
    const val RESENT_PASSWORD = DEV_URL + "index.php/api/resend_email"
    const val REGISTER = DEV_URL + "index.php/api/register"
    const val IMAGE_URL="https://stage.tradintro.com/uploads/"
    const val delete ="DELETE"
    const val update="UPDATE"


}