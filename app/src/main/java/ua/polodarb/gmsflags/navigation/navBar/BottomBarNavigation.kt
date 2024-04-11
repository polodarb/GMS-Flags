package ua.polodarb.gmsflags.navigation.navBar

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.koin.compose.koinInject
import ua.polodarb.gmsflags.navigation.NavBarItem
import ua.polodarb.gmsflags.navigation.ScreensDestination
import ua.polodarb.preferences.sharedPrefs.PreferenceConstants
import ua.polodarb.preferences.sharedPrefs.PreferencesManager
import ua.polodarb.saved.navigation.savedScreen
import ua.polodarb.search.navigation.searchScreen
import ua.polodarb.suggestions.navigation.suggestionsScreen
import ua.polodarb.updates.navigation.updatesScreen

@Stable
@Composable
internal fun BottomBarNavigation(
    isFirstStart: Boolean,
    modifier: Modifier = Modifier,
    parentNavController: NavController,
    navController: NavHostController
) {

    val sharedPrefs = koinInject<PreferencesManager>()
    val preferencesManager = remember { sharedPrefs }
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
        suggestionsScreen(
            route = NavBarItem.Suggestions.screenRoute,
            isFirstStart = isFirstStart,
            onSettingsClick = {
                parentNavController.navigate(ScreensDestination.Settings.screenRoute)
            }
        )
        searchScreen(
            route = NavBarItem.Search.screenRoute,
            onSettingsClick = {
                parentNavController.navigate(ScreensDestination.Settings.screenRoute)
            },
            onDialogPackageItemClick = {
                parentNavController.navigate(
                    ScreensDestination.FlagChange.createRoute(Uri.encode(it))
                )
            },
            onAllPackagesItemClick = {
                parentNavController.navigate(
                    ScreensDestination.FlagChange.createRoute(Uri.encode(it))
                )
            }
        )
        savedScreen(
            route = NavBarItem.Saved.screenRoute,
            onSettingsClick = {
                parentNavController.navigate(ScreensDestination.Settings.screenRoute)
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
        updatesScreen(
            onSettingsClick = {
                parentNavController.navigate(ScreensDestination.Settings.screenRoute)
            }
        )
    }
}
