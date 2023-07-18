import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.polodarb.gmsflags.R
import com.polodarb.gmsflags.ui.screens.appsScreen.AppsScreen
import com.polodarb.gmsflags.ui.screens.historyScreen.HistoryScreen
import com.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreen
import com.polodarb.gmsflags.ui.screens.savedScreen.SavedScreen
import com.polodarb.gmsflags.ui.screens.suggestionsScreen.SuggestionsScreen

internal sealed class NavBarItem(var title: String, var icon: Int, var screenRoute: String) {

    object Suggestions : NavBarItem("Suggestions", R.drawable.ic_navbar_suggestions_active, "packages")
    object Apps : NavBarItem("Apps", R.drawable.ic_navbar_apps, "apps")
    object Saved : NavBarItem("Saved", R.drawable.ic_save_inactive, "saved")
    object History : NavBarItem("History", R.drawable.ic_navbar_history, "history")

}

internal sealed class ScreensDestination(var screenRoute: String) {

    fun createStringRoute(rootRoute: String) = "${rootRoute}/$screenRoute"

    object Root : ScreensDestination("root")
    object Suggestions : ScreensDestination("suggest")
    object FlagChange : ScreensDestination("{flagChange}") {
        fun createRoute(flagChange: String): String {
            return "packages/$flagChange"
        }
    }
    object Settings : ScreensDestination("settings")
}

@Composable
internal fun BottomBarNavigation( // Navigation realization for BottomBar
    parentNavController: NavController,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavBarItem.Suggestions.screenRoute,
        modifier = modifier
    ) {
        composable(route = NavBarItem.Suggestions.screenRoute) {
            SuggestionsScreen(
                onSettingsClick = {
                    parentNavController.navigate(ScreensDestination.Settings.screenRoute)
                }
            )
        }
        composable(route = NavBarItem.Apps.screenRoute) {
            AppsScreen()
        }
        composable(route = NavBarItem.Saved.screenRoute) {
            SavedScreen()
        }
        composable(route = NavBarItem.History.screenRoute) {
            HistoryScreen()
        }
    }
}