package ua.polodarb.ui.components.dropDown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.polodarb.ui.R

@Composable
fun FlagSelectDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onEnableSelected: () -> Unit,
    onDisableSelected: () -> Unit,
    onSelectAllItems: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.offset(x = (-12).dp)
    ) {
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            )
            {
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.drop_down_enable_selected)) },
                    onClick = onEnableSelected,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_enable_selected),
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.drop_down_disable_selected)) },
                    onClick = onDisableSelected,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_disable_selected),
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.drop_down_select_all)) },
                    onClick = onSelectAllItems,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_select_all),
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
            }
        }
    }
}
