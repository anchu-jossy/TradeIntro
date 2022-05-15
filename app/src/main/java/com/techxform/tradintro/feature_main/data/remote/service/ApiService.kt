package com.techxform.tradintro.feature_main.data.remote.service

import com.techxform.tradintro.feature_main.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("v1/data")
    suspend fun getDataList(): List<ApiData>

    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>


}