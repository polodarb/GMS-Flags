package ua.polodarb.updates.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.polodarb.updates.UpdatesScreen

private const val updatesRoute = "updates"

fun NavGraphBuilder.updatesScreen(
    onSettingsClick: () -> Unit
) {
    composable(updatesRoute) {
        UpdatesScreen(onSettingsClick)
    }
}