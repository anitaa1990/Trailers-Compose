package com.an.trailers_compose.ui.list.movie

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.an.trailers_compose.AppConstants.IMAGE_URL
import com.an.trailers_compose.R
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.ui.component.CustomSearchBar
import com.an.trailers_compose.ui.component.ProvideAppBarTitle
import com.an.trailers_compose.ui.component.TopBarTitle

@Composable
fun MovieListScreen(
    movies: LazyPagingItems<MovieEntity>
) {
    // Toolbar title
    ProvideAppBarTitle { TopBarTitle(text = R.string.title_movies) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Search Bar
        CustomSearchBar()

        // Movie List
        val pagerState = rememberPagerState(
            pageCount = { movies.itemCount }
        )
        HorizontalPager(
            state = pagerState,
            pageSize = PageSize.Fill
        ) { index ->
            movies[index]?.let {
                MovieListItem(
                    movie = it
                )
            }
        }
    }
}

@Composable
fun MovieListItem(
    movie: MovieEntity
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxSize(0.85f),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(20.dp)
        ) {
            AsyncImage(
                model = String.format(IMAGE_URL, movie.posterPath),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(top = 20.dp, bottom = 2.dp),
            text = movie.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier.fillMaxWidth(0.6f),
            text = movie.tagline ?: movie.overview,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}