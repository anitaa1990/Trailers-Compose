package com.an.trailers_compose.data.remote.model

import com.an.trailers_compose.data.local.entity.MovieEntity
import com.google.gson.annotations.SerializedName

data class MovieApiResponse(
    val page: Long,
    @SerializedName("total_pages")
    val totalPages: Long,
    @SerializedName("total_results")
    val totalResults: Long,
    val results: List<MovieEntity>
)
