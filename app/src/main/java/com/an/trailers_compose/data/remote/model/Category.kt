package com.an.trailers_compose.data.remote.model

import androidx.annotation.DrawableRes
import com.an.trailers_compose.R

enum class Category(@DrawableRes val iconResId: Int) {
    NOW_PLAYING(R.drawable.ic_now_playing),
    POPULAR(R.drawable.ic_popular),
    TOP_RATED(R.drawable.ic_top_rated),
    UPCOMING(R.drawable.ic_upcoming)
}
fun Category.api() = this.name.lowercase()

fun Category.equalTo(category: Category?) = this == category
