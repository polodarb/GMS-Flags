package ua.polodarb.suggestions.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.polodarb.suggestions.SuggestionsScreen

fun NavGraphBuilder.suggestionsScreen(
    route: String,
    isFirstStart: Boolean,
    onSettingsClick: () -> Unit
) {
    composable(route) {
        SuggestionsScreen(
            onSettingsClick = onSettingsClick,
            isFirstStart = isFirstStart
        )
    }
}