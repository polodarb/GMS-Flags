package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.dialogs.FlagReportDialog

@Composable
fun BoolValItem(
    flagName: String,
    checked: Boolean,
    lastItem: Boolean = false,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(checked = false, onCheckedChange = onCheckedChange) { // todo
                if (false) { //todo
                    Icon(
                        painterResource(id = R.drawable.ic_save_active),
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_save_inactive),
                        contentDescription = "Localized description"
                    )
                }
            }
            Column(Modifier.weight(0.9f), verticalArrangement = Arrangement.Center) {
                Text(text = flagName, fontSize = 15.sp)
            }
            Switch(
                checked = checked, onCheckedChange = onCheckedChange, modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
    if (!lastItem) HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IntFloatStringValItem(
    flagName: String,
    flagValue: String,
    lastItem: Boolean = false,
    savedButtonChecked: Boolean,
    savedButtonOnChecked: (Boolean) -> Unit,
    haptic: HapticFeedback,
    context: Context,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.combinedClickable (
            onClick = {
                Toast.makeText(context, "onClick", Toast.LENGTH_SHORT).show()
            },
            onLongClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                Toast.makeText(context, "onLongClick", Toast.LENGTH_SHORT).show()
            },
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
//            Box( // indicator
//                modifier = Modifier
//                    .padding(start = 8.dp)
//                    .size(width = 4.dp, height = 32.dp)
//                    .clip(CircleShape)
//                    .background(MaterialTheme.colorScheme.error)
//            )
            IconToggleButton(
                checked = savedButtonChecked,
                onCheckedChange = savedButtonOnChecked
            ) { // todo
                if (savedButtonChecked) { //todo
                    Icon(
                        painterResource(id = R.drawable.ic_save_active),
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_save_inactive),
                        contentDescription = "Localized description"
                    )
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
                Text(text = flagName, fontSize = 15.sp)
            }
            Text(
                text = flagValue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(0.35f),
                textAlign = TextAlign.End
            )
        }
    }
    if (!lastItem) HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}