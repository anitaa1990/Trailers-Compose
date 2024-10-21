package com.an.trailers_compose.ui.model

import com.an.trailers_compose.AppConstants
import com.an.trailers_compose.AppConstants.MOVIE_STATUS_RELEASED
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.remote.model.Genre
import com.an.trailers_compose.data.remote.model.Video

data class Content(
    val id: Long,
    val title: String,
    val description: String,
    val posterUrl: String,
    val backdropUrl: String,
    val genres: List<Genre>,
    val trailers: List<Video>,
    val status: String,
    val durationInMins: String,
    val cast: List<Artist>,
    val crew: List<Artist>,
    val similarMovies: List<SimilarContent>
)

data class SimilarContent(
    val id: Long,
    val posterUrl: String
)

fun MovieEntity.toSimilarContent() = SimilarContent(
    id = this.remoteId,
    posterUrl = getPosterUrl()
)

fun MovieEntity.toContent() = Content(
    id = this.remoteId,
    title = this.title,
    description = this.overview,
    posterUrl = getPosterUrl(),
    backdropUrl = getBackdropUrl(),
    genres = this.genres ?: emptyList(),
    trailers = this.videoApiResponse?.videos ?: emptyList(),
    status = this.status ?: "",
    durationInMins = this.getDuration(),
    cast = this.credits?.cast?.map { it.toArtist() } ?: emptyList(),
    crew = this.credits?.crew?.map { it.toArtist() } ?: emptyList(),
    similarMovies = this.similarMoviesApiResponse?.results?.map { it.toSimilarContent() } ?: emptyList()
)

private fun MovieEntity.getDuration() = if (MOVIE_STATUS_RELEASED == status)
    runtime.toString() else releaseDate

private fun MovieEntity.getPosterUrl() = String.format(AppConstants.IMAGE_URL, this.posterPath)
private fun MovieEntity.getBackdropUrl() = String.format(AppConstants.IMAGE_URL, this.backdropPath)