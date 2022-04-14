package com.techxform.tradintro.data.remote.service

import com.techxform.tradintro.data.remote.dto.ApiData
import retrofit2.http.GET

interface ApiService {
    @GET("v1/data")
    suspend fun getDataList(): List<ApiData>
}