package ua.polodarb.gmsflags.ui.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GFlagFilterChipRow(
    list: List<String>,
    selectedChips: Int,
    chipOnClick: (index: Int) -> Unit

) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 6.dp)
            .height(36.dp)
    ) {
        list.forEachIndexed { index, title ->
            GFlagFilterChip(
                selected = selectedChips == index,
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