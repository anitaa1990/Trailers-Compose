package com.an.trailers_compose.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.an.trailers_compose.data.remote.model.CreditsApiResponse
import com.an.trailers_compose.data.remote.model.Genre
import com.an.trailers_compose.data.remote.model.TvApiResponse
import com.an.trailers_compose.data.remote.model.VideoApiResponse
import com.google.gson.annotations.SerializedName

@Entity
data class TvEntity(
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
    @SerializedName("name")
    val title: String,
    val overview: String,
    @SerializedName("first_air_date")
    val releaseDate: String,
    val runtime: Long,
    val status: String?,
    @SerializedName("videos")
    val videoApiResponse: VideoApiResponse?,
    val credits: CreditsApiResponse?,
    @SerializedName("similar")
    val similarTvApiResponse: TvApiResponse?
)
