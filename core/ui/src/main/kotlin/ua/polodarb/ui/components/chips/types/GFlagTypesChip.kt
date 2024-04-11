package ua.polodarb.ui.components.chips.types

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GFlagTypesChip(
    selected: Boolean,
    chipOnClick: () -> Unit,
    chipTitle: String,
    modifier: Modifier
) {
    FilterChip(
        selected = selected,
        onClick = chipOnClick,
        colors = SelectableChipColors(
            containerColor = Color.Transparent,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            leadingIconColor = Color.Transparent,
            trailingIconColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledLabelColor = MaterialTheme.colorScheme.onSecondaryContainer.copy(
                alpha = 0.3f
            ),
            disabledLeadingIconColor = Color.Transparent,
            disabledTrailingIconColor = Color.Transparent,
            selectedContainerColor = MaterialTheme.colorScheme.secondary,
            disabledSelectedContainerColor = MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.2f
            ),
            selectedLabelColor = MaterialTheme.colorScheme.onSecondary,
            selectedLeadingIconColor = Color.Transparent,
            selectedTrailingIconColor = Color.Transparent
        ),
        label = {
            Text(
                text = chipTitle,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = null,
        modifier = modifier
    )
}