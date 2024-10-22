package com.an.trailers_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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

                NavHost(
                    navController = navController,
                    startDestination = MOVIES,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(
                        route = MOVIES,
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                            )
                        },
                        exitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                            )
                        },
                        popEnterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                            )
                        }
                    ) {
                        MovieListScreen(
                            movies = movies,
                            onItemClicked = { navController.navigateToDetail(it) },
                            selectedCategory = selectedCategory.value,
                            onCategorySelected = {
                                viewModel.updateCategory(it)
                                movies.refresh()
                            }
                        )
                    }
                    composable(
                        route = AppConstants.ROUTE_MOVIE_DETAIL_PATH,
                        arguments = listOf(
                            navArgument(AppConstants.ROUTE_DETAIL_ARG_NAME) { type = NavType.LongType },
                        ),
                        enterTransition = {
                            return@composable slideIntoContainer(
                                AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
                            )
                        },
                        popExitTransition = {
                            return@composable slideOutOfContainer(
                                AnimatedContentTransitionScope.SlideDirection.End, tween(700)
                            )
                        }
                    ) {
                        MovieDetailScreen(
                            viewModel = hiltViewModel<MovieDetailViewModel>(),
                            onItemClicked = { navController.navigateToDetail(it) }
                        )
                    }
                }
            }
        }
    }
}


