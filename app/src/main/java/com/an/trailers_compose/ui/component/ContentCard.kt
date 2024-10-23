package com.an.trailers_compose.ui.component

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.an.trailers_compose.AppConstants
import com.an.trailers_compose.R
import com.an.trailers_compose.data.remote.model.Genre
import com.an.trailers_compose.ui.model.Artist
import com.an.trailers_compose.ui.model.Content
import com.an.trailers_compose.ui.theme.statusColor

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionScope.ContentCard(
    content: Content,
    animatedContentScope: AnimatedContentScope
) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 25.dp, top = 25.dp, end = 5.dp)
                .animateContentSize(
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = LinearOutSlowInEasing
                    )
                )
        ) {
            // Content title
            Text(
                text = content.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.sharedElement(rememberSharedContentState(
                    key = String.format(AppConstants.KEY_SHARED_TRANSITION_TITLE, content.id)
                ), animatedVisibilityScope = animatedContentScope)
            )
            // Content Genres
            LazyRow(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 10.dp)
            ) {
                items(
                    count = content.genres.size
                ) {
                    GenreItem(genre = content.genres[it], bgColor = content.genreBgColor)
                }
            }
            // Content Description
            ContentDescription(content = content, animatedContentScope = animatedContentScope)

            // Content status
            ContentStatus(status = content.status, runTime = content.durationInMins)

            // Read More section
            ExpandableCard{ CastCrewSection(content.cast, content.crew) }
        }
    }
}

@Composable
private fun GenreItem(
    genre: Genre,
    bgColor: Color
) {
    FilterChip(
        onClick = {  },
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = true,
            borderColor = bgColor,
            selectedBorderColor = bgColor
        ),
        shape = RoundedCornerShape(4.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = bgColor
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
private fun SharedTransitionScope.ContentDescription(
    content: Content,
    animatedContentScope: AnimatedContentScope
) {
    Text(
        text = content.description,
        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 25.sp),
        maxLines = 5,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Justify,
        modifier = Modifier
            .padding(top = 20.dp, bottom = 20.dp, end = 12.dp)
            .sharedElement(rememberSharedContentState(
                key = String.format(AppConstants.KEY_SHARED_TRANSITION_DESC, content.id)
            ), animatedVisibilityScope = animatedContentScope)
    )
}

@Composable
private fun ContentStatus(
    status: String,
    runTime: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Content Status
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
                    text = status,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                )
            },
            selected = true,
            modifier = Modifier.padding(end = 6.dp)
        )
        // Content runtime
        Text(
            text = runTime,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .align(Alignment.Bottom),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CastCrewSection(
    cast: List<Artist>,
    crew: List<Artist>
) {
    ArtistCard(
        titleId = R.string.cast,
        artists = cast
    )
    ArtistCard(
        titleId = R.string.crew,
        artists = crew
    )
}