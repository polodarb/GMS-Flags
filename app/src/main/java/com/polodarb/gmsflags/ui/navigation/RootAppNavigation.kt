package com.polodarb.gmsflags.ui.navigation

import NavBarItem
import ScreensDestination
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.polodarb.gmsflags.ui.animations.enterAnimFade
import com.polodarb.gmsflags.ui.animations.enterAnimToRight
import com.polodarb.gmsflags.ui.animations.exitAnimFade
import com.polodarb.gmsflags.ui.animations.exitAnimToLeft
import com.polodarb.gmsflags.ui.animations.popEnterAnimToRight
import com.polodarb.gmsflags.ui.animations.popExitAnimToLeft
import com.polodarb.gmsflags.ui.screens.RootScreen
import com.polodarb.gmsflags.ui.screens.settingsScreen.SettingsScreen
import com.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreen
import com.polodarb.gmsflags.ui.screens.suggestionsScreen.SuggestionsScreen

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
        composable(
            route = ScreensDestination.Settings.screenRoute,
            enterTransition = { enterAnimToRight(initial = initialState, target = targetState) },
            exitTransition = { exitAnimToLeft(initial = initialState, target = targetState)},
            popEnterTransition = { enterAnimToRight(initial = initialState, target = targetState) },
            popExitTransition = { exitAnimToLeft(initial = initialState, target = targetState) }
        ) {
            SettingsScreen(
                onBackPressed = navController::navigateUp
            ) // TODO: Implement SettingsScreen
        }
        composable(
            route = ScreensDestination.Suggestions.screenRoute
        ) {
            SuggestionsScreen() // TODO: Implement SuggestionsScreen
        }
    }
}

