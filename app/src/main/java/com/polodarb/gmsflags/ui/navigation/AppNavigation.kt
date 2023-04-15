package com.polodarb.gmsflags.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.polodarb.gmsflags.R
import com.polodarb.gmsflags.ui.screens.appsScreen.AppsScreen
import com.polodarb.gmsflags.ui.screens.historyScreen.HistoryScreen
import com.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreen
import com.polodarb.gmsflags.ui.screens.savedScreen.SavedScreen

internal sealed class NavBarItem(var title: String, var icon: Int, var screenRoute: String) {

    object Packages : NavBarItem("Packages", R.drawable.ic_navbar_packages, "packages")
    object Apps : NavBarItem("Apps", R.drawable.ic_navbar_apps, "apps")
    object Saved : NavBarItem("Saved", R.drawable.ic_save_inactive, "saved")
    object History : NavBarItem("History", R.drawable.ic_navbar_history, "history")

}

internal sealed class ScreensDestination(var screenRoute: String) {

    fun createStringRoute(rootRoute: String) = "${rootRoute}/$screenRoute"

    object Root : ScreensDestination("root")
    object Suggestions : ScreensDestination("suggest")
    object FlagChange : ScreensDestination("{flagChange}") {
        fun createRoute(flagChange: String): String {
            return "packages/$flagChange"
        }
    }

    object Settings : ScreensDestination("settings")
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun BottomBarNavigation( // Navigation realization for BottomBar
    parentNavController: NavController,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = NavBarItem.Packages.screenRoute,
        modifier = modifier
    ) {
        composable(route = NavBarItem.Packages.screenRoute,
            enterTransition = {
                when (targetState.destination.route) {
                    NavBarItem.Packages.screenRoute -> fadeIn(animationSpec = tween(250))
                    ScreensDestination.Settings.screenRoute -> slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(700)
                    )

                    else -> null
                }
            }) {
            PackagesScreen(
                onFlagClick = { packageName ->
                    parentNavController.navigate(
                        ScreensDestination.FlagChange.createRoute(
                            packageName
                        )
                    )
                },
                onSuggestionsClick = {
                    parentNavController.navigate(ScreensDestination.Suggestions.screenRoute)
                },
                onSettingsClick = {
                    parentNavController.navigate(ScreensDestination.Settings.screenRoute)
                }
            )
        }
        composable(route = NavBarItem.Apps.screenRoute,
            enterTransition = {
                fadeIn(animationSpec = tween(250))
            }) {
            AppsScreen()
        }
        composable(route = NavBarItem.Saved.screenRoute, enterTransition = {
            fadeIn(animationSpec = tween(250))
        }) {
            SavedScreen()
        }
        composable(route = NavBarItem.History.screenRoute,
            enterTransition = {
                fadeIn(animationSpec = tween(250))
            }) {
            HistoryScreen()
        }
    }
}