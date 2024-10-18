package com.an.trailers_compose.ui.list.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.an.trailers_compose.R
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.ui.component.CircleRevealPager
import com.an.trailers_compose.ui.component.EmptyScreen
import com.an.trailers_compose.ui.component.LoadingItem
import com.an.trailers_compose.ui.component.ProvideAppBarTitle
import com.an.trailers_compose.ui.component.TopBarTitle

@Composable
fun MovieListScreen(
    movies: LazyPagingItems<MovieEntity>,
    onItemClicked: (remoteId: Long) -> Unit
) {
    // Toolbar title
    ProvideAppBarTitle { TopBarTitle(text = R.string.title_movies) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Different load states – Loading, Empty State, Pager list state
        val loadState = movies.loadState.mediator
        when (loadState?.refresh) {
            LoadState.Loading -> {
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
                CircleRevealPager(movies)
            }
        }
    }
}
