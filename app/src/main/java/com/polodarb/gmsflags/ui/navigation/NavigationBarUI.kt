package com.polodarb.gmsflags.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.android.material.color.MaterialColors
import com.polodarb.gmsflags.R

@Composable
fun BottomBarUI( // UI realization for BottomBar
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavigationBar(
//        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(
                        if (currentRoute == NavBarItem.Suggestions.screenRoute)
                            R.drawable.ic_navbar_suggestions_active
                        else
                            R.drawable.ic_navbar_suggestions_inactive
                    ),
                    contentDescription = "Suggestions"
                )
            },
            label = { Text(text = NavBarItem.Suggestions.title) },
            selected = currentRoute == NavBarItem.Suggestions.screenRoute,
            onClick = {
                navController.navigate(NavBarItem.Suggestions.screenRoute) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(NavBarItem.Apps.icon),
                    contentDescription = "Apps"
                )
            },
            label = { Text(text = NavBarItem.Apps.title) },
            selected = currentRoute == NavBarItem.Apps.screenRoute,
            onClick = {
                navController.navigate(NavBarItem.Apps.screenRoute) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },

        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(
                        if (currentRoute == NavBarItem.Saved.screenRoute)
                            R.drawable.ic_save_active
                        else
                            R.drawable.ic_save_inactive
                    ),
                    contentDescription = "Saved"
                )
            },
            label = { Text(text = NavBarItem.Saved.title) },
            selected = currentRoute == NavBarItem.Saved.screenRoute,
            onClick = {
                navController.navigate(NavBarItem.Saved.screenRoute) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(NavBarItem.History.icon),
                    contentDescription = "History"
                )
            },
            label = { Text(text = NavBarItem.History.title) },
            selected = currentRoute == NavBarItem.History.screenRoute,
            onClick = {
                navController.navigate(NavBarItem.History.screenRoute) {
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}