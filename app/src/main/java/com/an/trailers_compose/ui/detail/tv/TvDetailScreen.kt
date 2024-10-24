package com.an.trailers_compose.ui.detail.tv

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.component.ContentCard
import com.an.trailers_compose.ui.component.EmptyScreen
import com.an.trailers_compose.ui.component.LoadingItem
import com.an.trailers_compose.ui.component.PosterImage
import com.an.trailers_compose.ui.component.SimilarContentCard
import com.an.trailers_compose.ui.component.TrailersCard
import com.an.trailers_compose.ui.model.Content
import com.an.trailers_compose.ui.state.ContentDetailUiState
import com.an.trailers_compose.utils.ImageUtils

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TvDetailScreen(
    viewModel: TvDetailViewModel,
    onItemClicked: (remoteId: Long) -> Unit,
    onVideoItemClicked: (key: String) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    val movieUiState = viewModel.tvUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    when(movieUiState.value) {
        is ContentDetailUiState.Success -> {
            val movie = (movieUiState.value as ContentDetailUiState.Success).content
            MovieSuccessView(
                content = movie,
                onItemClicked = onItemClicked,
                onVideoItemClicked = onVideoItemClicked,
                animatedContentScope = animatedContentScope
            )
        }
        is ContentDetailUiState.Loading -> {
            LoadingItem()
        }
        is ContentDetailUiState.Error ->
            EmptyScreen(errorMessage = stringResource(id = R.string.load_error)) {
                viewModel.refresh()
            }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MovieSuccessView(
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

            // Movie detail card
            ContentCard(
                content = content,
                animatedContentScope = animatedContentScope
            )

            // Similar movies
            SimilarContentCard(
                similarContentTitleId = R.string.similar_movies,
                similarContent = content.similarMovies,
                onItemClicked = onItemClicked
            )
        }
    }
}
