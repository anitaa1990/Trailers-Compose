package com.an.trailers_compose.ui.search

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchScreen(
    viewModel: SearchViewModel = hiltViewModel<SearchViewModel>(),
    onBackButtonClicked: () -> Unit
) {

}

