package com.an.trailers_compose.data.local.converter

import androidx.room.TypeConverter
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MovieApiResponseTypeConverter {
    @TypeConverter
    fun fromString(value: String): MovieApiResponse {
        val listType = object : TypeToken<MovieApiResponse>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: MovieApiResponse): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}