package ua.polodarb.gmsflags.ui.screens.flagChange.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FlagChangeDialog(
    showDialog: Boolean,
    flagName: String,
    flagValue: String,
    onQueryChange: (String) -> Unit,
    flagType: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDefault: () -> Unit
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Changing flag's value") },
            text = {
                Column {
                    Text(
                        text = "Name: $flagName",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Type: $flagType",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal
                    )
                    OutlinedTextField(
                        value = flagValue,
                        onValueChange = {
                            onQueryChange(it)
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
                    TextButton(onClick = onDefault) {
                        Text(text = "Default")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Row(
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = "Close")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = onConfirm) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        )
    }
}