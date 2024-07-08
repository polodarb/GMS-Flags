package ua.polodarb.updates.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppsFilterDialog(
    showDialog: Boolean,
    currentData: String,
    onDataChanges: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    if (showDialog) {
        var text by remember { mutableStateOf(currentData) }
        var isValid by remember { mutableStateOf(true) }

        fun validate(input: String): Boolean {
            val regex = Regex("^([A-Za-z]+(, )?)*[A-Za-z]*$")
            return regex.matches(input)
        }

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = {
                Text(text = "Filtered list of apps")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = text,
                        onValueChange = {
                            text = it
                            isValid = validate(it)
                        },
                        shape = MaterialTheme.shapes.medium,
                        label = { Text("Apps") },
                        isError = !isValid,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (isValid) {
                                    onDataChanges(text)
                                    onDismissRequest()
                                }
                            }
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    if (!isValid) {
                        Text(
                            text = "Input must be words separated by comma and space.",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton (onClick = onDismissRequest) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            if (isValid) {
                                onDataChanges(text)
                                onDismissRequest()
                            }
                        }
                    ) {
                        Text("Save")
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppsFilterDialog() {
    MaterialTheme {
        AppsFilterDialog(
            showDialog = true,
            currentData = "Google Task, Keep, Google News",
            onDataChanges = {},
            onDismissRequest = {}
        )
    }
}
