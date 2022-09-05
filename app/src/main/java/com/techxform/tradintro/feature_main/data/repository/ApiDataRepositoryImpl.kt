package com.techxform.tradintro.feature_main.data.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.techxform.tradintro.core.utils.Contants.ADD_USER_URL
import com.techxform.tradintro.core.utils.Contants.FORGOT_PASSWORD
import com.techxform.tradintro.core.utils.Contants.RECHARGE_URL
import com.techxform.tradintro.core.utils.Contants.REGISTER
import com.techxform.tradintro.core.utils.Contants.RESENT_PASSWORD
import com.techxform.tradintro.core.utils.UserDetailsSingleton
import com.techxform.tradintro.feature_main.data.remote.FcmTokenRegReq
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.data.remote.service.ApiService
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.net.UnknownHostException
import javax.inject.Inject


class ApiDataRepositoryImpl @Inject constructor(
    @ApplicationContext val context:Context,
    private val apiService: ApiService
) : ApiRepository {

    override suspend fun getDataList(): List<ApiData> {
        return apiService.getDataList()
    }

    private suspend fun <T> apiCall(apiCall: suspend () -> Response<BaseResponse<T>>): Result<BaseResponse<T>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiCall()
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    response.body()?.let {
                        Result.Error(Failure.FeatureFailure(it.error!!.message))
                    }?:run{
                        val gson = Gson()
                        val errorResponse =
                            gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                        Result.Error(Failure.FeatureFailure(errorResponse.error.message))
                    }

                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun login(loginRequest: LoginRequest): Result<BaseResponse<LoginResponse>> {
        return apiCall{apiService.login(loginRequest)}


    }

    override suspend fun marketList(searchModel: SearchModel): Result<BaseResponse<ArrayList<Stock>>> {
        val reqMap = mutableMapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
            "skip" to searchModel.skip.toString()
        )

        if (!searchModel.searchText.isNullOrEmpty())
            reqMap["search"] = searchModel.searchText!!

        return apiCall{apiService.marketList(reqMap)}


    }

    override suspend fun marketDetails(marketId: Int): Result<BaseResponse<Stock>> {

        return apiCall{apiService.marketDetails(marketId)}


    }

    override suspend fun buyStock(
        marketId: Int,
        buySellStockReq: BuySellStockReq
    ): Result<BaseResponse<PortfolioItem>> {

        return apiCall{apiService.buyStock(marketId, buySellStockReq)}


    }

    override suspend fun sellStock(
        marketId: Int,
        buySellStockReq: BuySellStockReq
    ): Result<BaseResponse<PortfolioItem>> {
        return apiCall{apiService.sellStock(marketId, buySellStockReq)}

    }

    override suspend fun portfolio(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        val reqMap = mutableMapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
            "skip" to searchModel.skip.toString(),
            "order_execution_type" to searchModel.orderExecutionType,
            "order_status" to searchModel.orderStatus,
            "portfolio_status" to searchModel.portfolioStatus
        )
        if (!searchModel.searchText.isNullOrEmpty())
            reqMap["search"] = searchModel.searchText!!

        return apiCall{apiService.portfolio(reqMap)}

    }

    override suspend fun portfolioV2(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        val reqMap = mutableMapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
            "skip" to searchModel.skip.toString(),
            "order_execution_type" to searchModel.orderExecutionType,

            "order_status" to searchModel.orderStatus,
            "portfolio_status" to searchModel.portfolioStatus
        )
        if (!searchModel.searchText.isNullOrEmpty())
            reqMap["search"] = searchModel.searchText!!
        if (!searchModel.stockId.isNullOrEmpty())
            reqMap["stockId"] = searchModel.stockId!!

        return apiCall{apiService.portfolioV2(reqMap)}

    }

    override suspend fun updatePortfolio(
        id: Int,
        updatePortFolioReq: UpdatePortfolioRequest
    ): Result<BaseResponse<PortfolioItem>> {

        return apiCall{apiService.updatePortfolio(id, updatePortFolioReq)}
    }

    override suspend fun deletePortfolio(id: Int): Result<BaseResponse<Any>> {

        return apiCall{apiService.deletePortfolio(id)}
    }

    override suspend fun portfolioDetails(
        orderId: Int,
        filterModel: FilterModel
    ): Result<BaseResponse<PortfolioItem>> {
        return apiCall{apiService.portfolioDetails(orderId, "")}
    }

    override suspend fun portfolioDashboard(): Result<BaseResponse<PortfolioDashboard>> {
        return apiCall{apiService.portfolioDashboard()}
    }

    override suspend fun portfolioDashboardV2(): Result<BaseResponse<PortfolioDashboard>> {
        return apiCall{apiService.portfolioDashboardV2()}
    }

    override suspend fun portfolioDashboardOfStockV2(stockId: Int): Result<BaseResponse<StockDashboard>> {
        return apiCall{apiService.portfolioDashboardOfStockV2(stockId)}
    }

    override suspend fun usersDashboard(): Result<BaseResponse<UserDashboard>> {
        return apiCall{apiService.usersDashboard()}
    }

    override suspend fun notifications(searchModel: SearchModel): Result<BaseResponse<ArrayList<Notifications>>> {
        val reqMap = mutableMapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
            "skip" to searchModel.skip.toString(),
            "type" to searchModel.type.toString(),
        )

        if (!searchModel.searchText.isNullOrEmpty())
            reqMap["search"] = searchModel.searchText!!

        return apiCall{apiService.notifications(reqMap)}

        /*return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mutableMapOf(
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                    "skip" to searchModel.skip.toString(),
                    "type" to searchModel.type.toString(),
                )

                if (!searchModel.searchText.isNullOrEmpty())
                    reqMap["search"] = searchModel.searchText!!

                val response = apiService.notifications(reqMap)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.raw().message)
                    Result.Error(Failure.FeatureFailure(response.raw().message))
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun deleteNotification(notificationsId: Int): Result<BaseResponse<DeleteNotificationResponse>> {
        return apiCall{apiService.notifications(notificationsId)}
    }

    override suspend fun watchlist(filterModel: FilterModel): Result<BaseResponse<ArrayList<WatchList>>> {

        val reqMap = mutableMapOf(
            "limit" to filterModel.limit.toString(),
            "offset" to filterModel.offset.toString(),
            "skip" to filterModel.skip.toString()
        )

        if (filterModel.searchText.isNotEmpty())
            reqMap["search"] = filterModel.searchText!!

        return apiCall{apiService.watchlist(reqMap)}


    }

    override suspend fun watchlistDetail(watchlistId: Int): Result<BaseResponse<WatchList>> {
        return apiCall{apiService.watchlistDetail(watchlistId)}


    }

    override suspend fun createWatchList(createWatchListRequest: CreateWatchListRequest): Result<BaseResponse<WatchList>> {
        return apiCall{apiService.createWatchList(createWatchListRequest)}


    }

    override suspend fun updateWatchList(
        id: Number,
        req: UpdateWatchListRequest
    ): Result<BaseResponse<UpdateData>> {
        return apiCall{apiService.updateWatchList(id, req)}

    }

    override suspend fun deleteWatchList(id: Number): Result<BaseResponse<DeleteWatchListResponse>> {
        return apiCall{apiService.deleteWatchList(id)}

    }

    override suspend fun walletSummary(type: PaymentType): Result<BaseResponse<WalletSummaryResponse>> {
        return apiCall{apiService.getWalletSummary()}

    }

    override suspend fun userLevels(): Result<BaseResponse<UserLevels>> {
        return apiCall{apiService.userLevels()}

    }

    override suspend fun userDetails(): Result<BaseResponse<UserDetailsResponse>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.userDetails()
                if (response.isSuccessful) {
                    UserDetailsSingleton.setUserDetails(response.body()!!.data)
                    Result.Success(response.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))

                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun readNotification(id: Int): Result<BaseResponse<Int>> {
        return apiCall{apiService.readNotification(id)}

    }

    override suspend fun walletHistory(searchModel: SearchModel): Result<BaseResponse<ArrayList<WalletHistory>>> {
        val reqMap = mapOf(
            "type" to searchModel.type,
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString()
        )

        return apiCall{apiService.walletHistory(reqMap)}

    }


    override suspend fun updateWallet(updateWalletRequest: UpdateWalletRequest): Result<UpdateWalletResponse> {
        return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "user_id" to updateWalletRequest.userId.toString(),
                    "gst_amount" to updateWalletRequest.gstAmount.toString(),
                    "other_charge_amount" to updateWalletRequest.otherRechargeAmount.toString(),
                    "recharge_amount" to updateWalletRequest.rechargeAmount.toString(),
                    "total_amount" to updateWalletRequest.totalAmount.toString()
                )
                val response = apiService.updateWallet(RECHARGE_URL, reqMap = reqMap)
                if (response.isSuccessful) {
                    val walletResponse = response.body()!!
                    if (walletResponse.status.equals("ERROR")) {
                        Result.Error(Failure.FeatureFailure(walletResponse.reason!!))
                    } else
                        Result.Success(response.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))

                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                e.printStackTrace()
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun addUser(addUserRequest: AddUserRequest): Result<AddUserResponse> {
        return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "user_id" to addUserRequest.userId.toString(),
                    "name" to addUserRequest.name.toString(),
                    "email" to addUserRequest.email.toString(),
                    )
                val response = apiService.addUser(ADD_USER_URL, reqMap = reqMap)
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))
                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                e.printStackTrace()
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun logOut(loginRequest: LogOutRequest): Result<BaseResponse<Any>> {
        return apiCall{apiService.logOut(loginRequest)}


    }


    override suspend fun findUserInviteList(): Result<BaseResponse<ArrayList<InviteData>>> {
        val reqMap = mapOf(
            "10" to "limit",
            "0" to "offset",
        )
        return apiCall{apiService.userInviteList(reqMap = reqMap)}

    }


    override suspend fun fcmTokenRegistration(request: FcmTokenRegReq): Result<Any> {
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.fcmTokenRegistration(request)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))

                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                e.printStackTrace()
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun forgetPassword(emailId: String): Result<Any> {
        return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "forgot_email" to emailId
                )
                val response = apiService.forgetPassword(FORGOT_PASSWORD, reqMap)
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))

                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                e.printStackTrace()
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(Failure.ServerError)
            }
        }
    }


    override suspend fun resendEmail(emailId: String): Result<Any> {
        return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "email" to emailId
                )
                val response = apiService.resendEmail(RESENT_PASSWORD, reqMap)
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(response.errorBody()!!.string(), ErrorResponse::class.java)
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))

                }
            } catch (e: UnknownHostException) {
                e.printStackTrace()
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                e.printStackTrace()
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                e.printStackTrace()
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun register(request: RegisterRequest): Result<BaseResponse<Any>> {

        val reqMap = mapOf(
            "first_name" to request.firstName,
            "last_name" to (request.lastName ?: ""),
            "email" to request.email,
            "password" to request.password,
        )

        return apiCall{apiService.register(REGISTER, reqMap)}


    }

    override suspend fun historicalReport(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        val reqMap = mapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
        )

        return apiCall{apiService.historicalReport(reqMap)}

    }

    override suspend fun reportCurrent(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        val reqMap = mapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
        )

        return apiCall{apiService.reportCurrent(reqMap)}

    }

    override suspend fun summaryReport(): Result<BaseResponse<SummaryReport>> {
        return apiCall{apiService.summaryReport()}

    }

    override suspend fun alertPrice(
        id: Int,
        alertPriceRequest: AlertPriceRequest
    ): Result<BaseResponse<AlertPriceResponse>> {

        return apiCall{apiService.alertPrice(id, alertPriceRequest)}

    }

    override suspend fun alertPriceWL(
        id: Int,
        alertPriceRequest: AlertPriceRequest
    ): Result<BaseResponse<AlertPriceResponse>> {
        return apiCall{apiService.alertPriceWL(id, alertPriceRequest)}

    }


    override suspend fun editProfile(editUserProfileReq: EditUserProfileReq): Result<BaseResponse<UserDetailsResponse>> {
        val reqMap = hashMapOf<String, String>()
        editUserProfileReq.let {

            if (it.userName?.isNotEmpty() == true)
                reqMap["user_name"] = it.userName!!
            if (it.lastName?.isNotEmpty() == true)
                reqMap["user_last_name"] = it.lastName!!
            if (it.userPhone?.isNotEmpty() == true)
                reqMap["user_phone"] = it.userPhone!!
        }

        var filePart :MultipartBody.Part? = null
        if(editUserProfileReq.image != null) {
            filePart = MultipartBody.Part.createFormData(
                "image",
                editUserProfileReq.image?.name,
                //RequestBody.create("image/*".toMediaTypeOrNull(), editUserProfileReq.image!!)
                editUserProfileReq.image!!.asRequestBody("image/*".toMediaTypeOrNull())
            )
        }


        val multipartBody = editUserProfileReq.image?.asRequestBody()?.let {
            MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("firstName", "")
                .addFormDataPart("avatarUrl", editUserProfileReq.image?.name, it)
                .build()
        }
        return apiCall{apiService.editProfile(reqMap,filePart)}

    }

    override suspend fun deleteProfile(): Result<BaseResponse<Any>> {
        return apiCall{apiService.deleteProfile()}

    }

    override suspend fun changePassword(reqBody: ChangePasswordRequest): Result<BaseResponse<LoginResponse>> {
        return apiCall{apiService.changePassword(reqBody)}
    }

    override suspend fun userPointsHistory(searchModel: SearchModel): Result<BaseResponse<ArrayList<Level>>> {
        var reqMap = mutableMapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),

        )

        return apiCall{apiService.userLevelHistory(reqMap)}    }
}




