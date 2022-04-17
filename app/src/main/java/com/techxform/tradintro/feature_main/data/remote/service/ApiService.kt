package com.techxform.tradintro.feature_main.data.remote.service

import com.techxform.tradintro.feature_main.data.remote.dto.ApiData
import retrofit2.http.GET

interface ApiService {
    @GET("v1/data")
    suspend fun getDataList(): List<ApiData>
}