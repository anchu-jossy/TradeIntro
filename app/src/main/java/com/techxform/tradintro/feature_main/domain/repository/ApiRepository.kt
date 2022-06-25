package com.techxform.tradintro.feature_main.domain.repository

import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.QueryMap

interface ApiRepository {
    suspend fun getDataList(): List<ApiData>

    suspend fun login(loginRequest: LoginRequest): Result<BaseResponse<LoginResponse>>

    suspend fun marketList(searchModel: SearchModel): Result<BaseResponse<ArrayList<Stock>>>

    suspend fun marketDetails(marketId: Int): Result<BaseResponse<Stock>>

    suspend fun buyStock(
        marketId: Int,
        buySellStockReq: BuySellStockReq
    ): Result<BaseResponse<PortfolioItem>>

    suspend fun sellStock(
        marketId: Int,
        buySellStockReq: BuySellStockReq
    ): Result<BaseResponse<PortfolioItem>>

    suspend fun portfolio(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>>

    suspend fun portfolioDetails(
        orderId: Int,
        filterModel: FilterModel
    ): Result<BaseResponse<PortfolioItem>>

    suspend fun portfolioDashboard(): Result<BaseResponse<PortfolioDashboard>>

    suspend fun usersDashboard(): Result<BaseResponse<UserDashboard>>

    suspend fun notifications(searchModel: SearchModel): Result<BaseResponse<ArrayList<Notifications>>>

    suspend fun deleteNotification(notificationsId: Int): Result<BaseResponse<DeleteNotificationResponse>>

    suspend fun watchlist(filterModel: FilterModel): Result<BaseResponse<ArrayList<WatchList>>>

    suspend fun watchlistDetail(watchlistId: Int): Result<BaseResponse<WatchList>>

    suspend fun createWatchList(createWatchListRequest: CreateWatchListRequest): Result<BaseResponse<WatchList>>
    suspend fun updateWatchList(id: Number, req: UpdateWatchListRequest): Result<BaseResponse<UpdateData>>
    suspend fun deleteWatchList(id: Number): Result<BaseResponse<DeleteWatchListResponse>>

    suspend fun walletSummary(type: PaymentType): Result<BaseResponse<WalletSummaryResponse>>

    suspend fun userLevels(): Result<BaseResponse<UserLevels>>
    suspend fun userDetails(): Result<BaseResponse<UserDetailsResponse>>

    suspend fun readNotification(id:Int): Result<BaseResponse<Int>>

    suspend fun walletHistory(searchModel: SearchModel): Result<BaseResponse<WalletHistory>>

    suspend fun updateWallet(updateWalletRequest: UpdateWalletRequest) : Result<UpdateWalletResponse>
    suspend fun addUser(addUserRequest: AddUserRequest) : Result<AddUserResponse>

}