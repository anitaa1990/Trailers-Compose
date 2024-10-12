package com.an.trailers_compose.ui.list.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.an.trailers_compose.AppConstants.IMAGE_URL
import com.an.trailers_compose.R
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.ui.component.ProvideAppBarTitle
import com.an.trailers_compose.ui.component.TopBarTitle

@Composable
fun MovieListScreen(
    movies: LazyPagingItems<MovieEntity>
) {
    // Toolbar title
    ProvideAppBarTitle { TopBarTitle(text = R.string.title_movies) }

    // Movie List
    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow {
            items(
                count = movies.itemCount,
                key = movies.itemKey { it.remoteId }
            ) { index ->
                movies[index]?.let { movie ->
                    MovieListItem(
                        movie, Modifier
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListItem(
    movie: MovieEntity,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        AsyncImage(
            model = String.format(IMAGE_URL, movie.posterPath),
            modifier = Modifier.width(350.dp),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}