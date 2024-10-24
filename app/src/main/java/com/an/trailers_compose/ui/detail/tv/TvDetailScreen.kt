package com.an.trailers_compose.ui.detail.tv

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
fun SharedTransitionScope.TvDetailScreen(
    viewModel: TvDetailViewModel,
    onItemClicked: (remoteId: Long) -> Unit,
    onVideoItemClicked: (key: String) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    val tvUiState = viewModel.tvUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    when(tvUiState.value) {
        is ContentDetailUiState.Success -> {
            val tv = (tvUiState.value as ContentDetailUiState.Success).content
            ContentSuccessView(
                content = tv,
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
