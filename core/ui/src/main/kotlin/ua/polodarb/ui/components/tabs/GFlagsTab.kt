package ua.polodarb.ui.components.tabs

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun GFlagsTab(
    selected: Boolean,
    tabState: Int,
    index: Int,
    tabTitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Tab(
        selected = selected,
        onClick = onClick,
        text = {
            Text(
                text = tabTitle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (tabState == index) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        },

        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 12.dp)
            .height(40.dp)
            .clip(MaterialTheme.shapes.extraLarge),
        enabled = enabled
    )
}
