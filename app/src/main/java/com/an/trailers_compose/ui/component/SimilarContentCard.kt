package com.an.trailers_compose.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.an.trailers_compose.ui.model.SimilarContent

@Composable
fun SimilarContentCard(
    @StringRes similarContentTitleId: Int,
    similarContent: List<SimilarContent>,
    onItemClicked: (remoteId: Long) -> Unit
) {
    Column {
        Text(
            text = stringResource(id = similarContentTitleId),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
        )
        LazyRow(modifier = Modifier.padding(vertical = 10.dp)) {
            items(
                count = similarContent.size
            ) { index ->
                SimilarContentListItem(similarContent[index], onItemClicked)
            }
        }
    }
}

@Composable
private fun SimilarContentListItem(
    content: SimilarContent,
    onItemClicked: (remoteId: Long) -> Unit
) {
    Card(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 10.dp)
            .clickable { onItemClicked(content.id) },
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(content.posterUrl)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop
        )
    }
}
