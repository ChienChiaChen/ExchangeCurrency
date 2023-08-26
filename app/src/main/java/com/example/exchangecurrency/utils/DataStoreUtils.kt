package com.example.exchangecurrency.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "OERDataStore")

/**
 * the datastore for all module
 */
object DataStoreUtils {
    private lateinit var dataStore: DataStore<Preferences>

    private const val CURRENCY_UPDATED_TIME: String = "currency_updated_time"

    fun init(context: Context) {
        if (!this::dataStore.isInitialized) {
            dataStore = context.dataStore
        }
    }

    fun getUpdatedTime(): Long {
        return readLongData(CURRENCY_UPDATED_TIME, 0)
    }

    fun setCurrencyUpdatedTime(timestamp: Long = System.currentTimeMillis()) {
        saveSyncLongData(CURRENCY_UPDATED_TIME, timestamp)
    }

    private fun readLongData(key: String, default: Long = 0L): Long = runBlocking {
        var value = 0L
        dataStore.data.first {
            value = it[longPreferencesKey(key)] ?: default
            true
        }
        value
    }

    private suspend fun saveLongData(key: String, value: Long) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[longPreferencesKey(key)] = value
        }
    }

    fun saveSyncLongData(key: String, value: Long) = runBlocking { saveLongData(key, value) }
}
