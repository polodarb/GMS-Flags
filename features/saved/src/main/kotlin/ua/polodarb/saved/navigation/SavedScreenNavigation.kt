package ua.polodarb.saved.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.polodarb.common.fagsTypes.FlagsTypes
import ua.polodarb.saved.SavedScreen

private const val savedScreenRoute = "saved"

fun NavGraphBuilder.savedScreen(
    onSettingsClick: () -> Unit,
    onSavedPackageClick: (packageName: String) -> Unit,
    onSavedFlagClick: (packageName: String, flagName: String, type: FlagsTypes) -> Unit,
) {
    composable(savedScreenRoute) {
        SavedScreen(
            onSettingsClick = onSettingsClick,
            onSavedPackageClick = onSavedPackageClick,
            onSavedFlagClick = onSavedFlagClick
        )
    }
}