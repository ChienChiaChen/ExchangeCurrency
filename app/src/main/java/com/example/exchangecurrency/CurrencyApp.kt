package com.example.exchangecurrency

import android.app.Application
import com.example.exchangecurrency.utils.DataStoreUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DataStoreUtils.init(this@CurrencyApp)
    }
}