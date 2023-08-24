package com.example.exchangecurrency.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.exchangecurrency.data.local.MapConverter
import com.google.gson.annotations.SerializedName

@Entity("CurrencyInfo")
data class CurrencyInfo(
    @PrimaryKey
    @ColumnInfo(name = "base")
    @SerializedName(value = "base")
    val base: String = "",

    @ColumnInfo(name = "disclaimer")
    @SerializedName(value = "disclaimer")
    val disclaimer: String = "",

    @ColumnInfo(name = "license")
    @SerializedName(value = "license")
    val license: String = "",

    @ColumnInfo(name = "timestamp")
    @SerializedName(value = "timestamp")
    val timestamp: Long = 0,

    @TypeConverters(MapConverter::class)
    @ColumnInfo(name = "rates")
    @SerializedName(value = "rates")
    val rates: Map<String, Float> = mutableMapOf(),
)
