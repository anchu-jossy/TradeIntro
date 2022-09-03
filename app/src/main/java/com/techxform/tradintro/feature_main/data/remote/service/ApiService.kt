package com.techxform.tradintro.feature_main.data.remote.service


import com.techxform.tradintro.feature_main.data.remote.FcmTokenRegReq
import com.techxform.tradintro.feature_main.data.remote.dto.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("v1/data")
    suspend fun getDataList(): List<ApiData>

    @POST("api/users/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>

    @GET("api/market")
    suspend fun marketList(@QueryMap reqMap: Map<String, String?>): Response<BaseResponse<ArrayList<Stock>>>

    @GET("api/market/{id}")
    suspend fun marketDetails(@Path("id") marketId: Int): Response<BaseResponse<Stock>>

    @POST("/api/market/{id}/buy")
    suspend fun buyStock(
        @Path("id") marketId: Int,
        @Body buySellStockReq: BuySellStockReq
    ): Response<BaseResponse<PortfolioItem>>

    @POST("/api/market/{id}/sell")
    suspend fun sellStock(
        @Path("id") marketId: Int,
        @Body buySellStockReq: BuySellStockReq
    ): Response<BaseResponse<PortfolioItem>>

    @GET("api/portfolio")
    suspend fun portfolio(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<PortfolioItem>>>

    @GET("api/v2/portfolio")
    suspend fun portfolioV2(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<PortfolioItem>>>

    @GET("api/v2/portfolio/dashboard")
    suspend fun portfolioDashboardV2(): Response<BaseResponse<PortfolioDashboard>>

    @GET("api/v2/portfolio/{stockId}/dashboard")
    suspend fun portfolioDashboardOfStockV2(
        @Path("stockId") stockId: Int,
    ): Response<BaseResponse<StockDashboard>>

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
    suspend fun notifications(@QueryMap reqMap: Map<String, String?>): Response<BaseResponse<ArrayList<Notifications>>>

    @DELETE("api/notifications/{id}")
    suspend fun notifications(@Path("id") notificationId: Int): Response<BaseResponse<DeleteNotificationResponse>>

    @GET("api/watch-lists")
    suspend fun watchlist(@QueryMap reqString: Map<String, String>): Response<BaseResponse<ArrayList<WatchList>>>

    @GET("api/watch-lists/{id}")
    suspend fun watchlistDetail(@Path("id") watchlistId: Int): Response<BaseResponse<WatchList>>

    @POST("api/watch-lists")
    suspend fun createWatchList(@Body createWatchList: CreateWatchListRequest): Response<BaseResponse<WatchList>>

    @PATCH("api/watch-lists/{id}")
    suspend fun updateWatchList(
        @Path("id") id: Number,
        @Body updateWatchlistReq: UpdateWatchListRequest
    ): Response<BaseResponse<UpdateData>>

    @DELETE("api/watch-lists/{id}")
    suspend fun deleteWatchList(@Path("id") id: Number): Response<BaseResponse<DeleteWatchListResponse>>

    @GET("api/wallet/summery/")
    suspend fun getWalletSummary(): Response<BaseResponse<WalletSummaryResponse>>

    @GET("api/user-levels")
    suspend fun userLevels(): Response<BaseResponse<UserLevels>>

    @GET("api/users/me")
    suspend fun userDetails(): Response<BaseResponse<UserDetailsResponse>>

    @PATCH("api/notifications/read/{id}")
    suspend fun readNotification(@Path("id") id: Int): Response<BaseResponse<Int>>

    @GET("api/wallet/history")
    suspend fun walletHistory(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<WalletHistory>>>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST
    @FormUrlEncoded
    suspend fun updateWallet(
        @Url url: String,
        @FieldMap reqMap: Map<String, String>
    ): Response<UpdateWalletResponse>

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST
    @FormUrlEncoded
    suspend fun addUser(
        @Url url: String,
        @FieldMap reqMap: Map<String, String>
    ): Response<AddUserResponse>


    @POST("api/users/logout")
    suspend fun logOut(@Body request: LogOutRequest): Response<BaseResponse<Any>>

    @GET("api/users/invites/history")
    suspend fun userInviteList(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<InviteData>>>

    @POST("api/users/fcm/token")
    suspend fun fcmTokenRegistration(@Body request: FcmTokenRegReq) : Response<Any>

    @POST
    @FormUrlEncoded
    suspend fun forgetPassword(@Url url: String, @FieldMap reqMap: Map<String, String>) : Response<Any>

    @POST
    @FormUrlEncoded
    suspend fun register(@Url url: String, @FieldMap request: Map<String, String>): Response<BaseResponse<Any>>

    @GET("api/reports/historical")
    suspend fun historicalReport(@QueryMap reqMap: Map<String, String>) : Response<BaseResponse<ArrayList<PortfolioItem>>>

    @GET("api/reports/current")
    suspend fun reportCurrent(@QueryMap reqMap: Map<String, String>) : Response<BaseResponse<ArrayList<PortfolioItem>>>

    @GET("api/reports/profit-loss/summery")
    suspend fun summaryReport() : Response<BaseResponse<SummaryReport>>

    @POST("api/portfolio/{id}/alert")
    suspend fun alertPrice(@Path("id") id: Int, @Body  alertPriceRequest: AlertPriceRequest) : Response<BaseResponse<AlertPriceResponse>>

    @POST("api/watch-lists/{id}/alert")
    suspend fun alertPriceWL(@Path("id") id: Int, @Body  alertPriceRequest: AlertPriceRequest) : Response<BaseResponse<AlertPriceResponse>>

    @PATCH("api/users/me")
    @Multipart
    suspend fun editProfile(@PartMap reqMap: Map<String, String>, @Part image: MultipartBody.Part?) : Response<BaseResponse<UserDetailsResponse>>


    @DELETE("api/users/me")
    suspend fun deleteProfile() : Response<BaseResponse<Any>>

    @PATCH("api/portfolio/{id}/")
    suspend fun updatePortfolio(@Path("id") id: Int,
        @Body updatePortFolioRequest : UpdatePortfolioRequest):Response<BaseResponse<PortfolioItem>>

    @DELETE("api/portfolio/{id}/")
    suspend fun deletePortfolio(@Path("id") id: Int) : Response<BaseResponse<Any>>

    @POST("api/users/change-password")
    suspend fun changePassword(@Body reqBody:ChangePasswordRequest) : Response<BaseResponse<LoginResponse>>
}