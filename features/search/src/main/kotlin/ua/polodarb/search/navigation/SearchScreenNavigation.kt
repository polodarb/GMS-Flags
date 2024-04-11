package ua.polodarb.search.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ua.polodarb.common.FlagsTypes
import ua.polodarb.search.SearchScreen

fun NavGraphBuilder.searchScreen(
    route: String,
    onSettingsClick: () -> Unit,
    onDialogPackageItemClick: (packageName: String) -> Unit,
    onAllPackagesItemClick: (packageName: String) -> Unit,
) {
    composable(route) {
        SearchScreen(
            onSettingsClick = onSettingsClick,
            onDialogPackageItemClick = onDialogPackageItemClick,
            onAllPackagesItemClick = onAllPackagesItemClick
        )
    }
}