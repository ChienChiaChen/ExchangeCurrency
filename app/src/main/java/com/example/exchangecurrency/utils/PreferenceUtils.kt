package com.example.exchangecurrency.utils

object PreferenceUtils {
    fun isDataExpired(timeout: Long = 30 * 60 * 1000): Boolean {
        return (System.currentTimeMillis() - DataStoreUtils.getUpdatedTime()) > timeout
    }
}