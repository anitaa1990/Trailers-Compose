package com.an.trailers_compose.ui.search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.an.trailers_compose.ui.component.ErrorScreen
import com.an.trailers_compose.ui.component.SearchInputField
import com.an.trailers_compose.ui.list.common.CircleRevealPager

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.SearchScreen(
    viewModel: SearchViewModel = hiltViewModel<SearchViewModel>(),
    onBackButtonClicked: () -> Unit,
    onItemClicked: (remoteId: Long) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    val inputText = viewModel.inputText.collectAsState().value
    val content = viewModel.movieSearchResults.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchInputField(
            inputText = inputText,
            onClearInputClicked = { viewModel.clearInput() },
            onSearchInputChanged = { input -> viewModel.updateInput(input) },
            onChevronClicked = { onBackButtonClicked() }
        )

        val loadState = content.loadState
        when (loadState.refresh) {
            is LoadState.Loading -> {  }
            is LoadState.Error -> {
                val error = (loadState.refresh as LoadState.Error).error
                ErrorScreen(errorMessage = error.message ?: error.toString()) {
                    content.refresh()
                }
            }
            else -> {
                CircleRevealPager(
                    contentList = content,
                    onItemClicked = onItemClicked,
                    animatedContentScope = animatedContentScope
                )
            }
        }
    }
}
