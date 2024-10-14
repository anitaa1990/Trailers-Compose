package com.an.trailers_compose.ui.main

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.an.trailers_compose.ui.list.movie.MovieListScreen
import com.an.trailers_compose.ui.list.movie.MovieListViewModel
import com.an.trailers_compose.ui.list.tv.TvListScreen
import com.an.trailers_compose.ui.theme.TrailersComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(navController: NavHostController) {
    TrailersComposeTheme {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState()
        )

        Scaffold(
            // Adding this line in order to draw the screen over the status bar
            contentWindowInsets = WindowInsets.navigationBars,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                MainTopAppBar(
                    navController = navController,
                    showBackButton = false,
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                MainAppBottomBar(
                    navController = navController
                )
            }
        ) { innerPadding ->

            val viewModel = hiltViewModel<MovieListViewModel>()
            val movies = viewModel.getMovies().collectAsLazyPagingItems()

            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Movies.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Movies.route) {
                    MovieListScreen(movies)
                }
                composable(BottomNavItem.Tv.route) {
                    TvListScreen()
                }
            }
        }
    }
}