package com.an.trailers_compose.ui.model

import com.an.trailers_compose.AppConstants.IMAGE_URL
import com.an.trailers_compose.data.remote.model.Cast
import com.an.trailers_compose.data.remote.model.Crew

data class Artist(
    val id: Long,
    val name: String,
    val job: String,
    val imageUrl: String
)

fun Cast.toArtist() = Artist(
    id = this.id,
    name = this.name,
    job = this.character,
    imageUrl = String.format(IMAGE_URL, profilePath)
)

fun Crew.toArtist() = Artist(
    id = this.id,
    name = this.name,
    job = this.job,
    imageUrl = String.format(IMAGE_URL, profilePath)
)
