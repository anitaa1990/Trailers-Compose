package com.an.trailers_compose.utils

import android.content.Context
import android.content.Intent
import androidx.navigation.NavHostController
import com.an.trailers_compose.AppConstants
import com.an.trailers_compose.AppConstants.INTENT_VIDEO_KEY
import com.an.trailers_compose.VideoActivity
import dagger.hilt.android.qualifiers.ApplicationContext

fun NavHostController.navigateToDetail(id: Long) {
    this.navigate(
        route = AppConstants.ROUTE_MOVIE_DETAIL_PATH.replace(
            "{${AppConstants.ROUTE_DETAIL_ARG_NAME}}",
            "$id"
        )
    )
}

fun navigateToVideo(@ApplicationContext context: Context, key: String) {
    val intent = Intent(context, VideoActivity::class.java)
    intent.putExtra(INTENT_VIDEO_KEY, key)
    context.startActivity(intent)
}