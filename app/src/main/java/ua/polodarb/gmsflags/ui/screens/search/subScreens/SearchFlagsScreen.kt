package ua.polodarb.gmsflags.ui.screens.search.subScreens

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.data.repo.mappers.FlagDetails
import ua.polodarb.gmsflags.ui.components.chips.filter.GFlagFilterChipRow
import ua.polodarb.gmsflags.ui.components.chips.types.GFlagTypesChipRow
import ua.polodarb.gmsflags.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.gmsflags.ui.components.inserts.NotFoundContent
import ua.polodarb.gmsflags.ui.screens.flagChange.FilterMethod
import ua.polodarb.gmsflags.ui.screens.saved.SavedFlagsLazyItem

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchFlagsScreen(
    flags: List<FlagDetails>,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (flags.isEmpty()) {
            NotFoundContent(NoFlagsOrPackages.FLAGS)
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val grouped = flags.toList().groupBy { it.pkgName }
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
                        val isEqual = flags.any { flag ->
                            flag.pkgName == targetFlag.pkgName &&
                                    flag.flagName == targetFlag.flagName &&
                                    flag.type == targetFlag.type
                        }

                        SavedFlagsLazyItem(
                            flagName = item.flagName,
                            checked = isEqual,
                            onCheckedChange = {
//                                onCheckedChange(it, item.flagName, item.pkgName)
                            },
                            modifier = Modifier.combinedClickable(
                                onClick = {
//                                    onFlagClick(item.pkgName, item.flagName, item.type)
                                },
                                onLongClick = {
//                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                                    clipboardManager.setText(AnnotatedString(item.flagName))
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