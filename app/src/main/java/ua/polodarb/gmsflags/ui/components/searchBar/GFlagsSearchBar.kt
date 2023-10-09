package ua.polodarb.gmsflags.ui.components.searchBar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GFlagsSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    iconVisibility: Boolean,
    iconOnClick: () -> Unit,
    placeHolderText: String,
    keyboardFocus: FocusRequester
) {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .height(64.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DockedSearchBar(
            query = query,
            onQueryChange = onQueryChange,
            onSearch = {},
            placeholder = {
                Text(text = placeHolderText)
            },
            trailingIcon = {
                AnimatedVisibility(
                    visible = iconVisibility,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(onClick = iconOnClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null
                        )
                    }
                }
            },
            active = false,
            onActiveChange = { },
                        modifier = Modifier
                .fillMaxWidth()
                .focusRequester(keyboardFocus)
        ) { }
    }
}