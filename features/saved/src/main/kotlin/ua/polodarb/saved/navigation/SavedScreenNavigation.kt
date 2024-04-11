package ua.polodarb.saved.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.polodarb.common.FlagsTypes
import ua.polodarb.saved.SavedScreen

fun NavGraphBuilder.savedScreen(
    route: String,
    onSettingsClick: () -> Unit,
    onSavedPackageClick: (packageName: String) -> Unit,
    onSavedFlagClick: (packageName: String, flagName: String, type: FlagsTypes) -> Unit,
) {
    composable(route) {
        SavedScreen(
            onSettingsClick = onSettingsClick,
            onSavedPackageClick = onSavedPackageClick,
            onSavedFlagClick = onSavedFlagClick
        )
    }
}