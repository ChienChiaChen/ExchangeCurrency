package com.example.exchangecurrency.remote

import com.example.exchangecurrency.BuildConfig
import com.example.exchangecurrency.data.entity.CurrencyInfo
import com.example.exchangecurrency.data.remote.CurrencyInfoApi
import com.example.exchangecurrency.data.remote.interceptor.NoInternetInterceptor
import com.example.exchangecurrency.utils.enqueueResponse
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class CurrencyApiTest {

    private val mockWebService = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .addInterceptor(NoInternetInterceptor)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebService.url("/"))
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(CurrencyInfoApi::class.java)

    @After
    fun tearDown() {
        mockWebService.shutdown()
    }

    @Test
    fun `Given 200 response When fetching users Then returns currencies correctly`() {
        // Given
        mockWebService.enqueueResponse(fileName = "currencies.json") { body ->
            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        val expected = CurrencyInfo(
            base = "USD",
            timestamp = 1692871200,
            disclaimer = "Usage subject to terms: https://openexchangerates.org/terms",
            license = "https://openexchangerates.org/license",
            rates = mapOf(Pair("TWD", 31.7654f))
        )

        // When
        val actual = runBlocking { api.getLatestCurrency(BuildConfig.API_KEY) }.body()
        val request = mockWebService.takeRequest()


        // Then
        assertEquals(expected.base, actual?.base)
        assertEquals(expected.rates.size, actual?.rates?.size)
        assertEquals("/latest.json?app_id=${BuildConfig.API_KEY}", request.path)
    }

    @Test(expected = IOException::class) // then
    fun `It'll change into IOException when socket exception occurs`() {
        // Given

        mockWebService.enqueueResponse(fileName = "currencies.json") { body ->
            MockResponse()
                .setResponseCode(200)
                .setSocketPolicy(SocketPolicy.NO_RESPONSE)
                .setBody(body)
        }
        // When
        runBlocking { api.getLatestCurrency(BuildConfig.API_KEY) }.body()
    }

}