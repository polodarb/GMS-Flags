package ua.polodarb.gmsflags.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.dp
import java.util.SortedMap

object Extensions {

    fun String.toFormattedInt(): Int {
        val digits = this.filter { it.isDigit() }
        return digits.toIntOrNull() ?: 0
    }

    fun Map<String, String>.toSortMap(): SortedMap<String, String> {
        return this.toSortedMap(
            compareByDescending<String> {
                it.toIntOrNull() ?: 0
            }.thenBy { it })
    }

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

    fun Boolean.toInt() = if (this) 1 else 0
}
