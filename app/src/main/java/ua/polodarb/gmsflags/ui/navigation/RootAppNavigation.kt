package ua.polodarb.gmsflags.ui.navigation

import android.net.Uri
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ua.polodarb.gmsflags.ui.animations.enterAnim
import ua.polodarb.gmsflags.ui.animations.exitAnim
import ua.polodarb.gmsflags.ui.screens.RootScreen
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreen
import ua.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreen
import ua.polodarb.gmsflags.ui.screens.settingsScreen.SettingsScreen

@OptIn(ExperimentalAnimationApi::class)
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
            enterTransition = { enterAnim(toLeft = false) },
            exitTransition = { exitAnim(toLeft = true) },
        ) {
            RootScreen(parentNavController = navController)
        }
        composable(
            route = ScreensDestination.FlagChange.createStringRoute(ScreensDestination.Packages.screenRoute),
            arguments = listOf(navArgument("flagChange") { type = NavType.StringType }),
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false) }
        ) { backStackEntry ->
            FlagChangeScreen(
                onBackPressed = navController::navigateUp,
                packageName = Uri.decode(backStackEntry.arguments?.getString("flagChange"))
            )
        }
        composable(
            route = ScreensDestination.Settings.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false) },
        ) {
            SettingsScreen(
                onBackPressed = navController::navigateUp
            ) // TODO: Implement SettingsScreen
        }
        composable(
            route = ScreensDestination.Packages.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = true) },
            popEnterTransition = { enterAnim(toLeft = false) },
            popExitTransition = { exitAnim(toLeft = false) }
        ) {
            PackagesScreen(
                onFlagClick = { packageName ->
                    navController.navigate(
                        ScreensDestination.FlagChange.createRoute(Uri.encode(packageName))
                    )
                },
                onBackPressed = navController::navigateUp
            ) // TODO: Implement PackagesScreen
        }
    }
}
