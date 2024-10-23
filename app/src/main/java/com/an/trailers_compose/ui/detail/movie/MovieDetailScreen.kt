package com.an.trailers_compose.ui.detail.movie

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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.component.ContentCard
import com.an.trailers_compose.ui.component.PosterImage
import com.an.trailers_compose.ui.component.SimilarContentCard
import com.an.trailers_compose.ui.component.TrailersCard
import com.an.trailers_compose.utils.ImageUtils

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MovieDetailScreen(
    viewModel: MovieDetailViewModel,
    onItemClicked: (remoteId: Long) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    val movieUiState = viewModel.movieUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    if (movieUiState.value is MovieDetailViewModel.MovieDetailUiState.Success) {
        val movie = (movieUiState.value as MovieDetailViewModel.MovieDetailUiState.Success).content

        var backgroundColor by remember { mutableStateOf(Color.Black) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .verticalScroll(rememberScrollState()),
        ) {

            // Poster Image
            PosterImage(
                imageUrl = movie.posterUrl,
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
                    videos = movie.trailers
                )

                // Movie detail card
                ContentCard(
                    content = movie,
                    animatedContentScope = animatedContentScope
                )

                // Similar movies
                SimilarContentCard(
                    similarContentTitleId = R.string.similar_movies,
                    similarContent = movie.similarMovies,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}
