package com.an.trailers_compose.ui.detail.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.an.trailers_compose.R
import com.an.trailers_compose.data.remote.model.Video
import com.an.trailers_compose.data.remote.model.youtubeUrl

@Composable
fun TrailersCard(
    videos: List<Video>,
    onVideoItemClicked: (key: String) -> Unit
) {
    LazyRow(modifier = Modifier) {
        items(
            count = videos.size
        ) { index ->
            Box(
                modifier = Modifier.fillParentMaxSize(0.7f)
            ) {
                TrailersListItem(
                    video = videos[index],
                    onVideoItemClicked = onVideoItemClicked
                )
            }
        }
    }
}

@Composable
fun TrailersListItem(
    video: Video,
    onVideoItemClicked: (key: String) -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(ratio = 16f/9f)
            .padding(horizontal = 10.dp)
            .clickable { onVideoItemClicked(video.key) }
        ,
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(20.dp)
    ) {
        Box(modifier = Modifier) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(video.youtubeUrl())
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