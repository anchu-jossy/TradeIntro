package com.techxform.tradintro.feature_main.data.repository

import android.util.Log
import com.google.gson.JsonParseException
import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.data.remote.service.ApiService
import com.techxform.tradintro.feature_main.domain.model.SearchModel
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import kotlinx.coroutines.*
import java.net.UnknownHostException
import javax.inject.Inject

class ApiDataRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ApiRepository {

    override suspend fun getDataList(): List<ApiData> {
        return apiService.getDataList()
    }

    override suspend fun login(loginRequest: LoginRequest): Result<BaseResponse<LoginResponse>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.login(loginRequest)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.errorBody().toString())
                    Result.Error(Failure.ServerError)
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun marketList(searchModel: SearchModel): Result<BaseResponse<ArrayList<Stock>>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "search" to searchModel.searchText,
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                    "skip" to searchModel.skip.toString()
                )

                val response = apiService.marketList(reqMap)
                if (response.isSuccessful)
                    Result.Success(response.body()!!)
                else {
                    Log.e("Error:", response.errorBody().toString())
                    Result.Error(Failure.ServerError)
                }
            } catch (e: UnknownHostException) {
                Result.Error(Failure.NetworkConnection)
            } catch (e: Exception) {
                Result.Error(Failure.ServerError)
            }
        }
    }

    override suspend fun portfolio(searchModel: SearchModel): Result<BaseResponse<ArrayList<PortfolioItem>>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "search" to searchModel.searchText,
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                    "skip" to searchModel.skip.toString()
                )

                val response = apiService.portfolio(reqMap)
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
        }
    }

    override suspend fun portfolioDashboard(): Result<BaseResponse<PortfolioDashboard>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.portfolioDashboard()
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
        }
    }

    override suspend fun usersDashboard(): Result<BaseResponse<UserDashboard>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val response = apiService.usersDashboard()
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
        }
    }

    override suspend fun notifications(searchModel: SearchModel): Result<BaseResponse<ArrayList<Notifications>>> {
        return withContext(Dispatchers.Default)
        {
            try {
                val reqMap = mapOf(
                    "search" to searchModel.searchText,
                    "limit" to searchModel.limit.toString(),
                    "offset" to searchModel.offset.toString(),
                    "skip" to searchModel.skip.toString()
                )

                val response = apiService.notifications(reqMap)
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
        }
    }


}