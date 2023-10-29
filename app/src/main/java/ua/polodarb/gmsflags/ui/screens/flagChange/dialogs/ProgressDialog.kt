package ua.polodarb.gmsflags.ui.screens.flagChange.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProgressDialog(
    showDialog: Boolean
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { /*TODO*/ },
            confirmButton = {},
            dismissButton = {},
            title = { Text(text = "Please wait", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    trackColor = MaterialTheme.colorScheme.secondary,
                )
            }
        )
    }
}
