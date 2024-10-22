package com.an.trailers_compose.utils

import androidx.navigation.NavHostController
import com.an.trailers_compose.AppConstants

fun NavHostController.navigateToDetail(id: Long) {
    this.navigate(
        route = AppConstants.ROUTE_MOVIE_DETAIL_PATH.replace(
            "{${AppConstants.ROUTE_DETAIL_ARG_NAME}}",
            "$id"
        )
    )
}