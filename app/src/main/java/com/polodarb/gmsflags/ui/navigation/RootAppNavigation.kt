package com.polodarb.gmsflags.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.polodarb.gmsflags.ui.screens.RootScreen
import com.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreen

@Composable
internal fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = ScreensDestination.Root.screenRoute,
        modifier = modifier
    ) {
        composable(
            route = ScreensDestination.Root.screenRoute,
            
        ) {
            RootScreen(parentNavController = navController)
        }
        composable(
            route = ScreensDestination.FlagChange.createStringRoute(NavBarItem.Packages.screenRoute),
            arguments =
            listOf(navArgument("flagChange") { type = NavType.StringType })
        ) { backStackEntry ->
            FlagChangeScreen(
                onBackPressed = navController::navigateUp,
                packageName = backStackEntry.arguments?.getString("flagChange")
            )
        }
    }
}