package com.an.trailers_compose.ui.list.tv

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.an.trailers_compose.AppConstants.tvCategories
import com.an.trailers_compose.R
import com.an.trailers_compose.data.remote.model.Category
import com.an.trailers_compose.ui.list.common.CircleRevealPager
import com.an.trailers_compose.ui.list.common.ContentCategories
import com.an.trailers_compose.ui.list.common.ContentMenuBar
import com.an.trailers_compose.ui.component.EmptyScreen
import com.an.trailers_compose.ui.component.LoadingItem
import com.an.trailers_compose.ui.model.Content

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.TvListScreen(
    tvSeriesList: LazyPagingItems<Content>,
    onItemClicked: (remoteId: Long) -> Unit,
    selectedCategory: Category,
    onCategorySelected: (category: Category) -> Unit,
    onMenuItemSelected: () -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Different load states – Loading, Empty State, Pager list state
        val loadState = tvSeriesList.loadState.mediator
        when (loadState?.refresh) {
            is LoadState.Loading -> {
                LoadingItem()
            }
            is LoadState.Error -> {
                val error = (loadState.refresh as LoadState.Error).error
                EmptyScreen(errorMessage = error.message ?: error.toString()) {
                    tvSeriesList.refresh()
                }
            }
            else -> {
                // Tv List
                CircleRevealPager(
                    contentList = tvSeriesList,
                    onItemClicked = onItemClicked,
                    animatedContentScope = animatedContentScope
                )

                // Added search & movie option
                ContentMenuBar(
                    resId = R.drawable.ic_movie,
                    onMenuSelected = onMenuItemSelected
                )

                // Filter by category
                ContentCategories(
                    selectedCategory = selectedCategory,
                    categories = tvCategories,
                    onCategorySelected = onCategorySelected
                )
            }
        }
    }
}