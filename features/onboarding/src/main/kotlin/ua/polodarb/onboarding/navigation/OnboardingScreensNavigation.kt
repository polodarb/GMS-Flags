package ua.polodarb.onboarding.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.onboarding.RequestNotificationPermissionScreen
import ua.polodarb.onboarding.RootRequestScreen
import ua.polodarb.onboarding.WelcomeScreen
import ua.polodarb.ui.animations.enterAnim
import ua.polodarb.ui.animations.exitAnim

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.welcomeComposable(
    route: String,
    onStart: () -> Unit,
    openLink: (String) -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = false) },
        exitTransition = { exitAnim(toLeft = true) },
    ) {
        WelcomeScreen(
            onStart = onStart,
            openLink = openLink
        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.rootRequestComposable(
    route: String,
    onExit: () -> Unit,
    onRootRequest: () -> Unit,
    isButtonLoading: Boolean
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = true) },
        popEnterTransition = { enterAnim(toLeft = false) },
        popExitTransition = { exitAnim(toLeft = false) }
    ) {

        RootRequestScreen(
            onExit = onExit,
            onRootRequest = onRootRequest,
            isButtonLoading = isButtonLoading
        )
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.notificationRequestComposable(
    route: String,
    onSkip: () -> Unit,
    onNotificationRequest: () -> Unit
) {
    composable(
        route = route,
        enterTransition = { enterAnim(toLeft = true) },
        exitTransition = { exitAnim(toLeft = true) },
        popEnterTransition = { enterAnim(toLeft = false) },
        popExitTransition = { exitAnim(toLeft = false) }
    ) {
        RequestNotificationPermissionScreen(
            onSkip = onSkip,
            onNotificationRequest = onNotificationRequest
        )
    }
}
