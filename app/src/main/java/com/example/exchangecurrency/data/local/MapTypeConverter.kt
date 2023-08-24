package com.example.exchangecurrency.data.local

import androidx.room.TypeConverter
import com.example.exchangecurrency.utils.fromJson
import com.example.exchangecurrency.utils.typedToJson
import com.google.gson.GsonBuilder


object MapConverter {
    @[TypeConverter JvmStatic]
    fun string2Map(src: String): Map<String, Float> =
        GsonBuilder().create().fromJson(src)


    @[TypeConverter JvmStatic]
    fun map2String(data: Map<String, Float>?): String {
        return GsonBuilder().create().typedToJson(data)
    }
}
