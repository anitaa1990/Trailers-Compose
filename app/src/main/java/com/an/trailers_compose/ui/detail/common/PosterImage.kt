package com.an.trailers_compose.ui.detail.common

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.an.trailers_compose.AppConstants.KEY_SHARED_TRANSITION_IMAGE

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.PosterImage(
    imageUrl: String,
    onImageLoaded:(bitmap: Bitmap) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    Column (
        modifier = Modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier
                .aspectRatio(ratio = 1.5f/1f)
                .sharedElement(
                    rememberSharedContentState(
                        key = String.format(KEY_SHARED_TRANSITION_IMAGE, imageUrl)
                    ), animatedVisibilityScope = animatedContentScope
                ),
            contentScale = ContentScale.Crop,
            onSuccess = {
                val bitmap = (it.result.drawable as BitmapDrawable).bitmap.copy(
                    Bitmap.Config.ARGB_8888, true
                )
                onImageLoaded(bitmap)
            }
        )
    }
}
