package ua.polodarb.gmsflags.ui.screens.flagChangeScreen.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

@Composable
fun SuggestFlagsDialog(
    showDialog: Boolean,
    flagDesc: String,
    onFlagDescChange: (String) -> Unit,
    senderName: String,
    onSenderNameChanged: (String) -> Unit,
    onSend: () -> Unit,
    onDismiss: () -> Unit
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_navbar_suggestions_active),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            },
            dismissButton = {},
            title = {
                Text(
                    text = "Suggest flags",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column {
                    Text(text = "If you notice that this flag gives an interesting result, you can submit it to us for consideration. If your proposal is approved - it will appear in the \"Offers\" screen.")
                    OutlinedTextField(
                        value = flagDesc,
                        onValueChange = onFlagDescChange,
                        placeholder = {
                            Text(
                                text = "Write flag`s description",
                            )
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        shape = MaterialTheme.shapes.medium

                    )
                    OutlinedTextField(
                        value = senderName,
                        onValueChange = onSenderNameChanged,
                        placeholder = {
                            Text(
                                text = "Write your name or nickname",
                            )
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
                        Text(text = "Close")
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = onSend) {
                        Text(text = "Send suggest")
                    }
                }
            }
        )
    }
}