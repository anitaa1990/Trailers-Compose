package com.an.trailers_compose.ui.main

import androidx.annotation.DrawableRes
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.an.trailers_compose.AppConstants.MOVIES
import com.an.trailers_compose.AppConstants.TV
import com.an.trailers_compose.R

@Composable
fun MainAppBottomBar(
    navController: NavHostController
) {
    BottomAppBar(
        modifier = Modifier.shadow(elevation = 12.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        val screens = listOf(BottomNavItem.Movies, BottomNavItem.Tv)

        NavigationBar(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            screens.forEach { bottomNavItem ->
                NavigationBarItem(
                    alwaysShowLabel = false,
                    icon = {
                        Icon(
                            painter = painterResource(id = bottomNavItem.icon),
                            contentDescription = bottomNavItem.route
                        )
                    },
                    selected = currentRoute == bottomNavItem.route,
                    onClick = {
                        navController.navigate(bottomNavItem.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults
                        .colors(
                            selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer
                        )
                )
            }
        }
    }
}

sealed class BottomNavItem(val route: String, @DrawableRes val icon: Int) {
    data object Movies : BottomNavItem(MOVIES, R.drawable.ic_movie)
    data object Tv : BottomNavItem(TV, R.drawable.ic_tv)
}