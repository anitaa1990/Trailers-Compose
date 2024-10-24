package com.an.trailers_compose.ui.list.movie

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.an.trailers_compose.AppConstants.movieCategories
import com.an.trailers_compose.R
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.ui.component.CircleRevealPager
import com.an.trailers_compose.ui.component.ContentCategories
import com.an.trailers_compose.ui.component.ContentMenuBar
import com.an.trailers_compose.ui.component.EmptyScreen
import com.an.trailers_compose.ui.component.LoadingItem
import com.an.trailers_compose.ui.model.Content

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.MovieListScreen(
    movies: LazyPagingItems<Content>,
    onItemClicked: (remoteId: Long) -> Unit,
    selectedCategory: Category,
    onCategorySelected: (category: Category) -> Unit,
    onTvMenuSelected: () -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Different load states – Loading, Empty State, Pager list state
        val loadState = movies.loadState.mediator
        when (loadState?.refresh) {
            is LoadState.Loading -> {
                LoadingItem()
            }
            is LoadState.Error -> {
                val error = (loadState.refresh as LoadState.Error).error
                EmptyScreen(errorMessage = error.message ?: error.toString()) {
                    movies.refresh()
                }
            }
            else -> {
                // Movie List
                CircleRevealPager(
                    contentList = movies,
                    onItemClicked = onItemClicked,
                    animatedContentScope = animatedContentScope
                )

                // Added filter, search & Tv option
                ContentMenuBar(
                    resId = R.drawable.ic_tv,
                    onMenuSelected = onTvMenuSelected
                )

                // Filter by category
                ContentCategories(
                   selectedCategory = selectedCategory,
                    categories = movieCategories,
                    onCategorySelected = onCategorySelected
                )
            }
        }
    }
}
