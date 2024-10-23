package com.an.trailers_compose.data.local.converter

import androidx.room.TypeConverter
import com.an.trailers_compose.data.remote.model.TvApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TvApiResponseTypeConverter {
    @TypeConverter
    fun fromString(value: String): TvApiResponse {
        val listType = object : TypeToken<TvApiResponse>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: TvApiResponse): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}