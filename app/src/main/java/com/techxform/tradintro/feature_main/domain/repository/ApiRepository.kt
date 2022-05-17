package com.techxform.tradintro.feature_main.domain.repository

import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.MarketSearchModel

interface ApiRepository {
    suspend fun getDataList(): List<ApiData>

    suspend fun login(loginRequest: LoginRequest): Result<BaseResponse<LoginResponse>>

    suspend fun marketList(marketSearchModel: MarketSearchModel): Result<BaseResponse<ArrayList<Stock>>>
}