package com.techxform.tradintro.feature_main.domain.repository

import com.techxform.tradintro.feature_main.data.remote.dto.ApiData

interface ApiRepository {
    suspend fun getDataList(): List<ApiData>
}