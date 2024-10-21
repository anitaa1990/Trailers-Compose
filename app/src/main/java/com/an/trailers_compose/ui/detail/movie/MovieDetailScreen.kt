package com.an.trailers_compose.ui.detail.movie

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.an.trailers_compose.AppConstants.IMAGE_URL
import com.an.trailers_compose.AppConstants.YOUTUBE_THUMBNAIL_URL
import com.an.trailers_compose.AppConstants.genreColors
import com.an.trailers_compose.R
import com.an.trailers_compose.data.local.entity.MovieEntity
import com.an.trailers_compose.data.remote.model.Cast
import com.an.trailers_compose.data.remote.model.Crew
import com.an.trailers_compose.data.remote.model.Genre
import com.an.trailers_compose.data.remote.model.Video
import com.an.trailers_compose.ui.component.ExpandableCard
import com.an.trailers_compose.ui.theme.statusColor
import com.an.trailers_compose.utils.ImageUtils
import java.util.*

@Composable
fun MovieDetailScreen(viewModel: MovieDetailViewModel) {
    val movieUiState = viewModel.movieUiState.collectAsStateWithLifecycle(
        lifecycleOwner = LocalLifecycleOwner.current
    )

    if (movieUiState.value is MovieDetailViewModel.MovieDetailUiState.Success) {
        val movie = (movieUiState.value as MovieDetailViewModel.MovieDetailUiState.Success).movie

        var backgroundColor by remember { mutableStateOf(Color.Black) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = backgroundColor)
                .verticalScroll(rememberScrollState()),
        ) {

            // Poster Image
            PosterImage(imageUrl = String.format(IMAGE_URL, movie.posterPath)) {
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
                movie.videoApiResponse?.let {
                    TrailersList(videos = it.videos)
                }

                // Movie detail card
                MovieCard(movie = movie)

                // Similar movies
                SimilarMovies(movie.similarMoviesApiResponse?.results ?: emptyList())
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

@Composable
fun TrailersList(videos: List<Video>) {
    LazyRow(modifier = Modifier.padding(10.dp)) {
        items(
            count = videos.size
        ) { index ->
            TrailersListItem(video = videos[index])
        }
    }
}

@Composable
fun TrailersListItem(video: Video) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .width(250.dp)
            .height(150.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Box(modifier = Modifier) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(String.format(YOUTUBE_THUMBNAIL_URL, video.key))
                    .build(),
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

@Composable
fun MovieCard(movie: MovieEntity) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(start = 25.dp, end = 15.dp, top = 25.dp, bottom = 8.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            // Movie title
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge
            )
            // Movie Genres
            val colorIndex = Random().nextInt(genreColors.size)
            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 10.dp)
            ) {
                items(
                    count = movie.genres?.size ?: 0
                ) {
                    movie.genres?.get(it)?.let { genre ->
                        GenreItem(genre = genre, colorIndex = colorIndex)
                    }
                }
            }
            // Movie Description
            Text(
                text = movie.overview,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 25.sp),
                modifier = Modifier.padding(vertical = 20.dp)
            )
            // Movie status
            MovieStatus(status = movie.status, runTime = movie.runtime)

            // Read More section
            ExpandableCard{ CastCrewSection(movie.credits?.cast!!, movie.credits.crew) }
        }
    }
}

@Composable
fun GenreItem(
    genre: Genre,
    colorIndex: Int
) {
    FilterChip(
        onClick = {  },
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = true,
            borderColor = genreColors[colorIndex],
            selectedBorderColor = genreColors[colorIndex]
        ),
        shape = RoundedCornerShape(4.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = genreColors[colorIndex]
        ),
        label = {
            Text(
                text = genre.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            )
        },
        selected = true,
        modifier = Modifier.padding(end = 6.dp)
    )
}

@Composable
fun MovieStatus(
    status: String?,
    runTime: Long
) {
   Row(
       modifier = Modifier.fillMaxWidth(),
       horizontalArrangement = Arrangement.Start
   ) {
       // Movie Status
       FilterChip(
           onClick = {  },
           border = FilterChipDefaults.filterChipBorder(
               enabled = true,
               selected = true,
               borderColor = statusColor,
               selectedBorderColor = statusColor
           ),
           shape = RoundedCornerShape(4.dp),
           colors = FilterChipDefaults.filterChipColors(
               selectedContainerColor = statusColor
           ),
           label = {
               Text(
                   text = status!!,
                   style = MaterialTheme.typography.bodyMedium.copy(
                       color = Color.White,
                       textAlign = TextAlign.Center
                   )
               )
           },
           selected = true,
           modifier = Modifier.padding(end = 6.dp)
       )
       // Movie runtime
       Text(
           text = String.format(stringResource(id = R.string.runtime),runTime.toString()),
           style = MaterialTheme.typography.bodyMedium,
           modifier = Modifier
               .padding(horizontal = 20.dp)
               .align(Alignment.CenterVertically),
           textAlign = TextAlign.Center
       )
   }
}

@Composable
fun CastCrewSection(
    cast: List<Cast>,
    crew: List<Crew>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.cast),
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
        ) {
            items(
                count = cast.size
            ) { index ->
                CastItem(cast = cast[index])
            }
        }
        Text(
            text = stringResource(id = R.string.crew),
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider(
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(vertical = 5.dp)
        )
        LazyRow(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(10.dp)
        ) {
            items(
                count = crew.size
            ) { index ->
                CrewItem(crew = crew[index])
            }
        }
    }
}

@Composable
fun CastItem(
    cast: Cast
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(String.format(IMAGE_URL, cast.profilePath))
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
        )
        Text(
            text = cast.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 5.dp)
        )
        Text(
            text = cast.character,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun CrewItem(
    crew: Crew
) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(String.format(IMAGE_URL, crew.profilePath))
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(90.dp)
                .height(90.dp)
        )
        Text(
            text = crew.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 5.dp)
        )
        Text(
            text = crew.job,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SimilarMovies(movies: List<MovieEntity>) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.similar_movies),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 10.dp)
        )
        LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
            items(
                count = movies.size
            ) { index ->
                SimilarMovieItem(movies[index])
            }
        }
    }
}

@Composable
fun SimilarMovieItem(movie: MovieEntity) {
    Card(
        modifier = Modifier.wrapContentSize().padding(horizontal = 10.dp),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(String.format(IMAGE_URL, movie.posterPath))
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}
