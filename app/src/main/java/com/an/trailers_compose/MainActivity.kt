package com.an.trailers_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import com.an.trailers_compose.AppConstants.ROUTE_DETAIL_ARG_NAME
import com.an.trailers_compose.AppConstants.ROUTE_MOVIE_DETAIL_PATH
import com.an.trailers_compose.AppConstants.ROUTE_TV_DETAIL_PATH
import com.an.trailers_compose.AppConstants.TV
import com.an.trailers_compose.ui.detail.movie.MovieDetailScreen
import com.an.trailers_compose.ui.detail.movie.MovieDetailViewModel
import com.an.trailers_compose.ui.detail.tv.TvDetailScreen
import com.an.trailers_compose.ui.detail.tv.TvDetailViewModel
import com.an.trailers_compose.ui.list.movie.MovieListScreen
import com.an.trailers_compose.ui.list.movie.MovieListViewModel
import com.an.trailers_compose.ui.list.tv.TvListScreen
import com.an.trailers_compose.ui.list.tv.TvListViewModel
import com.an.trailers_compose.ui.theme.TrailersComposeTheme
import com.an.trailers_compose.utils.navigateToMovieDetail
import com.an.trailers_compose.utils.navigateToTvDetail
import com.an.trailers_compose.utils.navigateToVideo
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

                val movieListViewModel = hiltViewModel<MovieListViewModel>()
                val movies = movieListViewModel.movies.collectAsLazyPagingItems()
                val selectedMovieCategory = movieListViewModel.selectedCategory.collectAsStateWithLifecycle(
                    lifecycleOwner = LocalLifecycleOwner.current
                )

                val tvListViewModel = hiltViewModel<TvListViewModel>()
                val tvList = tvListViewModel.tvSeriesList.collectAsLazyPagingItems()
                val selectedTvCategory = tvListViewModel.selectedCategory.collectAsStateWithLifecycle(
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
                                onItemClicked = { navController.navigateToMovieDetail(it) },
                                selectedCategory = selectedMovieCategory.value,
                                onCategorySelected = {
                                    movieListViewModel.updateCategory(it)
                                    movies.refresh()
                                },
                                onTvMenuSelected = { navController.navigate(TV) },
                                animatedContentScope = this@composable
                            )
                        }
                        composable(
                            route = ROUTE_MOVIE_DETAIL_PATH,
                            arguments = listOf(
                                navArgument(ROUTE_DETAIL_ARG_NAME) { type = NavType.LongType },
                            )
                        ) {
                            val context = LocalContext.current
                            MovieDetailScreen(
                                viewModel = hiltViewModel<MovieDetailViewModel>(),
                                onItemClicked = { navController.navigateToMovieDetail(it) },
                                onVideoItemClicked = { navigateToVideo(context, it) },
                                animatedContentScope = this@composable
                            )
                        }
                        composable(route = TV) {
                            TvListScreen(
                                tvSeriesList = tvList,
                                onItemClicked = { navController.navigateToTvDetail(it) },
                                selectedCategory = selectedTvCategory.value,
                                onCategorySelected = {
                                    tvListViewModel.updateCategory(it)
                                    tvList.refresh()
                                },
                                onMenuItemSelected = { navController.navigate(MOVIES) },
                                animatedContentScope = this@composable
                            )
                        }
                        composable(
                            route = ROUTE_TV_DETAIL_PATH,
                            arguments = listOf(
                                navArgument(ROUTE_DETAIL_ARG_NAME) { type = NavType.LongType },
                            )
                        ) {
                            val context = LocalContext.current
                            TvDetailScreen(
                                viewModel = hiltViewModel<TvDetailViewModel>(),
                                onItemClicked = { navController.navigateToTvDetail(it) },
                                onVideoItemClicked = { navigateToVideo(context, it) },
                                animatedContentScope = this@composable
                            )
                        }
                    }
                }
            }
        }
    }
}


