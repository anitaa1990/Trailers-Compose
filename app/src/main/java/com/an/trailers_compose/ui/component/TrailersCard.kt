package com.an.trailers_compose.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.an.trailers_compose.AppConstants
import com.an.trailers_compose.R
import com.an.trailers_compose.data.remote.model.Video

@Composable
fun TrailersCard(
    videos: List<Video>
) {
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
                    .data(String.format(AppConstants.YOUTUBE_THUMBNAIL_URL, video.key))
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