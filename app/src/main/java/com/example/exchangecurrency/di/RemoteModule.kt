package com.example.exchangecurrency.di

import com.example.exchangecurrency.data.remote.CurrencyInfoApi
import com.example.exchangecurrency.data.remote.interceptor.NoInternetInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class RemoteModule {
    companion object {
        const val HTTP_TIMEOUT: Long = 30
        const val BASE_URL: String = "https://openexchangerates.org/api/"
    }
    private fun makeLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @[Provides Singleton]
    fun makeOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(NoInternetInterceptor)
            .addInterceptor(makeLoggingInterceptor())
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .build()
    }

    @[Provides Singleton]
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().apply {
            client(okHttpClient)
            addConverterFactory(GsonConverterFactory.create())
            baseUrl(BASE_URL)
        }.build()
    }

    @[Provides Singleton]
    fun provideCurrencyDataApi(retrofit: Retrofit): CurrencyInfoApi {
        return retrofit.create(CurrencyInfoApi::class.java)
    }
}