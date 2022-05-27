package com.techxform.tradintro.feature_main.domain.repository

import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.SearchModel

interface ApiRepository {
    suspend fun getDataList(): List<ApiData>

    suspend fun login(loginRequest: LoginRequest): Result<BaseResponse<LoginResponse>>

    suspend fun marketList(searchModel: SearchModel): Result<BaseResponse<ArrayList<Stock>>>

    suspend fun marketDetails(marketId: Int): Result<BaseResponse<Stock>>

    suspend fun buyStock(
        marketId: Int,
        buyStockReq: BuyStockReq
    ): Result<BaseResponse<PortfolioItem>>

    suspend fun portfolio(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>>

    suspend fun portfolioDetails(
        orderId: Int,
        filterModel: FilterModel
    ): Result<BaseResponse<PortfolioItem>>

    suspend fun portfolioDashboard(): Result<BaseResponse<PortfolioDashboard>>

    suspend fun usersDashboard(): Result<BaseResponse<UserDashboard>>

    suspend fun notifications(searchModel: SearchModel): Result<BaseResponse<ArrayList<Notifications>>>

    suspend fun watchlist(filterModel: FilterModel): Result<BaseResponse<ArrayList<WatchList>>>

    suspend fun watchlistDetail(watchlistId: Int): Result<BaseResponse<WatchList>>

    suspend fun createWatchList(createWatchListRequest: CreateWatchListRequest): Result<BaseResponse<CreateWatchListResponse>>
    suspend fun updateWatchList(id: Number, req: UpdateWatchListRequest): Result<BaseResponse<UpdateWatchListResponse>>
    suspend fun deleteWatchList(id: Number): Result<BaseResponse<DeleteWatchListResponse>>


}