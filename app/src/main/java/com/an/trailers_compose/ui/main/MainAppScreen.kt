package com.an.trailers_compose.ui.main

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.paging.compose.LazyPagingItems
import com.an.trailers_compose.data.local.entity.MovieEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    movies: LazyPagingItems<MovieEntity>,
) {

}