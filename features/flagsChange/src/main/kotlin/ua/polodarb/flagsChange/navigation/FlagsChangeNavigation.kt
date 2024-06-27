package ua.polodarb.flagsChange.navigation

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ua.polodarb.flagsChange.FlagChangeScreen
import ua.polodarb.flagsChange.extScreens.AddFlagList
import ua.polodarb.ui.animations.enterAnim
import ua.polodarb.ui.animations.exitAnim

private const val NAV_ARG_FLAG_CHANGE = "flagChange"
private const val NAV_ARG_ADD_FLAG_LIST = "addFlagList"

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.flagChangeComposable(
    route: String,
    onBackPressed: () -> Unit,
    onAddMultipleFlags: (packageName: String) -> Unit,
) {
    composable(
        route = route,
        arguments = listOf(navArgument(NAV_ARG_FLAG_CHANGE) { type = NavType.StringType }),
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = true) },
        popEnterTransition = { enterAnim(toLeft = false) },
        popExitTransition = { exitAnim(toLeft = false) }
    ) { backStackEntry ->
        FlagChangeScreen(
            onBackPressed = onBackPressed,
            packageName = Uri.decode(backStackEntry.arguments?.getString(NAV_ARG_FLAG_CHANGE)),
            onAddMultipleFlags = onAddMultipleFlags
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.addFlagListComposable(
    route: String,
    onBackPressed: () -> Unit,
) {
    composable(
        route = route,
        arguments = listOf(navArgument(NAV_ARG_ADD_FLAG_LIST) { type = NavType.StringType }),
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = true) },
        popEnterTransition = { enterAnim(toLeft = false) },
        popExitTransition = { exitAnim(toLeft = false) }
    ) { backStackEntry ->
        AddFlagList(
            onBackPressed = onBackPressed,
            packageName = Uri.decode(backStackEntry.arguments?.getString(NAV_ARG_ADD_FLAG_LIST))
        )
    }
}