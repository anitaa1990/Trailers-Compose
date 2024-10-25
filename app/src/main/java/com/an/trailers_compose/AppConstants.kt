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

    const val MOVIES = "Movies"
    const val TV = "TV"
    const val SEARCH_MOVIES = "Search-movies"
    const val SEARCH_TV = "Search-tv"
    const val MOVIE_STATUS_RELEASED = "Released"

    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500%s"
    const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/maxresdefault.jpg"

    private const val ROUTE_MOVIE_DETAIL = "movie_detail"
    private const val ROUTE_TV_DETAIL = "tv_detail"
    const val ROUTE_DETAIL_ARG_NAME = "remoteId"

    const val ROUTE_MOVIE_DETAIL_PATH = "$ROUTE_MOVIE_DETAIL/$ROUTE_DETAIL_ARG_NAME/{$ROUTE_DETAIL_ARG_NAME}"
    const val ROUTE_TV_DETAIL_PATH = "$ROUTE_TV_DETAIL/$ROUTE_DETAIL_ARG_NAME/{$ROUTE_DETAIL_ARG_NAME}"

    const val KEY_SHARED_TRANSITION_IMAGE = "image-%s"
    const val KEY_SHARED_TRANSITION_TITLE = "title-%s"
    const val KEY_SHARED_TRANSITION_DESC = "desc-%s"

    const val INTENT_VIDEO_KEY = "intent_video_key"

    val genreColors = listOf(genreColor1, genreColor2, genreColor3, genreColor4)
    val movieCategories = listOf(Category.NOW_PLAYING, Category.POPULAR, Category.TOP_RATED, Category.UPCOMING)
    val tvCategories = listOf(Category.AIRING_TODAY, Category.POPULAR, Category.TOP_RATED, Category.ON_THE_AIR)
}