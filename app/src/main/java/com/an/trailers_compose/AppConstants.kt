package com.an.trailers_compose

import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.ui.theme.genreColor1
import com.an.trailers_compose.ui.theme.genreColor2
import com.an.trailers_compose.ui.theme.genreColor3
import com.an.trailers_compose.ui.theme.genreColor4

object AppConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    const val PAGE_SIZE = 20
    const val PREFETCH_DISTANCE = 10

    const val HOME = "home"
    const val MOVIES = "Movies"
    const val TV = "TV"
    const val MOVIE_STATUS_RELEASED = "Released"

    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500%s"
    const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/maxresdefault.jpg"

    private const val ROUTE_MOVIE_DETAIL = "movie_detail"
    const val ROUTE_DETAIL_ARG_NAME = "localId"

    const val ROUTE_MOVIE_DETAIL_PATH = "$ROUTE_MOVIE_DETAIL/$ROUTE_DETAIL_ARG_NAME/{$ROUTE_DETAIL_ARG_NAME}"

    val genreColors = listOf(genreColor1, genreColor2, genreColor3, genreColor4)
    val movieCategories = Category.entries.toList()
}