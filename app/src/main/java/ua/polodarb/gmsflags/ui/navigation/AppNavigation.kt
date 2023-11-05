package ua.polodarb.gmsflags.ui.navigation

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.data.prefs.shared.PreferenceConstants
import ua.polodarb.gmsflags.data.prefs.shared.PreferencesManager
import ua.polodarb.gmsflags.ui.screens.apps.AppsScreen
import ua.polodarb.gmsflags.ui.screens.saved.SavedScreen
import ua.polodarb.gmsflags.ui.screens.suggestions.SuggestionsScreen

sealed class NavBarItem(
    @StringRes val title: Int,
    @DrawableRes val iconActive: Int,
    @DrawableRes val iconInactive: Int?,
    val screenRoute: String
) {
    data object Suggestions : NavBarItem(
        title = R.string.nav_bar_suggestions,
        iconActive = R.drawable.ic_navbar_suggestions_active,
        iconInactive = R.drawable.ic_navbar_suggestions_inactive,
        screenRoute = "suggestions"
    )

    data object Apps : NavBarItem(
        title = R.string.nav_bar_apps,
        iconActive = R.drawable.ic_navbar_apps,
        iconInactive = null,
        screenRoute = "apps"
    )

    data object Saved : NavBarItem(
        title = R.string.nav_bar_saved,
        iconActive = R.drawable.ic_save_active,
        iconInactive = R.drawable.ic_save_inactive,
        screenRoute = "saved"
    )

//    data object History : NavBarItem(
//        title = R.string.nav_bar_history,
//        iconActive = R.drawable.ic_navbar_history,
//        iconInactive = null,
//        screenRoute = "history"
//    )
}

val navBarItems = listOf(NavBarItem.Suggestions, NavBarItem.Apps, NavBarItem.Saved /*, NavBarItem.History*/)

internal sealed class ScreensDestination(var screenRoute: String) {

    fun createStringRoute(rootRoute: String) = "${rootRoute}/$screenRoute"

    data object Root : ScreensDestination("root")
    data object FlagChange : ScreensDestination("{flagChange}") {
        fun createRoute(flagChange: String): String {
            return "packages/$flagChange"
        }
    }

    data object Settings : ScreensDestination("settings")
    data object SettingsAbout : ScreensDestination("settingsAbout")
    data object SettingsResetFlags : ScreensDestination("settingsResetFlags")
    data object SettingsResetSaved : ScreensDestination("settingsResetSaved")
    data object SettingsChangeNavigation : ScreensDestination("changeNavigation")

    data object Packages : ScreensDestination("packages")
    data object Welcome : ScreensDestination("welcome")
    data object RootRequest : ScreensDestination("rootRequest")
    data object NotificationRequest : ScreensDestination("notificationRequest")
}

@Stable
@Composable
internal fun BottomBarNavigation( // Navigation realization for BottomBar
    isFirstStart: Boolean,
    modifier: Modifier = Modifier,
    parentNavController: NavController,
    navController: NavHostController
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val startScreen = remember {
        mutableStateOf(
            preferencesManager.getData(
                PreferenceConstants.START_SCREEN_KEY,
                NavBarItem.Suggestions.screenRoute
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = startScreen.value,
        modifier = modifier
    ) {
        composable(route = NavBarItem.Suggestions.screenRoute) {
            SuggestionsScreen(
                isFirstStart = isFirstStart,
                onSettingsClick = {
                    parentNavController.navigate(ScreensDestination.Settings.screenRoute)
                },
                onPackagesClick = {
                    parentNavController.navigate(ScreensDestination.Packages.screenRoute)
                }
            )
        }
        composable(route = NavBarItem.Apps.screenRoute) {
            AppsScreen(
                onSettingsClick = {
                    parentNavController.navigate(ScreensDestination.Settings.screenRoute)
                },
                onPackagesClick = {
                    parentNavController.navigate(ScreensDestination.Packages.screenRoute)
                },
                onPackageItemClick = {
                    parentNavController.navigate(
                        ScreensDestination.FlagChange.createRoute(Uri.encode(it))
                    )
                }
            )
        }
        composable(route = NavBarItem.Saved.screenRoute) {
            SavedScreen(
                onSettingsClick = {
                    parentNavController.navigate(ScreensDestination.Settings.screenRoute)
                },
                onPackagesClick = {
                    parentNavController.navigate(ScreensDestination.Packages.screenRoute)
                },
                onSavedPackageClick = {
                    parentNavController.navigate(
                        ScreensDestination.FlagChange.createRoute(Uri.encode(it))
                    )
                },
                onSavedFlagClick = { packageName, flagName, type  ->
                    parentNavController.navigate(
                        ScreensDestination.FlagChange.createRoute(Uri.encode(packageName)) // TODO: Implement search flag in list after navigation
                    )
                }
            )
        }
//        composable(route = NavBarItem.History.screenRoute) {
//            HistoryScreen(
//                onSettingsClick = {
//                    parentNavController.navigate(ScreensDestination.Settings.screenRoute)
//                },
//                onPackagesClick = {
//                    parentNavController.navigate(ScreensDestination.Packages.screenRoute)
//                }
//            )
//        }
    }
}
