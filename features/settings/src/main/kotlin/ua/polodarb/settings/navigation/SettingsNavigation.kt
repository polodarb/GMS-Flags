package ua.polodarb.settings.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import ua.polodarb.settings.SettingsScreen
import ua.polodarb.settings.screens.about.AboutScreen
import ua.polodarb.settings.screens.resetFlags.ResetFlagsScreen
import ua.polodarb.settings.screens.resetSaved.ResetSavedScreen
import ua.polodarb.settings.screens.startRoute.ChangeNavigationScreen
import ua.polodarb.ui.animations.enterAnim
import ua.polodarb.ui.animations.exitAnim

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsComposable(
    route: String,
    onBackPressed: () -> Unit,
    onResetFlagsClick: () -> Unit,
    onResetSavedClick: () -> Unit,
    onChangeNavigationClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = true) },
        popEnterTransition = { enterAnim(toLeft = false) },
        popExitTransition = { exitAnim(toLeft = false) }
    ) {
        SettingsScreen(
            onBackPressed = onBackPressed,
            onResetFlagsClick = onResetFlagsClick,
            onResetSavedClick = onResetSavedClick,
            onChangeNavigationClick = onChangeNavigationClick,
            onAboutClick = onAboutClick
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsResetFlagsComposable(
    route: String,
    onBackPressed: () -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = false) },
    ) {
        ResetFlagsScreen(
            onBackPressed = onBackPressed
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsResetSavedComposable(
    route: String,
    onBackPressed: () -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = false) },
    ) {
        ResetSavedScreen(
            onBackPressed = onBackPressed
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsChangeNavigationComposable(
    route: String,
    parentSettingsRoute: String,
    onBackPressed: () -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = false) },
    ) {
        ChangeNavigationScreen(
            screenRoute = parentSettingsRoute,
            onBackPressed = onBackPressed
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.settingsAboutComposable(
    route: String,
    onBackPressed: () -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = false) },
    ) {
        AboutScreen(
            onBackPressed = onBackPressed
        )
    }
}