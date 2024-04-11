package ua.polodarb.ui.components.scrollbars

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.LazyColumnScrollbar

@Composable
fun ListScrollbar(
    listState: LazyListState,
    enabled: Boolean = true,
    content: @Composable () -> Unit
) {
    LazyColumnScrollbar(
        listState = listState,
        thickness = 8.dp,
        padding = 0.dp,
        thumbColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
        thumbSelectedColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
        enabled = enabled,
        content = content
    )
}