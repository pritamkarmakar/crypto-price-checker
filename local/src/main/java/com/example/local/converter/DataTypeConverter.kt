package com.example.local.converter

import androidx.room.TypeConverter
import com.example.model.Value
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList

class DataTypeConverter {
    @TypeConverter
    fun storedStringToMyObjects(data: String?): List<Value> {
        val gson = Gson()
        if (data == null) {
            return emptyList()
        }
        val listType = object : TypeToken<List<Value>>() {

        }.type
        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun fromArrayList(list: MutableList<Value>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}