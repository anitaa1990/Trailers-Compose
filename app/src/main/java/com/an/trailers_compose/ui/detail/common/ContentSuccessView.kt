package com.an.trailers_compose.ui.detail.common

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.model.Content
import com.an.trailers_compose.utils.ImageUtils

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ContentSuccessView(
    content: Content,
    onItemClicked: (remoteId: Long) -> Unit,
    onVideoItemClicked: (key: String) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    var backgroundColor by remember { mutableStateOf(Color.Black) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = backgroundColor)
            .verticalScroll(rememberScrollState()),
    ) {

        // Poster Image
        PosterImage(
            imageUrl = content.posterUrl,
            onImageLoaded = {
                backgroundColor = ImageUtils.parseColorSwatch(
                    Palette.from(it).generate().mutedSwatch
                )
            },
            animatedContentScope = animatedContentScope
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 240.dp)
        ) {
            // Trailers list
            TrailersCard(
                videos = content.trailers,
                onVideoItemClicked = onVideoItemClicked
            )

            // Content detail card
            ContentCard(
                content = content,
                animatedContentScope = animatedContentScope
            )

            // Similar movies/tv
            SimilarContentCard(
                similarContentTitleId = R.string.similar_movies,
                similarContent = content.similarMovies,
                onItemClicked = onItemClicked
            )
        }
    }
}
