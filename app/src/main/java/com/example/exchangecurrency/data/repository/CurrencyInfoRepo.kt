package com.example.exchangecurrency.data.repository

import com.example.exchangecurrency.BuildConfig
import com.example.exchangecurrency.data.entity.CurrencyInfo
import com.example.exchangecurrency.data.local.CurrencyDao
import com.example.exchangecurrency.data.remote.CurrencyInfoApi
import com.example.exchangecurrency.utils.Constants.BASE_CURRENCY_USD
import com.example.exchangecurrency.utils.DataStoreUtils
import com.example.exchangecurrency.utils.PreferenceUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyInfoRepo @Inject constructor(
    private val currencyInfoApi: CurrencyInfoApi,
    private val currencyDao: CurrencyDao,
) {
    var cacheCurrencyInfo: CurrencyInfo? = null

    fun getLatestCurrency(): Flow<CurrencyInfo> {
        // Check expired time
        // if it's expired , fetch data from memory or db.
        // otherwise, fetching data from server.
        return flow {
            if (PreferenceUtils.isDataExpired()) {
                emit(getCurrencyFromNetwork())
            } else {
                if (cacheCurrencyInfo != null) {
                    emit(cacheCurrencyInfo!!)
                    return@flow
                }
                val currencyFromDB = getCurrencyFromDB()
                if (currencyFromDB != null) {
                    cacheCurrencyInfo = currencyFromDB
                    emit(currencyFromDB)
                } else {
                    emit(getCurrencyFromNetwork())
                }

            }
        }
    }

    private suspend fun updateCurrencyInfo(currencyData: CurrencyInfo) =
        currencyDao.insert(currencyData)

    private suspend fun getCurrencyFromDB(): CurrencyInfo? {
        return currencyDao.getCurrencyInBaseCurrency(baseCurrency = BASE_CURRENCY_USD)
    }

    private suspend fun getCurrencyFromNetwork(appId: String = BuildConfig.API_KEY): CurrencyInfo {
        val currencyInfo = currencyInfoApi.getLatestCurrency(appId = appId).body()
        return if (currencyInfo != null) {
            cacheCurrencyInfo = currencyInfo // memory
            saveDataAndTimestamp(currencyInfo) // db
            currencyInfo
        } else {
            CurrencyInfo()
        }
    }

    private suspend fun saveDataAndTimestamp(data: CurrencyInfo) {
        val id = updateCurrencyInfo(data)
        if (id > 0) {
            DataStoreUtils.setCurrencyUpdatedTime()
        }
    }
}