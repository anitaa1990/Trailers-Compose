package com.an.trailers_compose.data.repository

import com.an.trailers_compose.data.remote.api.MovieApiService
import com.an.trailers_compose.data.remote.api.TvApiService
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import com.an.trailers_compose.data.remote.model.TvApiResponse
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val tvApiService: TvApiService,
    private val movieApiService: MovieApiService
) {
    suspend fun searchMovies(
        query: String,
        nextPage: Long = 1L
    ): MovieApiResponse {
        return try {
            val response = movieApiService.searchMovies(
                query = query,
                page = nextPage
            )
            response
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }

    suspend fun searchTvShows(
        query: String,
        nextPage: Long
    ): TvApiResponse {
        return try {
            val response = tvApiService.searchTvList(
                query = query,
                page = nextPage
            )
            response
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception(e)
        }
    }
}