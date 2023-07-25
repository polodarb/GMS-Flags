package ua.polodarb.gmsflags.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.core.currentScreenAsState

@Composable
fun BottomBarUI( // UI realization for BottomBar
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavigationBar {

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentSelectedItem by navController.currentScreenAsState()

        NavigationBarItem(
            icon = {
                Icon(
                    painter = painterResource(
                        if (currentSelectedItem == NavBarItem.Suggestions)
                            R.drawable.ic_navbar_suggestions_active
                        else
                            R.drawable.ic_navbar_suggestions_inactive
                    ),
                    contentDescription = "Suggestions"
                )
            },
            label = { Text(text = NavBarItem.Suggestions.title) },
            selected = currentSelectedItem == NavBarItem.Suggestions,
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
            selected = currentSelectedItem == NavBarItem.Apps,
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
                        if (currentSelectedItem == NavBarItem.Saved)
                            R.drawable.ic_save_active
                        else
                            R.drawable.ic_save_inactive
                    ),
                    contentDescription = "Saved"
                )
            },
            label = { Text(text = NavBarItem.Saved.title) },
            selected = currentSelectedItem == NavBarItem.Saved,
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
            selected = currentSelectedItem == NavBarItem.History,
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