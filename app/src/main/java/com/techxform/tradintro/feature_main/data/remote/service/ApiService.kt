package com.techxform.tradintro.feature_main.data.remote.service

import com.techxform.tradintro.feature_main.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("v1/data")
    suspend fun getDataList(): List<ApiData>

    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>

    @GET("api/market")
    suspend fun marketList(@QueryMap reqMap:Map<String,String>) : Response<BaseResponse<ArrayList<Stock>>>


}