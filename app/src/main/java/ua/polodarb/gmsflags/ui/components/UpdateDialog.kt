package ua.polodarb.gmsflags.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

@Composable
fun UpdateDialog(
    showDialog: Boolean,
    appVersion: String,
    onDismiss: () -> Unit,
    onUpdateClick: () -> Unit,
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Row {
                    OutlinedButton(onClick = onDismiss) {
                        Text(text = stringResource(R.string.update_dialog_close))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = onUpdateClick) {
                        Text(text = stringResource(R.string.update_dialog_confirm))
                    }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_update_app),
                    contentDescription = null
                )
            },
            text = {
                Text(text = stringResource(R.string.update_dialog_info))
            },
            title = {
                Text(text = stringResource(R.string.update_dialog_title) + appVersion + "!", fontWeight = FontWeight.Medium)
            },
        )
    }
}