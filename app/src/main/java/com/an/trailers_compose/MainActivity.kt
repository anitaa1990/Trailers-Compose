package com.an.trailers_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.an.trailers_compose.utils.navigateToDetail
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrailersComposeTheme {
                val navController = rememberNavController()

                val viewModel = hiltViewModel<MovieListViewModel>()
                val movies = viewModel.movies.collectAsLazyPagingItems()
                val selectedCategory = viewModel.selectedCategory.collectAsStateWithLifecycle(
                    lifecycleOwner = LocalLifecycleOwner.current
                )
                SharedTransitionLayout {
                    NavHost(
                        navController = navController,
                        startDestination = MOVIES,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        composable(route = MOVIES) {
                            MovieListScreen(
                                movies = movies,
                                onItemClicked = { navController.navigateToDetail(it) },
                                selectedCategory = selectedCategory.value,
                                onCategorySelected = {
                                    viewModel.updateCategory(it)
                                    movies.refresh()
                                },
                                animatedContentScope = this@composable
                            )
                        }
                        composable(
                            route = AppConstants.ROUTE_MOVIE_DETAIL_PATH,
                            arguments = listOf(
                                navArgument(AppConstants.ROUTE_DETAIL_ARG_NAME) { type = NavType.LongType },
                            )
                        ) {
                            MovieDetailScreen(
                                viewModel = hiltViewModel<MovieDetailViewModel>(),
                                onItemClicked = { navController.navigateToDetail(it) },
                                animatedContentScope = this@composable
                            )
                        }
                    }
                }
            }
        }
    }
}


