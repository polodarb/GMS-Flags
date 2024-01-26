package ua.polodarb.gmsflags.ui.components.chips.filter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.PersistentList

@Composable
fun GFlagFilterChipRow(
    list: PersistentList<String>,
    selectedChips: Int,
    pagerCurrentState: Int,
    colorFraction: Float? = null,
    chipOnClick: (index: Int) -> Unit
) {
    Row(
        modifier = Modifier
            .background(
                if (colorFraction != null) {
                    lerp(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp),
                        MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                        colorFraction
                    )
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 6.dp)
            .height(36.dp)
    ) {
        list.forEachIndexed { index, title ->
            GFlagFilterChip(
                selected = selectedChips == index,
                pagerCurrentState = pagerCurrentState,
                chipOnClick = {
                    chipOnClick(index)
                },
                chipTitle = title,
                modifier = Modifier
                    .weight(if (index != 0) 1f else 0.8f)
                    .padding(horizontal = 8.dp)
            )
        }
    }
}
