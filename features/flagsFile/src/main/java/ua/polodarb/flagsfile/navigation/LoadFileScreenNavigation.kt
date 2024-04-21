package ua.polodarb.flagsfile.navigation

import android.net.Uri
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.polodarb.flagsfile.LoadFileScreen
import ua.polodarb.ui.animations.enterAnim
import ua.polodarb.ui.animations.exitAnim

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loadFileComposable(
    route: String,
    fileUri: Uri?,
    onExit: () -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = true) },
        popEnterTransition = { enterAnim(toLeft = false) },
        popExitTransition = { exitAnim(toLeft = false) }
    ) {
        LoadFileScreen(
            fileUri = fileUri,
            onExit = onExit
        )
    }
}