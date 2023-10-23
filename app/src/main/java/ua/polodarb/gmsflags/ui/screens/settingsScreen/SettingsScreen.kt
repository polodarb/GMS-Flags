package ua.polodarb.gmsflags.ui.screens.settingsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.IRootDatabase
import ua.polodarb.gmsflags.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onResetFlagsClick: () -> Unit,
    onResetSavedClick: () -> Unit,
    onChangeNavigationClick: () -> Unit,
    onAboutClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            "Settings",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.displaySmall
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    ) { it ->
        Column(modifier = Modifier.padding(it)) {
            Column {
                SettingsItem(R.drawable.ic_reset_flags, "Reset flags", "Reset all overridden flags", onResetFlagsClick)
                SettingsItem(R.drawable.ic_reset_saved, "Reset saved", "Reset all saved packages or flags", onResetSavedClick)
                SettingsItem(R.drawable.ic_home, "Change the start screen", "Select from which screen the app will start", onChangeNavigationClick)
                SettingsItem(R.drawable.ic_info, "About & support", "Useful information and resources", onAboutClick)
            }
        }
    }
}

@Composable
fun SettingsItem(
    icon: Int,
    headline: String,
    description: String,
    onItemClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = icon), contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp).size(26.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = headline, fontSize = 20.sp)
            Text(text = description, color = MaterialTheme.colorScheme.outline, fontSize = 15.sp)
        }
    }
}