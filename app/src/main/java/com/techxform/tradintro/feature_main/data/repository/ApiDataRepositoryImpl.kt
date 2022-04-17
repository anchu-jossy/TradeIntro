package com.techxform.tradintro.feature_main.data.repository

import com.techxform.tradintro.feature_main.data.remote.dto.ApiData
import com.techxform.tradintro.feature_main.data.remote.service.ApiService
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository

class ApiDataRepositoryImpl(private val apiService: ApiService) : ApiRepository {
    override suspend fun getDataList(): List<ApiData> {
        return apiService.getDataList()
    }
}