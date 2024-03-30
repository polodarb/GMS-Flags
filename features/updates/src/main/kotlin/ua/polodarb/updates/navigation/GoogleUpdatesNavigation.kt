package ua.polodarb.updates.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.polodarb.updates.GoogleUpdatesScreen

private const val updatesRoute = "updates"

fun NavGraphBuilder.updatesScreen(
) {
    composable(updatesRoute) {
        GoogleUpdatesScreen()
    }
}