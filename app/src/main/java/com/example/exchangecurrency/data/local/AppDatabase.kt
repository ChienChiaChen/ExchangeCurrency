package com.example.exchangecurrency.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.exchangecurrency.data.entity.CurrencyInfo


@Database(
    entities = [CurrencyInfo::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(MapConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao

    companion object {
        private const val DATABASE_NAME: String = "currency_db"
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration().build()
    }
}