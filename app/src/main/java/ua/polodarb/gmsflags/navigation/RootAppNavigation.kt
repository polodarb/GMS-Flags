@file:OptIn(ExperimentalAnimationApi::class)

package ua.polodarb.gmsflags.navigation

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.flagsChange.navigation.addFlagListComposable
import ua.polodarb.flagsChange.navigation.flagChangeComposable
import ua.polodarb.flagsfile.navigation.loadFileComposable
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.MainActivity
import ua.polodarb.gmsflags.navigation.anim.enterAnim
import ua.polodarb.gmsflags.navigation.anim.exitAnim
import ua.polodarb.onboarding.navigation.notificationRequestComposable
import ua.polodarb.onboarding.navigation.rootRequestComposable
import ua.polodarb.onboarding.navigation.welcomeComposable
import ua.polodarb.platform.init.InitShell
import ua.polodarb.settings.navigation.settingsAboutComposable
import ua.polodarb.settings.navigation.settingsChangeNavigationComposable
import ua.polodarb.settings.navigation.settingsComposable
import ua.polodarb.settings.navigation.settingsResetFlagsComposable
import ua.polodarb.settings.navigation.settingsResetSavedComposable

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
internal fun RootAppNavigation(
    modifier: Modifier = Modifier,
    isFirstStart: Boolean,
    loadFlagIntent: Intent?,
    navController: NavHostController
) {

    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val mainActivity = context as MainActivity
    val hapticFeedback = LocalHapticFeedback.current

    // RootRequestScreen states
    var isButtonLoading by rememberSaveable { mutableStateOf(false) }

    NavHost(
        navController = navController,
        startDestination = if (isFirstStart) {
            ScreensDestination.Welcome.screenRoute
        } else if (isLoadFileIntent(loadFlagIntent)) {
            ScreensDestination.LoadFile.screenRoute
        } else {
            ScreensDestination.Root.screenRoute
        },
        modifier = modifier
    ) {
        rootComposable(
            navController = navController,
            isFirstStart = isFirstStart,
            loadFlagIntent = loadFlagIntent
        )

        welcomeComposable(
            route = ScreensDestination.Welcome.screenRoute,
            onStart = {
                navController.navigate(ScreensDestination.RootRequest.screenRoute)
            },
            openLink = {
                uriHandler.openUri(it)
            }
        )

        rootRequestComposable(
            route = ScreensDestination.RootRequest.screenRoute,
            onExit = {
                context.finish()
            },
            onRootRequest = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                isButtonLoading = true

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        InitShell.initShell()

                        if (Shell.getShell().isRoot) {
                            withContext(Dispatchers.Main) {
                                mainActivity.rootDBInitializer.initDB()
                                delay(700)
                                navController.navigate(ScreensDestination.NotificationRequest.screenRoute)
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                delay(150)
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                isButtonLoading = false
                                Toast.makeText(mainActivity, "ROOT IS DENIED!", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } catch (_: Exception) { }
                }
            },
            isButtonLoading = isButtonLoading
        )

        notificationRequestComposable(
            route = ScreensDestination.NotificationRequest.screenRoute,
            onSkip = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                mainActivity.setFirstLaunch()
                Toast.makeText(mainActivity, R.string.notifications_toast, Toast.LENGTH_SHORT).show()
                navController.navigate(ScreensDestination.Root.screenRoute)
            },
            onNotificationRequest = {
                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                mainActivity.setFirstLaunch()
                navController.navigate(ScreensDestination.Root.screenRoute)
            }
        )

        flagChangeComposable(
            route = ScreensDestination.FlagChange.createStringRoute(ScreensDestination.Packages.screenRoute),
            onBackPressed = navController::navigateUp,
            onAddMultipleFlags = {
                navController.navigate(
                    ScreensDestination.AddFlagList.createRoute(Uri.encode(it))
                )
            },
        )

        addFlagListComposable(
            route = ScreensDestination.AddFlagList.createStringRoute(ScreensDestination.FlagChange.screenRoute),
            onBackPressed = navController::navigateUp
        )

        settingsComposable(
            route = ScreensDestination.Settings.screenRoute,
            onBackPressed = navController::navigateUp,
            onResetFlagsClick = {
                navController.navigate(ScreensDestination.SettingsResetFlags.screenRoute)
            },
            onResetSavedClick = {
                navController.navigate(ScreensDestination.SettingsResetSaved.screenRoute)
            },
            onChangeNavigationClick = {
                navController.navigate(ScreensDestination.SettingsChangeNavigation.screenRoute)
            },
            onAboutClick = {
                navController.navigate(ScreensDestination.SettingsAbout.screenRoute)
            }
        )

        settingsResetFlagsComposable(
            route = ScreensDestination.SettingsResetFlags.screenRoute,
            onBackPressed = navController::navigateUp
        )

        settingsResetSavedComposable(
            route = ScreensDestination.SettingsResetSaved.screenRoute,
            onBackPressed = navController::navigateUp
        )

        settingsChangeNavigationComposable(
            route = ScreensDestination.SettingsChangeNavigation.screenRoute,
            parentSettingsRoute = NavBarItem.Suggestions.screenRoute,
            onBackPressed = navController::navigateUp
        )

        settingsAboutComposable(
            route = ScreensDestination.SettingsAbout.screenRoute,
            onBackPressed = navController::navigateUp
        )

    }
}

@OptIn(ExperimentalAnimationApi::class)
private fun NavGraphBuilder.rootComposable(
    navController: NavHostController,
    isFirstStart: Boolean,
    loadFlagIntent: Intent?
) {
    if (!isFirstStart && isLoadFileIntent(loadFlagIntent)) {
        loadFileComposable(
            route = ScreensDestination.LoadFile.screenRoute,
            fileUri = loadFlagIntent?.data
        )
    } else {
        composable(
            route = ScreensDestination.Root.screenRoute,
            enterTransition = { enterAnim(toLeft = true) },
            exitTransition = { exitAnim(toLeft = true) },
            popEnterTransition = { enterAnim(toLeft = false) },
            popExitTransition = { exitAnim(toLeft = false) }
        ) {
            RootScreen(isFirstStart = isFirstStart, parentNavController = navController)
        }
    }
}

fun isLoadFileIntent(intent: Intent?): Boolean {
    return intent != null && intent.action == Intent.ACTION_VIEW && intent.type == "application/xml"
}