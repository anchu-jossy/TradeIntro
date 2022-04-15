package com.techxform.tradintro.di


import com.techxform.tradintro.data.remote.service.ApiService
import com.techxform.tradintro.data.repository.ApiDataRepositoryImpl
import com.techxform.tradintro.domain.repository.ApiRepository
import com.techxform.tradintro.core.utils.Contants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [NetworkModules::class])
@InstallIn(SingletonComponent::class)
object AppModules {

    @Provides
    @Singleton
    fun provideAPIService(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ) = provideServices(client, gsonConverterFactory, ApiService::class.java)

    @Provides
    @Singleton
    fun provideCoinRepository(apiService: ApiService): ApiRepository =
        ApiDataRepositoryImpl(apiService)

    @Provides
    @Singleton
    fun provideCoroutineDispatcher() = Dispatchers.IO

    private fun <T> provideServices(
        client: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        newClass: Class<T>
    ) =
        createRetrofit(client, gsonConverterFactory).create(newClass)

    private fun createRetrofit(client: OkHttpClient, gsonConverterFactory: GsonConverterFactory) =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(gsonConverterFactory)
            .build()
}