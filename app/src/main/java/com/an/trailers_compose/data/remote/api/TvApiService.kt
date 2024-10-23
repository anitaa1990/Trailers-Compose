package com.an.trailers_compose.data.remote.api

import com.an.trailers_compose.data.local.entity.TvEntity
import com.an.trailers_compose.data.remote.model.TvApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvApiService {
    /**
     * We would be using the below url:
     * https://api.themoviedb.org/3/tv/{type}
     * where type can be: top_rated, on_the_air, airing_today or popular
     */
    @GET("tv/{type}?language=en-US&region=US")
    suspend fun fetchTvList(
        @Path("type") type: String,
        @Query("page") page: Long
    ): TvApiResponse

    @GET("tv/{tvId}?append_to_response=videos,credits,similar")
    suspend fun fetchTvDetail(
        @Path("tvId") tvId: Long
    ): TvEntity
}
