package ua.polodarb.flagsChange.dialogs

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.polodarb.flagschange.R

@Composable
fun ShareFlagsDialog(
    showDialog: Boolean,
    fileName: String,
    fileNameChange: (String) -> Unit,
    onSend: () -> Unit,
    onTrailingIconClick: () -> Unit,
    onDismiss: () -> Unit
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_file_name),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            },
            dismissButton = {},
            title = {
                Text(
                    text = stringResource(R.string.share_flag_dialog_suggest_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = fileName,
                        onValueChange = fileNameChange,
                        isError = !isFileNameValid(fileName),
                        supportingText = {
                            if (!isFileNameValid(fileName)) {
                                Text("File name is not valid")
                            }
                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = fileName.isNotEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(
                                    onClick = onTrailingIconClick
                                ) {
                                    Icon(Icons.Rounded.Close, null)
                                }
                            }
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        shape = MaterialTheme.shapes.medium

                    )
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
                    Button(onClick = onSend, enabled = fileName.isNotEmpty()) {
                        Text(text = stringResource(R.string.flag_dialog_share_action_send))
                    }
                }
            }
        )
    }
}

private fun isFileNameValid(name: String): Boolean {
    // Regex that excludes \ / : * ? " < > |
    val regex = """^[^\\/:*?"<>|]+$""".toRegex()
    return regex.matches(name)
}