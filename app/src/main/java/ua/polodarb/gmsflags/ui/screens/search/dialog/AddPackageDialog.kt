package ua.polodarb.gmsflags.ui.screens.search.dialog

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

@Composable
fun AddPackageDialog(
    showDialog: Boolean,
    pkgName: String,
    onPkgNameChange: (String) -> Unit,
    onAdd: () -> Unit,
    onDismiss: () -> Unit
) {

    var isError by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                isError = false
                onDismiss()
            },
            dismissButton = {},
            title = {
                Text(
                    text = "Add package",
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                OutlinedTextField(
                    value = pkgName,
                    onValueChange = {
                        isError = false
                        onPkgNameChange(it)
                    },
                    placeholder = {
                        if (isError) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "The field cannot be empty",
                                color = MaterialTheme.colorScheme.error
                            )
                        } else {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Enter package name",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    isError = isError,
                    modifier = Modifier.padding(top = 8.dp)
                )
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
                        if (pkgName.isEmpty()) {
                            isError = true
                        } else {
                            isError = false
                            onAdd()
                        }
                    }) {
                        Text(text = "Add")
                    }
                }
            }
        )
    }
}