package com.an.trailers_compose.data.remote.model

enum class Category {
    NOW_PLAYING, POPULAR, TOP_RATED, UPCOMING
}
fun Category.api() = this.name.lowercase()
