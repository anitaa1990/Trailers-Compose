package com.an.trailers_compose

import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object MovieTest {
    fun getNowPlayingMovies(): List<MovieEntity> {
        val reader = MockResponseFileReader("movie_now_playing_list_api_response.json")
        val listType = object : TypeToken<MovieApiResponse>() {}.type
        return Gson().fromJson<MovieApiResponse?>(reader.content, listType).results
    }
    fun getTopRatedMovies(): List<MovieEntity> {
        val reader = MockResponseFileReader("movie_top_rated_list_api_response.json")
        val listType = object : TypeToken<MovieApiResponse>() {}.type
        return Gson().fromJson<MovieApiResponse?>(reader.content, listType).results
    }
    fun getPopularMovies(): List<MovieEntity> {
        val reader = MockResponseFileReader("movie_popular_list_api_response.json")
        val listType = object : TypeToken<MovieApiResponse>() {}.type
        return Gson().fromJson<MovieApiResponse?>(reader.content, listType).results
    }
    fun getUpcomingMovies(): List<MovieEntity> {
        val reader = MockResponseFileReader("movie_upcoming_list_api_response.json")
        val listType = object : TypeToken<MovieApiResponse>() {}.type
        return Gson().fromJson<MovieApiResponse?>(reader.content, listType).results
    }
}