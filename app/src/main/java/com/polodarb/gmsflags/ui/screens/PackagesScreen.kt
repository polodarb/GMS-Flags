package com.polodarb.gmsflags.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.polodarb.gmsflags.R
import com.polodarb.gmsflags.ui.theme.Typography
import com.topjohnwu.superuser.Shell

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "Packages",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast.makeText(context, "Suggestions", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_suggestions),
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        val result =
            Shell.cmd(
                "cd $DB_PATH",
                "sqlite3 phenotype.db" +
                        " \"SELECT DISTINCT packageName FROM Flags LIMIT 30\""
            ).exec().out

        LazyColumn(
            contentPadding = it
        ) {
            items(result.size) {
                LazyItem(text = result[it])
            }
        }
    }
}

const val DB_PATH = "data/data/com.google.android.gms/databases/"
@Composable
fun LazyItem(text: String) {

    var listIconSelect by remember {
        mutableStateOf(false)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(checked = listIconSelect, onCheckedChange = { listIconSelect = it }) {
                if (listIconSelect) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_on),
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_save_off),
                        contentDescription = "Localized description"
                    )
                }
            }
            Column(Modifier.weight(0.9f)) {
                Text(text = text, style = Typography.bodyMedium)
                Text(
                    text = "Flags: 34",
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Icon(
                modifier = Modifier
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null
            )
        }
    }
    Divider()
}