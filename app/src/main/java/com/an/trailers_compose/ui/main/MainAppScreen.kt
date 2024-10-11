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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.an.trailers_compose.ui.list.movie.MovieListScreen
import com.an.trailers_compose.ui.list.tv.TvListScreen
import com.an.trailers_compose.ui.theme.TrailersComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(navController: NavHostController) {
    TrailersComposeTheme {
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState()
        )
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val isRootScreen by remember(currentBackStackEntry) {
            derivedStateOf { navController.previousBackStackEntry == null }
        }

        Scaffold(
            // Adding this line in order to draw the screen over the status bar
            contentWindowInsets = WindowInsets.navigationBars,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                MainTopAppBar(
                    navController = navController,
                    showBackButton = !isRootScreen,
                    scrollBehavior = scrollBehavior
                )
            },
            bottomBar = {
                MainAppBottomBar(
                    navController = navController
                )
            }
        ) { innerPadding ->

            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Movies.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(BottomNavItem.Movies.route) {
                    MovieListScreen()
                }
                composable(BottomNavItem.Tv.route) {
                    TvListScreen()
                }
            }
        }
    }
}