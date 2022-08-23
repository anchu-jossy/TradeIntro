package com.techxform.tradintro.feature_main.data.repository

import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.techxform.tradintro.core.utils.Contants.ADD_USER_URL
import com.techxform.tradintro.core.utils.Contants.FORGOT_PASSWORD
import com.techxform.tradintro.core.utils.Contants.RECHARGE_URL
import com.techxform.tradintro.core.utils.Contants.REGISTER
import com.techxform.tradintro.core.utils.UserDetailsSingleton
import com.techxform.tradintro.feature_main.data.remote.FcmTokenRegReq
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.data.remote.service.ApiService
import com.techxform.tradintro.feature_main.domain.model.FilterModel
import com.techxform.tradintro.feature_main.domain.model.PaymentType
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

class ApiDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ApiRepository {

    override suspend fun getDataList(): List<ApiData> {
        return apiService.getDataList()
    }

    private suspend fun <T> apiCall(apiCall: Response<BaseResponse<T>>): Result<BaseResponse<T>> {
        return withContext(Dispatchers.Default)
        {
            try {
                if (apiCall.isSuccessful) {
                    Result.Success(apiCall.body()!!)
                } else {
                    val gson = Gson()
                    val errorResponse =
                        gson.fromJson(apiCall.errorBody()!!.string(), ErrorResponse::class.java)
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))
                }
            }catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            }catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun login(loginRequest: LoginRequest): Result<BaseResponse<LoginResponse>> {
       return apiCall(apiService.login(loginRequest))

        /*return withContext(Dispatchers.Default)
        {
            try {
                apiCall(apiService.login(loginRequest))
                val response = apiService.login(loginRequest)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(response.errorBody()!!.string(),ErrorResponse::class.java )
                    Result.Error(Failure.FeatureFailure(errorResponse.error.message))
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun marketList(searchModel: SearchModel): Result<BaseResponse<ArrayList<Stock>>> {
        val reqMap = mutableMapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
            "skip" to searchModel.skip.toString()
        )

        if (!searchModel.searchText.isNullOrEmpty())
            reqMap["search"] = searchModel.searchText!!

        return apiCall(apiService.marketList(reqMap))

       /* return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mutableMapOf(
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                    "skip" to searchModel.skip.toString()
                )

                if (!searchModel.searchText.isNullOrEmpty())
                    reqMap["search"] = searchModel.searchText!!

                val response = apiService.marketList(reqMap)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.raw().message)
                    Result.Error(Failure.FeatureFailure(response.raw().message))
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun marketDetails(marketId: Int): Result<BaseResponse<Stock>> {

        return apiCall(apiService.marketDetails(marketId))

       /* return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.marketDetails(marketId)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.raw().message)
                    Result.Error(Failure.FeatureFailure(response.raw().message))
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun buyStock(
        marketId: Int,
        buySellStockReq: BuySellStockReq
    ): Result<BaseResponse<PortfolioItem>> {

        return apiCall(apiService.buyStock(marketId, buySellStockReq))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.buyStock(marketId, buySellStockReq)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error123:", response.errorBody().toString())
                    Result.Error(Failure.FeatureFailure(JSONObject(response.errorBody()?.string()).getString("message")))
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun sellStock(
        marketId: Int,
        buySellStockReq: BuySellStockReq
    ): Result<BaseResponse<PortfolioItem>> {
        return apiCall(apiService.sellStock(marketId, buySellStockReq))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.sellStock(marketId, buySellStockReq)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun portfolio(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        var reqMap = mutableMapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
            "skip" to searchModel.skip.toString(),
            "order_execution_type" to searchModel.orderExecutionType,
            "order_status" to searchModel.orderStatus,
            "portfolio_status" to searchModel.portfolioStatus
        )
        if (!searchModel.searchText.isNullOrEmpty())
            reqMap["search"] = searchModel.searchText!!

        return apiCall(apiService.portfolio(reqMap))


       /* return withContext(Dispatchers.Default)
        {
            try {
                var reqMap = mutableMapOf(
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                    "skip" to searchModel.skip.toString(),
                    "order_execution_type" to searchModel.orderExecutionType,
                    "order_status" to searchModel.orderStatus,
                    "portfolio_status" to searchModel.portfolioStatus
                )
                if (!searchModel.searchText.isNullOrEmpty())
                    reqMap["search"] = searchModel.searchText!!

                val response = apiService.portfolio(reqMap)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }
    override suspend fun updatePortfolio(id:Int,updatePortFolioReq: UpdatePortfolioRequest): Result<BaseResponse<PortfolioItem>> {

        return apiCall(apiService.updatePortfolio(id,updatePortFolioReq))



        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.updatePortfolio(id,updatePortFolioReq)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun deletePortfolio(id: Int): Result<BaseResponse<Any>> {

        return apiCall(apiService.deletePortfolio(id))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.deletePortfolio(id)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun portfolioDetails(
        orderId: Int,
        filterModel: FilterModel
    ): Result<BaseResponse<PortfolioItem>> {

        return apiCall(apiService.portfolioDetails(orderId, ""))


        /*return withContext(Dispatchers.Default)
        {
            try {

                *//* val reqMap = mapOf(
                    "search" to searchModel.searchText,
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                    "skip" to searchModel.skip.toString()
                )*//*

                val response = apiService.portfolioDetails(orderId, "")
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun portfolioDashboard(): Result<BaseResponse<PortfolioDashboard>> {
        return apiCall(apiService.portfolioDashboard())

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.portfolioDashboard()
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun usersDashboard(): Result<BaseResponse<UserDashboard>> {
        return apiCall(apiService.usersDashboard())


        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.usersDashboard()
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
                Result.Error(Failure.ServerError)
            }
        }*/
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

        return apiCall(apiService.notifications(reqMap))

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
        return apiCall(apiService.notifications(notificationsId))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.notifications(notificationsId)
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

    override suspend fun watchlist(filterModel: FilterModel): Result<BaseResponse<ArrayList<WatchList>>> {

        val reqMap = mutableMapOf(
            "limit" to filterModel.limit.toString(),
            "offset" to filterModel.offset.toString(),
            "skip" to filterModel.skip.toString()
        )

        if (!filterModel.searchText.isNullOrEmpty())
            reqMap["search"] = filterModel.searchText!!

        return apiCall(apiService.watchlist(reqMap))

        /*return withContext(Dispatchers.Default)
        {
            try {
                //  val reqString = Gson().toJson(filterModel)
                val reqMap = mutableMapOf(
                    "limit" to filterModel.limit.toString(),
                    "offset" to filterModel.offset.toString(),
                    "skip" to filterModel.skip.toString()
                )

                if (!filterModel.searchText.isNullOrEmpty())
                    reqMap["search"] = filterModel.searchText!!

                val response = apiService.watchlist(reqMap)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.errorBody().toString())
                    Result.Error(Failure.ServerError)
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun watchlistDetail(watchlistId: Int): Result<BaseResponse<WatchList>> {
        return apiCall(apiService.watchlistDetail(watchlistId))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.watchlistDetail(watchlistId)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.errorBody().toString())
                    Result.Error(Failure.ServerError)
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun createWatchList(createWatchListRequest: CreateWatchListRequest): Result<BaseResponse<WatchList>> {
        return apiCall(apiService.createWatchList(createWatchListRequest))

       /* return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.createWatchList(createWatchListRequest)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.errorBody().toString())
                    Result.Error(Failure.ServerError)
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun updateWatchList(
        id: Number,
        req: UpdateWatchListRequest
    ): Result<BaseResponse<UpdateData>> {
        return apiCall(apiService.updateWatchList(id, req))

/*
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.updateWatchList(id, req)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.errorBody().toString())
                    Result.Error(Failure.ServerError)
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun deleteWatchList(id: Number): Result<BaseResponse<DeleteWatchListResponse>> {
        return apiCall(apiService.deleteWatchList(id))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.deleteWatchList(id)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.errorBody().toString())
                    Result.Error(Failure.ServerError)
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: JsonParseException) {
                Result.Error(Failure.JsonParsing)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun walletSummary(type: PaymentType): Result<BaseResponse<WalletSummaryResponse>> {
        return apiCall(apiService.getWalletSummary())
        /*return try {
            val response = apiService.getWalletSummary()
            if (response.isSuccessful)
                Result.Success(response.body()!!)
            else {
                Log.e("Error:", response.errorBody().toString())
                Result.Error(Failure.ServerError)
            }
        } catch (e: UnknownHostException) {
            Result.Error(Failure.NetworkConnection)
        } catch (e: JsonParseException) {
            Result.Error(Failure.JsonParsing)
        } catch (e: Exception) {
            Result.Error(Failure.ServerError)
        }*/
    }

    override suspend fun userLevels(): Result<BaseResponse<UserLevels>> {
        return apiCall(apiService.userLevels())

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.userLevels()
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun userDetails(): Result<BaseResponse<UserDetailsResponse>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.userDetails()
                if (response.isSuccessful) {
                    UserDetailsSingleton.setUserDetails(response.body()!!.data)
                    Result.Success(response.body()!!)
                }else {
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
       return apiCall(apiService.readNotification(id))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.readNotification(id)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun walletHistory(searchModel: SearchModel): Result<BaseResponse<ArrayList<WalletHistory>>> {
        val reqMap = mapOf(
            "type" to searchModel.type,
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString()
        )

        return apiCall(apiService.walletHistory(reqMap))
        /*return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "type" to searchModel.type,
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString()
                )
                val response = apiService.walletHistory(reqMap)
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
                Result.Error(Failure.ServerError)
            }
        }*/
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
        return apiCall(apiService.logOut(loginRequest))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.logOut(loginRequest)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.raw().message)
                    Result.Error(Failure.FeatureFailure(response.raw().message))
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }*/

    }


    override suspend fun findUserInviteList(): Result<BaseResponse<ArrayList<InviteData>>> {
        val reqMap = mapOf(
            "10" to "limit",
            "0" to "offset",
        )
        return apiCall(apiService.userInviteList(reqMap = reqMap))

       /* return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "10" to "limit",
                    "0" to "offset",
                )
                val response = apiService.userInviteList(reqMap = reqMap)
                if (response.isSuccessful) {
                    Result.Success(response.body()!!)
                } else {
                    Log.e("Error:", response.raw().message)
                    Result.Error(Failure.FeatureFailure(response.raw().message))
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
        }*/
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


    override suspend fun register(request: RegisterRequest): Result<BaseResponse<Any>> {

        val reqMap = mapOf(
            "first_name" to request.firstName,
            "last_name" to (request.lastName ?: ""),
            "email" to request.email,
            "password" to request.password,
        )

        return apiCall(apiService.register(REGISTER, reqMap))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "first_name" to request.firstName,
                    "last_name" to (request.lastName ?: ""),
                    "email" to request.email,
                    "password" to request.password,
                )
                val response = apiService.register(REGISTER, reqMap)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.raw().message)
                    Result.Error(Failure.FeatureFailure(response.raw().message))
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
        }*/
    }

    override suspend fun historicalReport(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        val reqMap = mapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
        )

        return apiCall(apiService.historicalReport(reqMap))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                )

                val response = apiService.historicalReport(reqMap)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun reportCurrent(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        val reqMap = mapOf(
            "limit" to searchModel.limit.toString(),
            "offset" to searchModel.offset.toString(),
        )

        return apiCall(apiService.reportCurrent(reqMap))

        /*return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                )

                val response = apiService.reportCurrent(reqMap)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun summaryReport(): Result<BaseResponse<SummaryReport>> {
        return apiCall(apiService.summaryReport())

        /*return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.summaryReport()
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun alertPrice(
        id: Int,
        alertPriceRequest: AlertPriceRequest
    ): Result<BaseResponse<AlertPriceResponse>> {

        return apiCall(apiService.alertPrice(id, alertPriceRequest))
       /* return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.alertPrice(id, alertPriceRequest)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }

    override suspend fun alertPriceWL(
        id: Int,
        alertPriceRequest: AlertPriceRequest
    ): Result<BaseResponse<AlertPriceResponse>> {
       return apiCall(apiService.alertPriceWL(id, alertPriceRequest))

       /* return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.alertPriceWL(id, alertPriceRequest)
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }


    override suspend fun editProfile(editUserProfileReq: EditUserProfileReq): Result<BaseResponse<UserDetailsResponse>> {
        val reqMap = hashMapOf<String, String>()
        editUserProfileReq.let {
            if (it.image?.isNotEmpty() == true)
                reqMap["image"] = it.image!!
            if (it.userName?.isNotEmpty() == true)
                reqMap["user_name"] = it.userName!!
            if (it.lastName?.isNotEmpty() == true)
                reqMap["user_last_name"] = it.lastName!!
            if (it.userPhone?.isNotEmpty() == true)
                reqMap["user_phone"] = it.userPhone!!
        }

        return apiCall(apiService.editProfile(reqMap))

        /*return withContext(Dispatchers.Default)
        {

            try {
                val reqMap = hashMapOf<String, String>()
                editUserProfileReq?.apply {
                    if (this.image?.isNotEmpty() == true)
                        reqMap["image"] = this.image!!
                    if (this.userName?.isNotEmpty() == true)
                        reqMap["user_name"] = this.userName!!
                    if (this.lastName?.isNotEmpty() == true)
                        reqMap["user_last_name"] = this.lastName!!
                    if (this.userPhone?.isNotEmpty() == true)
                        reqMap["user_phone"] = this.userPhone!!
                }
                val response = apiService.editProfile(reqMap)

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
                Result.Error(Failure.ServerError)
            }


        }*/
    }

    override suspend fun deleteProfile(): Result<BaseResponse<Any>> {
        return apiCall(apiService.deleteProfile())
        /*
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.deleteProfile()
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
                Result.Error(Failure.ServerError)
            }
        }*/
    }
}




