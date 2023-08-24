package com.example.exchangecurrency.di

import android.app.Application
import com.example.exchangecurrency.data.local.AppDatabase
import com.example.exchangecurrency.data.local.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
class CacheModule {

    @[Provides Singleton]
    fun provideDatabaseModule(application: Application): AppDatabase {
        return AppDatabase.build(application)
    }

    @[Provides Singleton]
    fun provideSearchHistoryDao(appDatabase: AppDatabase): CurrencyDao {
        return appDatabase.currencyDao()
    }

}