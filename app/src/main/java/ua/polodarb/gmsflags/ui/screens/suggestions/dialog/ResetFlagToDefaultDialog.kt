package ua.polodarb.gmsflags.ui.screens.suggestions.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

@Composable
fun ResetFlagToDefaultDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_reset_flags),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            },
            title = {
                Text(text = stringResource(R.string.suggestions_dialog_title),
                    textAlign = TextAlign.Center)
            },
            text = {
                Text(
                    text = stringResource(R.string.suggestions_dialog_text),
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss
                    ) {
                        Text(stringResource(id = R.string.close))
                    }
                    Button(
                        onClick = onConfirmClick
                    ) {
                        Text(stringResource(id = R.string.update_dialog_confirm))
                    }
                }
            },
        )
    }
}
