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
    suspend fun marketList(@QueryMap reqMap:Map<String,String>) : Response<BaseResponse<ArrayList<Stock>>>

    @GET("api/market/{id}")
    suspend fun marketDetails(@Path("id") marketId:Int) : Response<BaseResponse<Stock>>

    @GET("api/portfolio")
    suspend fun portfolio(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<PortfolioItem>>>

    @GET("api/portfolio/{id}/")
    suspend fun portfolioDetails(@Path("id") marketId:Int, @Query("filter") reqString: String): Response<BaseResponse<PortfolioItem>>

    @GET("api/portfolio/dashboard")
    suspend fun portfolioDashboard(): Response<BaseResponse<PortfolioDashboard>>

    @GET("api/users/dashboard")
    suspend fun usersDashboard(): Response<BaseResponse<UserDashboard>>

    @GET("api/notifications")
    suspend fun notifications(@QueryMap reqMap: Map<String, String>): Response<BaseResponse<ArrayList<Notifications>>>


}