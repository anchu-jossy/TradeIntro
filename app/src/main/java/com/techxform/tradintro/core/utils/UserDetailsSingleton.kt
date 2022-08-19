package com.techxform.tradintro.core.utils

import com.techxform.tradintro.feature_main.data.remote.dto.UserDetailsResponse

object UserDetailsSingleton {

    lateinit var userDetailsResponse: UserDetailsResponse

    fun setUserDetails(userDetailsResponse: UserDetailsResponse)
    {
        this.userDetailsResponse = userDetailsResponse
    }

}