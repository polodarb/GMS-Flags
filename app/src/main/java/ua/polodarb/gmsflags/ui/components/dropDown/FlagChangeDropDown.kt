package ua.polodarb.gmsflags.ui.components.dropDown

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

@Composable
fun FlagChangeDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onAddFlag: () -> Unit,
    onDeleteOverriddenFlags: () -> Unit,
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
                    text = { Text("Add flag") },
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
                    text = { Text("Reset all overridden flags") },
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