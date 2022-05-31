package com.techxform.tradintro.feature_main.data.remote.service

import com.techxform.tradintro.feature_main.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("v1/data")
    suspend fun getDataList(): List<ApiData>

    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>

    @GET("api/market")
    suspend fun marketList(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<Stock>>>

    @GET("api/market/{id}")
    suspend fun marketDetails(@Path("id") marketId: Int): Response<BaseResponse<Stock>>

    @POST("/api/market/{id}/buy")
    suspend fun buyStock(@Path("id") marketId: Int, @Body buyStockReq: BuyStockReq) :Response<BaseResponse<PortfolioItem>>

    @GET("api/portfolio")
    suspend fun portfolio(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<PortfolioItem>>>

    @GET("api/portfolio/{id}/")
    suspend fun portfolioDetails(
        @Path("id") marketId: Int,
        @Query("filter") reqString: String
    ): Response<BaseResponse<PortfolioItem>>

    @GET("api/portfolio/dashboard")
    suspend fun portfolioDashboard(): Response<BaseResponse<PortfolioDashboard>>

    @GET("api/users/dashboard")
    suspend fun usersDashboard(): Response<BaseResponse<UserDashboard>>

    @GET("api/notifications")
    suspend fun notifications(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<Notifications>>>

    @GET("api/watch-lists")
    suspend fun watchlist(@QueryMap reqString: Map<String, String>): Response<BaseResponse<ArrayList<WatchList>>>

    @GET("api/watch-lists/{id}")
    suspend fun watchlistDetail(@Path("id") watchlistId: Int): Response<BaseResponse<WatchList>>

    @POST("api/watch-lists")
    suspend fun createWatchList(@Body createWatchList: CreateWatchListRequest): Response<BaseResponse<WatchList>>

    @PATCH("api/watch-lists/{id}")
    suspend fun updateWatchList(@Path("id") id: Number, @Body updateWatchlistReq: UpdateWatchListRequest): Response<BaseResponse<UpdateData>>

    @DELETE("api/watch-lists/{id}")
    suspend fun deleteWatchList(@Path("id") id: Number): Response<BaseResponse<DeleteWatchListResponse>>

    @GET("api/wallet/summery/")
    suspend fun getWalletSummary(@Query("voucher") name: String): Response<BaseResponse<WalletSummaryResponse>>

    @GET("api/user-levels")
    suspend fun userLevels(): Response<BaseResponse<UserLevels>>

    @GET("api/users/me")
    suspend fun userDetails(  ): Response<BaseResponse<UserDetailsResponse>>
}