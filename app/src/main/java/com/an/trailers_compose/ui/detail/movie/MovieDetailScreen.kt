package com.an.trailers_compose.ui.detail.movie

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel) {
    val movieUiState = viewModel.movieUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )
}