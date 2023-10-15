package ua.polodarb.gmsflags.ui.navigation

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.ui.MainActivity
import ua.polodarb.gmsflags.ui.animations.enterAnim
import ua.polodarb.gmsflags.ui.animations.exitAnim
import ua.polodarb.gmsflags.ui.screens.RootScreen
import ua.polodarb.gmsflags.ui.screens.firstStartScreens.RootRequestScreen
import ua.polodarb.gmsflags.ui.screens.firstStartScreens.WelcomeScreen
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreen
import ua.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreen
import ua.polodarb.gmsflags.ui.screens.settingsScreen.SettingsScreen
import ua.polodarb.gmsflags.ui.screens.settingsScreen.about.AboutScreen
import ua.polodarb.gmsflags.ui.screens.settingsScreen.resetFlags.ResetFlagsScreen
import ua.polodarb.gmsflags.ui.screens.settingsScreen.resetSaved.ResetSavedScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun RootAppNavigation(
    modifier: Modifier = Modifier,
    activity: MainActivity,
    isFirstStart: Boolean,
    navController: NavHostController
) {
    val uriHandler = LocalUriHandler.current
    val appContext = koinInject<Context>()
    val activityContext = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val isButtonLoading = rememberSaveable {
        mutableStateOf(false)
    }

    NavHost(
        navController = navController,
        startDestination = if (isFirstStart) ScreensDestination.Welcome.screenRoute else ScreensDestination.Root.screenRoute,
        modifier = modifier
    ) {
        composable(
            route = ScreensDestination.Root.screenRoute,
            enterTransition = { enterAnim(toLeft = false) },
            exitTransition = { exitAnim(toLeft = true) }
        ) {
            RootScreen(isFirstStart = isFirstStart, parentNavController = navController)
        }
        composable(
            route = ScreensDestination.Welcome.screenRoute,
            enterTransition = { enterAnim(toLeft = false) },
            exitTransition = { exitAnim(toLeft = true) },
        ) {
            WelcomeScreen(
                onStart = {
                    navController.navigate(ScreensDestination.RootRequest.screenRoute)
                },
                openLink = {
                    uriHandler.openUri(it)
                }
            )
        }
        composable(
            route = ScreensDestination.RootRequest.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false) }
        ) {
            RootRequestScreen(
                onExit = { activity.finish() },
                onRootRequest = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    isButtonLoading.value = true
                    try {
                        (appContext.applicationContext as GMSApplication).initShell()
                    } catch (_: Exception) {
                    }

                    if (Shell.getShell().isRoot) {
                        (appContext.applicationContext as GMSApplication).initDB()
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(700)
                            activity.setFirstLaunch()
                            navController.navigate(ScreensDestination.Root.screenRoute)
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            delay(150)
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                        isButtonLoading.value = false
                        Toast.makeText(activityContext, "ROOT IS DENIED!", Toast.LENGTH_SHORT)
                            .show()
                    }
                },
                isButtonLoading = isButtonLoading.value
            )
        }

        // Flag change
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

        // Settings
        composable(
            route = ScreensDestination.Settings.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = true) },
            popEnterTransition = { enterAnim(toLeft = false) },
            popExitTransition = { exitAnim(toLeft = false) }
        ) {
            SettingsScreen(
                onBackPressed = navController::navigateUp,
                onResetFlagsClick = {
                    navController.navigate(ScreensDestination.SettingsResetFlags.screenRoute)
                },
                onResetSavedClick = {
                    navController.navigate(ScreensDestination.SettingsResetSaved.screenRoute)
                },
                onAboutClick = {
                    navController.navigate(ScreensDestination.SettingsAbout.screenRoute)
                }
            ) // TODO: Implement SettingsScreen
        }

        // Settings - Reset Flags
        composable(
            route = ScreensDestination.SettingsResetFlags.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false) },
        ) {
            ResetFlagsScreen(
                onBackPressed = navController::navigateUp
            )
        }

        // Settings - Reset Saved
        composable(
            route = ScreensDestination.SettingsResetSaved.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false) },
        ) {
            ResetSavedScreen(
                onBackPressed = navController::navigateUp
            )
        }

        // Settings - About & Support
        composable(
            route = ScreensDestination.SettingsAbout.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = false) },
        ) {
            AboutScreen(
                onBackPressed = navController::navigateUp
            )
        }

        // Packages list
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
