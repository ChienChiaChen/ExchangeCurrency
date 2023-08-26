package com.example.exchangecurrency.data.remote

import com.example.exchangecurrency.data.entity.CurrencyInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyInfoApi {
    @GET("latest.json")
    suspend fun getLatestCurrency(@Query("app_id") appId: String): Response<CurrencyInfo>
}