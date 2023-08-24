package com.example.exchangecurrency.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.exchangecurrency.data.entity.CurrencyInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CurrencyInfo): Long

    @Query("SELECT * FROM CurrencyInfo WHERE base = :baseCurrency")
    suspend fun getCurrencyInBaseCurrency(baseCurrency: String): CurrencyInfo?

    @Query("SELECT * FROM CurrencyInfo")
    fun getAllCurrencies(): Flow<List<CurrencyInfo>>

    @Query("DELETE FROM CurrencyInfo")
    suspend fun clearAll(): Int
}