package ua.polodarb.gmsflags.ui.screens.search.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

@Composable
fun SortAppsDialog(
    sortTypes: List<String>,
    showDialog: Boolean,
    onSelect: (sortType: String) -> Unit,
    onDismiss: () -> Unit
) {

    val (selectedOption, onOptionSelected) = rememberSaveable { mutableStateOf(sortTypes[0]) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            dismissButton = {},
            title = {
                Text(
                    text = "Sort options",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(Modifier.selectableGroup()) {
                    sortTypes.forEach { text ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (text == selectedOption),
                                    onClick = { onOptionSelected(text) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (text == selectedOption),
                                onClick = null
                            )
                            Text(
                                text = text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = onDismiss) {
                        Text(text = stringResource(id = R.string.close))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = {
                        onSelect(selectedOption)
                    }) {
                        Text(text = "Select")
                    }
                }
            }
        )
    }
}