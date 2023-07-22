package ua.polodarb.gmsflags.ui.screens.savedScreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R

@Composable
fun SavedFlagsScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(20) {
                LazyFlagsItem(packageName = "45369077", index = it)
            }
        }
    }
}

@Composable
fun LazyFlagsItem(
    packageName: String,
    modifier: Modifier = Modifier,
    index: Int
) {
    var checkedState by rememberSaveable {
        mutableStateOf(false)
    }

    LazyFlagsItem(
        packageName = packageName,
        modifier = modifier,
        checked = checkedState,
        onCheckedChange = { checked ->
            checkedState = checked
        }
    )
}

@Composable
fun LazyFlagsItem(
    packageName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(checked = checked, onCheckedChange = onCheckedChange) {
                if (checked) {
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
                Text(text = packageName, style = MaterialTheme.typography.bodyLarge)
            }
            Switch(
                checked = false, onCheckedChange = {

                }, modifier = Modifier
                    .padding(horizontal = 16.dp)
            )
        }
    }
    Divider(Modifier.padding(horizontal = 16.dp))
}