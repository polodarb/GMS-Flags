package ua.polodarb.flagsChange.dialogs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.polodarb.flagschange.R

@Composable
fun ProgressDialog(
    showDialog: Boolean
) {

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
            dismissButton = {},
            title = {
                Text(
                    text = stringResource(R.string.flag_change_dialog_progress_title),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    trackColor = MaterialTheme.colorScheme.secondary,
                    strokeCap = StrokeCap.Round
                )
            }
        )
    }
}
