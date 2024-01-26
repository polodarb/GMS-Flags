package ua.polodarb.gmsflags.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy


@Stable
@Composable
fun NavController.currentScreenAsState(): State<NavBarItem> {

    val selectedItem = remember { mutableStateOf<NavBarItem>(NavBarItem.Suggestions) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == NavBarItem.Suggestions.screenRoute } -> {
                    selectedItem.value = NavBarItem.Suggestions
                }

                destination.hierarchy.any { it.route == NavBarItem.Apps.screenRoute } -> {
                    selectedItem.value = NavBarItem.Apps
                }

                destination.hierarchy.any { it.route == NavBarItem.Saved.screenRoute } -> {
                    selectedItem.value = NavBarItem.Saved
                }

                destination.hierarchy.any { it.route == NavBarItem.Updates.screenRoute } -> {
                    selectedItem.value = NavBarItem.Updates
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}