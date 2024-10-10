package com.an.trailers_compose.data.local.converter

import androidx.room.TypeConverter
import com.an.trailers_compose.data.remote.model.Genre
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class GenreListTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<Genre>? {
        val listType = object : TypeToken<List<Genre>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(genres: List<Genre>?): String? {
        return Gson().toJson(genres)
    }
}
