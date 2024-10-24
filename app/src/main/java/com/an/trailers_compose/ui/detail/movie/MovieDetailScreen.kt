package com.an.trailers_compose.ui.detail.movie

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.component.ErrorScreen
import com.an.trailers_compose.ui.component.LoadingItem
import com.an.trailers_compose.ui.detail.common.ContentSuccessView
import com.an.trailers_compose.ui.state.ContentDetailUiState

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MovieDetailScreen(
    viewModel: MovieDetailViewModel,
    onItemClicked: (remoteId: Long) -> Unit,
    onVideoItemClicked: (key: String) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    val movieUiState = viewModel.movieUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    when(movieUiState.value) {
        is ContentDetailUiState.Success -> {
            val movie = (movieUiState.value as ContentDetailUiState.Success).content
            ContentSuccessView(
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
            ErrorScreen(errorMessage = stringResource(id = R.string.load_error)) {
                viewModel.refresh()
            }
    }
}
