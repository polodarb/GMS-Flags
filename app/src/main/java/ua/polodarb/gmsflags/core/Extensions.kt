package ua.polodarb.gmsflags.core

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import ua.polodarb.gmsflags.ui.navigation.NavBarItem

object Extensions {

    fun Modifier.customTabIndicatorOffset(
        currentTabPosition: TabPosition
    ): Modifier = composed(
        inspectorInfo = debugInspectorInfo {
            name = "tabIndicatorOffset"
            value = currentTabPosition
        }
    ) {
        val indicatorWidth = 56.dp
        val currentTabWidth = currentTabPosition.width
        val indicatorOffset by animateDpAsState(
            targetValue = currentTabPosition.left + currentTabWidth / 2 - indicatorWidth / 2,
            animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
        )
        fillMaxWidth()
            .wrapContentSize(Alignment.BottomStart)
            .offset(x = indicatorOffset)
            .width(indicatorWidth)

    }
}

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

                destination.hierarchy.any { it.route == NavBarItem.History.screenRoute } -> {
                    selectedItem.value = NavBarItem.History
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
