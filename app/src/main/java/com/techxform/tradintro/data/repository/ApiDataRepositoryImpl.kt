package com.techxform.tradintro.data.repository

import com.techxform.tradintro.data.remote.dto.ApiData
import com.techxform.tradintro.data.remote.service.ApiService
import com.techxform.tradintro.domain.repository.ApiRepository

class ApiDataRepositoryImpl(private val apiService: ApiService) : ApiRepository {
    override suspend fun getDataList(): List<ApiData> {
        return apiService.getDataList()
    }
}