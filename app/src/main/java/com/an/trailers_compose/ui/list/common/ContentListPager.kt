package com.an.trailers_compose.ui.list.common

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.an.trailers_compose.AppConstants.KEY_SHARED_TRANSITION_DESC
import com.an.trailers_compose.AppConstants.KEY_SHARED_TRANSITION_IMAGE
import com.an.trailers_compose.AppConstants.KEY_SHARED_TRANSITION_TITLE
import com.an.trailers_compose.ui.model.Content
import kotlin.math.absoluteValue
import kotlin.math.sqrt

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SharedTransitionScope.CircleRevealPager(
    contentList: LazyPagingItems<Content>,
    onItemClicked: (remoteId: Long) -> Unit,
    animatedContentScope: AnimatedContentScope
) {
    val state = rememberPagerState(pageCount = { contentList.itemCount } )
    var offsetY by remember { mutableFloatStateOf(0f) }

    val imageRequest = ImageRequest.Builder(LocalContext.current)
    HorizontalPager(
        modifier = Modifier
            .pointerInteropFilter {
                offsetY = it.y
                false
            },
        state = state,
        pageSize = PageSize.Fill
    ) { page ->
        contentList[page]?.let { content ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onItemClicked(content.id) }
                    .graphicsLayer {
                        val pageOffset = state.offsetForPage(page)
                        translationX = size.width * pageOffset

                        val endOffset = state.endOffsetForPage(page)

                        shadowElevation = 20f
                        shape = CirclePath(
                            progress = 1f - endOffset.absoluteValue,
                            origin = Offset(
                                size.width,
                                offsetY,
                            )
                        )
                        clip = true

                        val absoluteOffset = state.offsetForPage(page).absoluteValue
                        val scale = 1f + (absoluteOffset.absoluteValue * .4f)

                        scaleX = scale
                        scaleY = scale

                        val startOffset = state.startOffsetForPage(page)
                        alpha = (2f - startOffset) / 2f

                    },
                contentAlignment = Alignment.Center,
            ) {
                if(page == 0) {
                    imageRequest.crossfade(800)
                        .memoryCachePolicy(CachePolicy.DISABLED)
                        .diskCachePolicy(CachePolicy.DISABLED)
                } else {
                    imageRequest.crossfade(false)
                        .memoryCachePolicy(CachePolicy.ENABLED)
                        .diskCachePolicy(CachePolicy.ENABLED)
                }
                val imageUrl = content.posterUrl
                AsyncImage(
                    model = imageRequest
                        .data(imageUrl)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .sharedElement(rememberSharedContentState(
                                key = String.format(KEY_SHARED_TRANSITION_IMAGE, imageUrl)
                        ), animatedVisibilityScope = animatedContentScope)
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .fillMaxHeight(.8f)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.Black.copy(alpha = 0f),
                                    Color.Black.copy(alpha = .7f),
                                )
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Text(
                        text = content.title,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier.sharedElement(rememberSharedContentState(
                            key = String.format(KEY_SHARED_TRANSITION_TITLE, content.id)
                        ), animatedVisibilityScope = animatedContentScope)
                    )
                    Box(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.White)
                    )
                    Text(
                        modifier = Modifier
                            .padding(vertical = 10.dp)
                            .sharedElement(rememberSharedContentState(
                                key = String.format(KEY_SHARED_TRANSITION_DESC, content.id)
                            ), animatedVisibilityScope = animatedContentScope),
                        text = content.description,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

private class CirclePath(
    private val progress: Float,
    private val origin: Offset = Offset(0f, 0f)
) : Shape {
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
    ): Outline {

        val center = Offset(
            x = size.center.x - ((size.center.x - origin.x) * (1f - progress)),
            y = size.center.y - ((size.center.y - origin.y) * (1f - progress)),
        )
        val radius = (sqrt(
            size.height * size.height + size.width * size.width
        ) * .5f) * progress

        return Outline.Generic(Path().apply {
            addOval(
                Rect(
                    center = center,
                    radius = radius,
                )
            )
        })
    }
}

// ACTUAL OFFSET
fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

// OFFSET ONLY FROM THE LEFT
fun PagerState.startOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtLeast(0f)
}

// OFFSET ONLY FROM THE RIGHT
fun PagerState.endOffsetForPage(page: Int): Float {
    return offsetForPage(page).coerceAtMost(0f)
}
