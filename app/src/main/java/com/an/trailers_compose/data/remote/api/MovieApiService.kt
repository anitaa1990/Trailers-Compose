package com.an.trailers_compose.data.remote.api

import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    /**
     * We would be using the below url:
     * https://api.themoviedb.org/3/movie/{type}
     * where type can be: top_rated, now_playing, upcoming or popular
     */
    @GET("movie/{type}?language=en-US&region=US")
    suspend fun fetchMovies(
        @Path("type") type: String,
        @Query("page") page: Long
    ): MovieApiResponse

    @GET("movie/{movieId}?append_to_response=videos,credits,similar")
    suspend fun fetchMovieDetail(
        @Path("movieId") movieId: Long
    ): MovieEntity
}
