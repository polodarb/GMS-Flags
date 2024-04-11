package ua.polodarb.gmsflags.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ua.polodarb.gmsflags.R

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

    data object Search : NavBarItem(
        title = R.string.nav_bar_search,
        iconActive = R.drawable.ic_navbar_apps,
        iconInactive = null,
        screenRoute = "search"
    )

    data object Saved : NavBarItem(
        title = R.string.nav_bar_saved,
        iconActive = R.drawable.ic_save_active,
        iconInactive = R.drawable.ic_save_inactive,
        screenRoute = "saved"
    )

    data object Updates : NavBarItem(
        title = R.string.nav_bar_updates,
        iconActive = R.drawable.ic_updates_active,
        iconInactive = R.drawable.ic_updates_inactive,
        screenRoute = "updates"
    )
}

val navBarItems = listOf(
    NavBarItem.Suggestions,
    NavBarItem.Search,
    NavBarItem.Saved,
    NavBarItem.Updates
)

internal sealed class ScreensDestination(var screenRoute: String) {

    fun createStringRoute(rootRoute: String) = "${rootRoute}/$screenRoute"

    data object Root : ScreensDestination("root")
    data object LoadFile : ScreensDestination("loadFile")
    data object FlagChange : ScreensDestination("{flagChange}") {
        fun createRoute(flagChange: String): String {
            return "packages/$flagChange"
        }
    }

    data object AddFlagList : ScreensDestination("{addFlagList}") {
        fun createRoute(pkgName: String): String {
            return "flagChange/$pkgName"
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
