package com.an.trailers_compose.data.local.converter

import androidx.room.TypeConverter
import com.an.trailers_compose.data.remote.model.CreditsApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CreditResponseTypeConverter {
    @TypeConverter
    fun fromString(value: String): CreditsApiResponse {
        val listType = object : TypeToken<CreditsApiResponse>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: CreditsApiResponse): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
