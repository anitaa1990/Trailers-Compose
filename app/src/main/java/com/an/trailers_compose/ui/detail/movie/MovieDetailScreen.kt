package com.an.trailers_compose.ui.detail.movie

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.an.trailers_compose.AppConstants.IMAGE_URL
import com.an.trailers_compose.AppConstants.YOUTUBE_THUMBNAIL_URL
import com.an.trailers_compose.R
import com.an.trailers_compose.data.remote.model.Video

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel) {
    val movieUiState = viewModel.movieUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    if (movieUiState.value is MovieDetailViewModel.MovieDetailUiState.Success) {
        val movie = (movieUiState.value as MovieDetailViewModel.MovieDetailUiState.Success).movie
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            // Background Image
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(String.format(IMAGE_URL, movie.backdropPath))
                    .build(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
                contentScale = ContentScale.Crop
            )

            // Poster Image
            PosterImage(String.format(IMAGE_URL, movie.posterPath))

            // Trailers list
            movie.videoApiResponse?.let {
                TrailersList(videos = it.videos)
            }
        }
    }
}

@Composable
fun PosterImage(imageUrl: String) {
    Column (
        modifier = Modifier
            .fillMaxHeight(0.25f)
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun TrailersList(videos: List<Video>) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 180.dp)) {

        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
        ) {
            items(
                count = videos.size
            ) { index ->
                TrailersListItem(video = videos[index])
            }
        }
    }
}

@Composable
fun TrailersListItem(video: Video) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize(0.2f),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Box(modifier = Modifier) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(String.format(YOUTUBE_THUMBNAIL_URL, video.key))
                    .build(),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Icon(
                modifier = Modifier
                    .size(80.dp)
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .shadow(20.dp)
                ,
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primaryContainer,
            )
        }
    }
}