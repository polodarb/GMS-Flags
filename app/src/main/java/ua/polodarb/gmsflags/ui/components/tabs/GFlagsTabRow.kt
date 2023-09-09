package ua.polodarb.gmsflags.ui.components.tabs

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GFlagsTabRow(
    list: List<String>,
    tabState: Int,
    topBarState: TopAppBarState,
    onClick: (Int) -> Unit
) {

    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomTabIndicatorAnimation(tabPositions = tabPositions, selectedTabIndex = tabState)
    }

    TabRow(
        selectedTabIndex = tabState,
        indicator = indicator,
        containerColor = lerp(
            MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp),
            MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
            FastOutLinearInEasing.transform(topBarState.collapsedFraction)
        )
    ) {
        list.forEachIndexed { index, title ->
            GFlagsTab(
                selected = tabState == index,
                tabState = tabState,
                index = index,
                tabTitle = title,
                onClick = {
                    onClick(index)
                }
            )
        }
    }
}