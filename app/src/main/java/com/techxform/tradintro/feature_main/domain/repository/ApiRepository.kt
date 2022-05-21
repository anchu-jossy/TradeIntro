package com.techxform.tradintro.feature_main.domain.repository

import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import retrofit2.Response
import retrofit2.http.QueryMap

interface ApiRepository {
    suspend fun getDataList(): List<ApiData>

    suspend fun login(loginRequest: LoginRequest): Result<BaseResponse<LoginResponse>>

    suspend fun marketList(searchModel: SearchModel): Result<BaseResponse<ArrayList<Stock>>>

    suspend fun portfolio(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>>

    suspend fun portfolioDashboard(): Result<BaseResponse<PortfolioDashboard>>

    suspend fun usersDashboard(): Result<BaseResponse<UserDashboard>>

    suspend fun notifications(searchModel: SearchModel): Result<BaseResponse<ArrayList<Notifications>>>

}