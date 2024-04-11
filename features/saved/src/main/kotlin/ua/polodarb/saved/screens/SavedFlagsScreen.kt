package ua.polodarb.saved.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.common.FlagsTypes
import ua.polodarb.repository.databases.local.model.SavedFlags
import ua.polodarb.saved.R
import ua.polodarb.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.ui.components.inserts.NotFoundContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedFlagsScreen(
    savedFlagsList: List<SavedFlags>,
    onCheckedChange: (value: Boolean, itemFlag: String, itemPackage: String) -> Unit,
    onFlagClick: (packageName: String, flagName: String, type: FlagsTypes) -> Unit
) {

    val clipboardManager = LocalClipboardManager.current
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (savedFlagsList.isEmpty()) {
            NotFoundContent(NoFlagsOrPackages.FLAGS)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val grouped = savedFlagsList.toList().groupBy { it.pkgName }
                grouped.entries.forEachIndexed { index, entry ->
                    stickyHeader {
                        Text(
                            text = entry.key,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                    itemsIndexed(entry.value) { _, item ->

                        val targetFlag = SavedFlags(item.pkgName, item.flagName, item.type)
                        val isEqual = savedFlagsList.any { flag ->
                            flag.pkgName == targetFlag.pkgName &&
                                    flag.flagName == targetFlag.flagName &&
                                    flag.type == targetFlag.type
                        }

                        SavedFlagsLazyItem(
                            flagName = item.flagName,
                            checked = isEqual,
                            onCheckedChange = {
                                onCheckedChange(it, item.flagName, item.pkgName)
                            },
                            modifier = Modifier.combinedClickable(
                                onClick = {
                                    onFlagClick(item.pkgName, item.flagName, item.type)
                                },
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    clipboardManager.setText(AnnotatedString(item.flagName))
                                }
                            )
                        )

                    }
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        if (grouped.size - 1 != index) HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SavedFlagsLazyItem(
    modifier: Modifier = Modifier,
    flagName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(checked = checked, onCheckedChange = onCheckedChange) {
                if (checked) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_active),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_save_inactive),
                        contentDescription = null
                    )
                }
            }
            Column(Modifier.weight(0.9f)) {
                Text(text = flagName, fontSize = 15.sp)
            }
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.ic_next),
                tint = MaterialTheme.colorScheme.outline,
                contentDescription = null
            )
        }
    }
}
