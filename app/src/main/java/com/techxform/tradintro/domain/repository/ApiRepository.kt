package com.techxform.tradintro.domain.repository

import com.techxform.tradintro.data.remote.dto.ApiData

interface ApiRepository {
    suspend fun getDataList(): List<ApiData>
}