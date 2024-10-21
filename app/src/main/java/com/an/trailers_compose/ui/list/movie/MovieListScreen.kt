package com.an.trailers_compose.ui.list.movie

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.an.trailers_compose.R
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.ui.component.CircleRevealPager
import com.an.trailers_compose.ui.component.EmptyScreen
import com.an.trailers_compose.ui.component.LoadingItem

@Composable
fun MovieListScreen(
    movies: LazyPagingItems<MovieEntity>,
    onItemClicked: (remoteId: Long) -> Unit
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
                CircleRevealPager(movies = movies, onItemClicked = onItemClicked)

                // Added filter, search & Tv/movie option
                MenuItems()
            }
        }
    }
}

@Composable
fun MenuItems() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 55.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // Filter option
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = R.drawable.ic_filter),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.weight(1f))
        // Tv or Movie option
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(id = R.drawable.ic_tv),
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.weight(1f))
        // Search icon
        Icon(
            modifier = Modifier.size(25.dp),
            imageVector = Icons.Default.Search,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}