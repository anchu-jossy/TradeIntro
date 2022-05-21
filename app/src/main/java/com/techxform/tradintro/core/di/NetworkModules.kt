package com.techxform.tradintro.core.di

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.techxform.tradintro.BuildConfig
import com.techxform.tradintro.core.di.utils.NetworkConnectionInterceptor
import com.techxform.tradintro.core.utils.Contants.PREF_TOKEN_KEY
import com.techxform.tradintro.core.utils.PreferenceHelper
import com.techxform.tradintro.core.utils.PreferenceHelper.token
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModules {

    @Provides
    @Singleton
    fun provideOkHttp(@ApplicationContext appContext: Context): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(NetworkConnectionInterceptor(appContext))
            .addInterceptor {
                val pref = PreferenceHelper.customPreference(appContext)
                val newRequest: Request = it.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${pref.token}")
                    .build()
                return@addInterceptor it.proceed(newRequest)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonFactory(gson: Gson) = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideGson() = GsonBuilder().create()
/*
    @Provides
    @Singleton
    fun providePreference(@ApplicationContext appContext: Context) = TradSharedPreference.createGetPreference(appContext)*/
}