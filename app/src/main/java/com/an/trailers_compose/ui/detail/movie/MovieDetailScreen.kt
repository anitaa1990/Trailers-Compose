package com.an.trailers_compose.ui.detail.movie

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.component.ContentCard
import com.an.trailers_compose.ui.component.SimilarContentCard
import com.an.trailers_compose.ui.component.TrailersCard
import com.an.trailers_compose.utils.ImageUtils

@Composable
fun MovieDetailScreen(
    viewModel: MovieDetailViewModel,
    onItemClicked: (remoteId: Long) -> Unit
) {
    val movieUiState = viewModel.movieUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    if (movieUiState.value is MovieDetailViewModel.MovieDetailUiState.Success) {
        val movie = (movieUiState.value as MovieDetailViewModel.MovieDetailUiState.Success).content

        var backgroundColor by remember { mutableStateOf(Color.Black) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .verticalScroll(rememberScrollState()),
        ) {

            // Poster Image
            PosterImage(imageUrl = movie.posterUrl) {
                backgroundColor = ImageUtils.parseColorSwatch(
                    Palette.from(it).generate().mutedSwatch
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 240.dp)
            ) {
                // Trailers list
                TrailersCard(
                    videos = movie.trailers
                )

                // Movie detail card
                ContentCard(content = movie)

                // Similar movies
                SimilarContentCard(
                    similarContentTitleId = R.string.similar_movies,
                    similarContent = movie.similarMovies,
                    onItemClicked = onItemClicked
                )
            }
        }
    }
}

@Composable
fun PosterImage(
    imageUrl: String,
    onImageLoaded:(bitmap: Bitmap) -> Unit
) {
    Column (
        modifier = Modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "",
            modifier = Modifier.aspectRatio(ratio = 1.5f/1f),
            contentScale = ContentScale.Crop,
            onSuccess = {
                val bitmap = (it.result.drawable as BitmapDrawable).bitmap.copy(
                    Bitmap.Config.ARGB_8888, true
                )
                onImageLoaded(bitmap)
            }
        )
    }
}
