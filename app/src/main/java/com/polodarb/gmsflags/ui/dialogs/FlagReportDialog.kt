package com.polodarb.gmsflags.ui.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun FlagReportDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = "Flag's report") },
            text = {
                Column {
                    Text(text = "If this flag causes problems or bugs, please describe it below.")
                    OutlinedTextField(
                        value = "",
                        onValueChange = {  },
                        modifier = Modifier.padding(top = 16.dp),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            },
            confirmButton = {
                Button(onClick = { /* Обработка нажатия кнопки "Send Report" */ }) {
                    Text(text = "Send Report")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(text = "Exit")
                }
            }
        )
    }
}
