package com.an.trailers_compose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.an.trailers_compose.data.remote.model.CreditsApiResponse
import com.an.trailers_compose.data.remote.model.Genre
import com.an.trailers_compose.data.remote.model.MovieApiResponse
import com.an.trailers_compose.data.remote.model.VideoApiResponse
import com.google.gson.annotations.SerializedName

@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Long = 0,
    @SerializedName("id")
    val remoteId: Long,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Long>? = emptyList(),
    val genres: List<Genre>? = emptyList(),
    val title: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Long,
    val revenue: Double,
    val runtime: Long,
    val status: String?,
    val tagline: String?,
    @SerializedName("videos")
    val videoApiResponse: VideoApiResponse?,
    val credits: CreditsApiResponse?,
    @SerializedName("similar")
    val similarMoviesApiResponse: MovieApiResponse?
)
