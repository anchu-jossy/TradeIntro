package com.techxform.tradintro.feature_main.data.repository

import com.techxform.tradintro.feature_main.data.remote.dto.*
import com.techxform.tradintro.feature_main.data.remote.service.ApiService
import com.techxform.tradintro.feature_main.domain.repository.ApiRepository
import kotlinx.coroutines.*
import retrofit2.Response
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
            val response = apiService.login(loginRequest)
            if (response.isSuccessful)
                Result.Success(response.body()!!)
            else {
                Result.Error(Failure.ServerError)
            }
        }
    }


}