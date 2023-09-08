package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

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
                        onValueChange = { },
                        modifier = Modifier.padding(top = 16.dp),
                        shape = MaterialTheme.shapes.medium
                    )
                }
            },
            icon = {
                Icon(
                    painterResource(id = R.drawable.ic_report),
                    "",
                    modifier = Modifier.size(36.dp)
                )
            },
            confirmButton = {
                Button(onClick = { }) {
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
