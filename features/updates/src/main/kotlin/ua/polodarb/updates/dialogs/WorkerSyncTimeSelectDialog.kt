package ua.polodarb.updates.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.polodarb.updates.R
import java.util.concurrent.TimeUnit

data class SyncTime(val value: Long, val unit: TimeUnit)

@Composable
fun WorkerSyncTimeSelectDialog(
    showDialog: Boolean,
    initialSyncTime: SyncTime,
    onDataChanges: (SyncTime) -> Unit,
    onDismissRequest: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }

    if (showDialog) {
        var selectedOption by remember { mutableStateOf(initialSyncTime) }

        val options = listOf(
            SyncTime(15, TimeUnit.MINUTES),
            SyncTime(30, TimeUnit.MINUTES),
            SyncTime(1, TimeUnit.HOURS),
            SyncTime(3, TimeUnit.HOURS),
            SyncTime(6, TimeUnit.HOURS),
            SyncTime(12, TimeUnit.HOURS),
            SyncTime(24, TimeUnit.HOURS)
        )

        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = stringResource(R.string.dialog_select_time_title)) },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.dialog_select_time_subtitle),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    options.forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .clip(CircleShape)
                                .clickable {
                                    selectedOption = option
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedOption == option,
                                onClick = { selectedOption = option },
                            )
                            Text(
                                text = formatTimeOptionText(option),
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(R.string.dialog_select_time_cancel))
                    }

                    Button(
                        onClick = {
                            onDataChanges(selectedOption)
                            onDismissRequest()
                        }
                    ) {
                        Text(stringResource(R.string.dialog_select_time_save))
                    }
                }
            }
        )
    }
}

@Composable
private fun formatTimeOptionText(option: SyncTime,): String {
    val unitText = when (option.value) {
        15L, 30L -> stringResource(R.string.dialog_select_time_item_minutes)
        1L -> stringResource(R.string.dialog_select_time_item_hour)
        else -> stringResource(R.string.dialog_select_time_item_hours)
    }
    return "${option.value} $unitText"
}

@Preview(showBackground = true)
@Composable
fun PreviewWorkerSyncTimeSelectDialog() {
    MaterialTheme {
        WorkerSyncTimeSelectDialog(
            showDialog = true,
            initialSyncTime = SyncTime(15, TimeUnit.MINUTES),
            onDataChanges = {},
            onDismissRequest = {}
        )
    }
}
