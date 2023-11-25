package ua.polodarb.gmsflags.utils

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.TabPosition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.util.SortedMap

object Extensions {

    @Composable
    fun Modifier.simpleVerticalScrollbar(
        state: LazyListState,
        width: Dp = 8.dp
    ): Modifier {
        val targetAlpha = if (state.isScrollInProgress) 1f else 0f
        val duration = if (state.isScrollInProgress) 150 else 500

        val alpha by animateFloatAsState(
            targetValue = targetAlpha,
            animationSpec = tween(durationMillis = duration)
        )

        return drawWithContent {
            drawContent()

            val firstVisibleElementIndex = state.layoutInfo.visibleItemsInfo.firstOrNull()?.index
            val needDrawScrollbar = state.isScrollInProgress || alpha > 0.0f

            // Draw scrollbar if scrolling or if the animation is still running and lazy column has content
            if (needDrawScrollbar && firstVisibleElementIndex != null) {
                val elementHeight = this.size.height / state.layoutInfo.totalItemsCount
                val scrollbarOffsetY = firstVisibleElementIndex * elementHeight
                val scrollbarHeight = state.layoutInfo.visibleItemsInfo.size * elementHeight

                drawRect(
                    color = Color.Red,
                    topLeft = Offset(this.size.width - width.toPx(), scrollbarOffsetY),
                    size = Size(width.toPx(), scrollbarHeight),
                    alpha = alpha
                )
            }
        }
    }

    fun String.toFormattedInt(): Int {
        val digits = this.filter { it.isDigit() }
        return digits.toIntOrNull() ?: 0
    }

    fun Map<String, String>.toSortMap(): SortedMap<String, String> {
        return this.toSortedMap(
            compareByDescending<String> {
                it.toLongOrNull()
            }.thenBy { it }
        )
    }

    fun Map<String, String>.filterByEnabled(): Map<String, String> {
        val filteredMap = mutableMapOf<String, String>()
        for ((key, value) in this) {
            if (value == "1") {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    fun Map<String, String>.filterByDisabled(): Map<String, String> {
        val filteredMap = mutableMapOf<String, String>()
        for ((key, value) in this) {
            if (value == "0") {
                filteredMap[key] = value
            }
        }
        return filteredMap
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
