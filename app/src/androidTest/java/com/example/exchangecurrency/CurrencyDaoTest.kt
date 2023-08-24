package com.example.exchangecurrency

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.exchangecurrency.DummyData.fakeCurrencyDataBaseABC
import com.example.exchangecurrency.DummyData.fakeCurrencyDataBaseUSD
import com.example.exchangecurrency.data.local.AppDatabase
import com.example.exchangecurrency.data.local.CurrencyDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CurrencyDaoTest {

    private lateinit var currencyDao: CurrencyDao
    private lateinit var appDatabase: AppDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        currencyDao = appDatabase.currencyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun deleteAll() = runBlocking {
        currencyDao.insert(fakeCurrencyDataBaseUSD)
        currencyDao.insert(fakeCurrencyDataBaseABC)
        currencyDao.clearAll()
        val allCurrencies = currencyDao.getAllCurrencies().first()
        Assert.assertTrue(allCurrencies.isEmpty())
    }

    @Test
    @Throws(Exception::class)
    fun getCurrencyBaseInABC() = runBlocking {
        val currency = fakeCurrencyDataBaseABC
        currencyDao.insert(currency)
        val currencyABCFromDb = currencyDao.getCurrencyInBaseCurrency("ABC")
        assertEquals(currencyABCFromDb, currency)
    }

    @Test
    @Throws(Exception::class)
    fun getAllCurrencies() = runBlocking {
        val currencyABC = fakeCurrencyDataBaseABC
        val currencyUSD = fakeCurrencyDataBaseUSD
        currencyDao.insert(currencyABC)
        currencyDao.insert(currencyUSD)
        val currenciesFromDb = currencyDao.getAllCurrencies().first()
        assertEquals(currenciesFromDb[0], currencyABC)
        assertEquals(currenciesFromDb[1], currencyUSD)
    }
}