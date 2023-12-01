package ua.polodarb.gmsflags.ui.components.dropDown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
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
import ua.polodarb.gmsflags.R

@Composable
fun FlagChangeDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onAddFlag: () -> Unit,
    onAddMultipleFlags: () -> Unit,
    onDeleteOverriddenFlags: () -> Unit,
    onOpenAppDetailsSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 44.dp)
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
                    text = { Text(text = stringResource(id = R.string.component_add_flag)) },
                    onClick = onAddFlag,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.add_a_multiple_flags)) },
                    onClick = onAddMultipleFlags,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_add_flag_list),
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
                DropdownMenuItem(
                    text = { Text(text = stringResource(R.string.open_app_details_settings)) },
                    onClick = onOpenAppDetailsSettings,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_open_app_settings),
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                DropdownMenuItem(
                    text = { Text(text = stringResource(id = R.string.component_reset_flags)) },
                    onClick = onDeleteOverriddenFlags,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_reset_flags),
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
            }
        }
    }
}
