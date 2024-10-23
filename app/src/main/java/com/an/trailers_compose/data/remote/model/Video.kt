package com.an.trailers_compose.data.remote.model

import com.an.trailers_compose.AppConstants

data class Video(
    val id: String,
    val key: String
)

fun Video.youtubeUrl() = String.format(AppConstants.YOUTUBE_THUMBNAIL_URL, this.key)