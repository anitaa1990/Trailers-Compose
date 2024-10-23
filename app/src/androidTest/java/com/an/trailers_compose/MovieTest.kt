package com.an.trailers_compose

import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MovieTest {
    fun getPopularMovies(): List<MovieEntity> {
        val reader = MockResponseFileReader("movie_list_api_response.json")
        val listType = object : TypeToken<MovieApiResponse>() {}.type
        return Gson().fromJson<MovieApiResponse?>(reader.content, listType).results
    }
}