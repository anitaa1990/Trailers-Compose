package com.an.trailers_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.an.trailers_compose.AppConstants.MOVIES
import com.an.trailers_compose.ui.detail.movie.MovieDetailScreen
import com.an.trailers_compose.ui.detail.movie.MovieDetailViewModel
import com.an.trailers_compose.ui.list.movie.MovieListScreen
import com.an.trailers_compose.ui.list.movie.MovieListViewModel
import com.an.trailers_compose.ui.theme.TrailersComposeTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrailersComposeTheme {
                val navController = rememberNavController()

                val viewModel = hiltViewModel<MovieListViewModel>()
                val movies = viewModel.getMovies().collectAsLazyPagingItems()

                NavHost(
                    navController = navController,
                    startDestination = MOVIES
                ) {
                    composable(MOVIES) {
                        MovieListScreen(
                            movies = movies,
                            onItemClicked = { id ->
                                navController.navigate(
                                    route = AppConstants.ROUTE_MOVIE_DETAIL_PATH.replace(
                                        "{${AppConstants.ROUTE_DETAIL_ARG_NAME}}",
                                        "$id"
                                    )
                                )
                            }
                        )
                    }
                    composable(
                        route = AppConstants.ROUTE_MOVIE_DETAIL_PATH,
                        arguments = listOf(
                            navArgument(AppConstants.ROUTE_DETAIL_ARG_NAME) { type = NavType.LongType },
                        ),
                    ) {
                        MovieDetailScreen(hiltViewModel<MovieDetailViewModel>())
                    }
                }
            }
        }
    }
}


