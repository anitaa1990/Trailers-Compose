package com.an.trailers_compose

object AppConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    const val PAGE_SIZE = 20
    const val PREFETCH_DISTANCE = 10

    const val HOME = "home"
    const val MOVIES = "Movies"
    const val TV = "TV"

    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500%s"
    const val YOUTUBE_THUMBNAIL_URL = "https://img.youtube.com/vi/%s/maxresdefault.jpg"

    private const val ROUTE_MOVIE_DETAIL = "movie_detail"
    const val ROUTE_DETAIL_ARG_NAME = "localId"

    const val ROUTE_MOVIE_DETAIL_PATH = "$ROUTE_MOVIE_DETAIL/$ROUTE_DETAIL_ARG_NAME/{$ROUTE_DETAIL_ARG_NAME}"
}