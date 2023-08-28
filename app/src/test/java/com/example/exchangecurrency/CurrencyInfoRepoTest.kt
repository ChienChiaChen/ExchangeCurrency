package com.example.exchangecurrency

import com.example.exchangecurrency.DummyData.fakeCurrencyDataBaseABC
import com.example.exchangecurrency.data.local.CurrencyDao
import com.example.exchangecurrency.data.remote.CurrencyInfoApi
import com.example.exchangecurrency.data.repository.CurrencyInfoRepo
import com.example.exchangecurrency.utils.DataStoreUtils
import com.example.exchangecurrency.utils.PreferenceUtils
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkAll
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test


class CurrencyInfoRepoTest {


    private val currencyInfoApi = mockk<CurrencyInfoApi>()
    private val currencyDao = mockk<CurrencyDao>()
    private lateinit var currencyInfoRepo: CurrencyInfoRepo

    @Before
    fun setup() {
        unmockkAll()
        unmockkObject()

        mockkObject(PreferenceUtils)
        mockkObject(DataStoreUtils)
        mockkStatic(DataStoreUtils::class) {
            coEvery { DataStoreUtils.setCurrencyUpdatedTime() } just runs
            coEvery { DataStoreUtils.saveSyncLongData(any(), any()) } just runs
        }

        currencyInfoRepo = (CurrencyInfoRepo(currencyInfoApi, currencyDao))
    }

    @After
    fun shoutdown() {
        unmockkAll()
        unmockkObject()
    }

    @Test
    fun `When getLatestCurrency called and data is expired then should call getLatestCurrency from currencyInfoApi`() = runBlocking {
            // Given
            coEvery { PreferenceUtils.isDataExpired() } returns true
            coEvery { currencyDao.insert(any()) } returns 1
            coEvery {
                currencyInfoApi.getLatestCurrency(any()).body()
            } returns fakeCurrencyDataBaseABC

            // When
            currencyInfoRepo.getLatestCurrency().collectLatest {
                assert(fakeCurrencyDataBaseABC == it)
            }
            coVerify {
                currencyInfoApi.getLatestCurrency(any())
            }
        }

    @Test
    fun `When data is not expired then should use cache first`() = runBlocking {
        // Given
        coEvery { PreferenceUtils.isDataExpired() } returns true
        coEvery { currencyDao.insert(any()) } returns 1
        coEvery {
            currencyInfoApi.getLatestCurrency(any()).body()
        } returns fakeCurrencyDataBaseABC

        // When
        currencyInfoRepo.getLatestCurrency().collect()
        coEvery { PreferenceUtils.isDataExpired() } returns false
        currencyInfoRepo.getLatestCurrency().collect()

        // Then
        assert(currencyInfoRepo.getLatestCurrency().first() == fakeCurrencyDataBaseABC)
    }

    @Test
    fun `When data is not expired and cache is null then should fetch data from db`() = runBlocking {
        // Given
        coEvery { PreferenceUtils.isDataExpired() } returns false
        coEvery { currencyDao.getCurrencyInBaseCurrency(any()) } returns fakeCurrencyDataBaseABC
        // When
        currencyInfoRepo.getLatestCurrency().collect()

        // Then
        assert(currencyInfoRepo.getLatestCurrency().first() == fakeCurrencyDataBaseABC)
    }

    @Test
    fun `When cache and data in db are null then should fetch data from network`() = runBlocking {
        // Given
        coEvery { currencyDao.insert(any()) } returns 1
        coEvery { PreferenceUtils.isDataExpired() } returns false
        coEvery { currencyDao.getCurrencyInBaseCurrency(any()) } returns null
        coEvery { currencyInfoApi.getLatestCurrency(any()).body() } returns fakeCurrencyDataBaseABC
        // When
        currencyInfoRepo.getLatestCurrency().collect()

        // Then
        coVerify { currencyInfoApi.getLatestCurrency(any()) }
    }
}