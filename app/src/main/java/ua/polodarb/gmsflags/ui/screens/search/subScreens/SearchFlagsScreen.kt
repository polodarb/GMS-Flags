package ua.polodarb.gmsflags.ui.screens.search.subScreens

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.ui.components.chips.filter.GFlagFilterChipRow
import ua.polodarb.gmsflags.ui.components.chips.types.GFlagTypesChipRow
import ua.polodarb.gmsflags.ui.screens.flagChange.FilterMethod

@Composable
fun SearchFlagsScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(30) {
            Text(
                text = "$it",
                modifier = Modifier.fillMaxWidth().height(36.dp).padding(horizontal = 12.dp)
            )
        }
    }
}