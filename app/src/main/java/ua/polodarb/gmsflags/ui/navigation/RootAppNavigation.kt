package ua.polodarb.gmsflags.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import ua.polodarb.gmsflags.ui.animations.enterAnim
import ua.polodarb.gmsflags.ui.animations.exitAnim
import ua.polodarb.gmsflags.ui.screens.RootScreen
import ua.polodarb.gmsflags.ui.screens.settingsScreen.SettingsScreen
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreen
import ua.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = ScreensDestination.Root.screenRoute,
        modifier = modifier
    ) {
        composable(
            route = ScreensDestination.Root.screenRoute,
            enterTransition = { enterAnim(toLeft = false) },
            exitTransition = { exitAnim(toLeft = true)},
        ) {
            RootScreen(parentNavController = navController)
        }
        composable(
            route = ScreensDestination.FlagChange.createStringRoute(ScreensDestination.Packages.screenRoute),
            arguments = listOf(navArgument("flagChange") { type = NavType.StringType }),
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false)}
        ) { backStackEntry ->
            FlagChangeScreen(
                onBackPressed = navController::navigateUp,
                packageName = backStackEntry.arguments?.getString("flagChange")
            )
        }
        composable(
            route = ScreensDestination.Settings.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false)},
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
                        ScreensDestination.FlagChange.createRoute(packageName)
                    )
                },
                onBackPressed = navController::navigateUp
            ) // TODO: Implement PackagesScreen
        }
    }
}
