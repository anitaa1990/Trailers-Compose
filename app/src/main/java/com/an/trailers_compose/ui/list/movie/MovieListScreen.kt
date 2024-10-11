package com.an.trailers_compose.ui.list.movie

import androidx.compose.runtime.Composable
import com.an.trailers_compose.R
import com.an.trailers_compose.ui.component.ProvideAppBarTitle
import com.an.trailers_compose.ui.component.TopBarTitle

@Composable
fun MovieListScreen() {
    // Toolbar title
    ProvideAppBarTitle { TopBarTitle(text = R.string.title_movies) }
}