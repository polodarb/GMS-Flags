package ua.polodarb.gmsflags.ui.screens.flagChange.flagsType

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.LazyColumnScrollbar
import ua.polodarb.gmsflags.utils.Extensions.toInt
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.flagChange.BoolValItem
import ua.polodarb.gmsflags.ui.screens.flagChange.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChange.FlagChangeUiStates
import ua.polodarb.gmsflags.ui.screens.flagChange.SelectFlagsType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BooleanFlagsScreen(
    listBool: Map<String, String>,
    uiState: FlagChangeUiStates,
    savedFlagsList: List<SavedFlags>,
    viewModel: FlagChangeScreenViewModel,
    packageName: String?,
    haptic: HapticFeedback,
    isSelectedList: List<String>,
    selectedItemLongClick: (isSelected: Boolean, flagName: String) -> Unit,
    selectedItemShortClick: (isSelected: Boolean, flagName: String) -> Unit
) {

    val lazyListState = rememberLazyListState()

    when (uiState) {
        is UiStates.Success -> {

            if (listBool.isEmpty()) NoFlagsOrPackages()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                if (listBool.isNotEmpty()) {
                    LazyColumnScrollbar(
                        listState = lazyListState,
                        thickness = 8.dp,
                        padding = 0.dp,
                        thumbColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        thumbSelectedColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        thumbMinHeight = 0.075f,
                        enabled = listBool.size >= 15
                    ) {
                        LazyColumn(
                            state = lazyListState
                        ) {
                            itemsIndexed(listBool.keys.toList()) { index, flagName ->

                                val isSelected = isSelectedList.contains(flagName)

                                val checked = listBool.values.toList()[index] == "1"
                                val targetFlag = SavedFlags(
                                    packageName.toString(),
                                    flagName,
                                    SelectFlagsType.BOOLEAN.name
                                )
                                val isEqual = savedFlagsList.any { flag ->
                                    flag.pkgName == targetFlag.pkgName &&
                                            flag.flagName == targetFlag.flagName &&
                                            flag.type == targetFlag.type
                                }

                                BoolValItem(
                                    flagName = flagName,
                                    checked = checked,
                                    isSelected = isSelected,
                                    onCheckedChange = { newValue ->
                                        viewModel.updateBoolFlagValues(
                                            listOf(flagName),
                                            newValue.toInt().toString()
                                        )
                                        viewModel.overrideFlag(
                                            packageName = packageName.toString(),
                                            name = flagName,
                                            boolVal = newValue.toInt().toString()
                                        )
                                        viewModel.initOverriddenBoolFlags(packageName.toString())
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    },
                                    saveChecked = isEqual,
                                    saveOnCheckedChange = {
                                        if (it) {
                                            viewModel.saveFlag(
                                                flagName,
                                                packageName.toString(),
                                                SelectFlagsType.BOOLEAN.name
                                            )
                                        } else {
                                            viewModel.deleteSavedFlag(
                                                flagName,
                                                packageName.toString()
                                            )
                                        }
                                    },
                                    lastItem = index == listBool.size - 1,
                                    modifier = Modifier.combinedClickable(
                                        onClick = { selectedItemShortClick(isSelected, flagName) },
                                        onLongClick = {
                                            selectedItemLongClick(
                                                isSelected,
                                                flagName
                                            )
                                        }
                                    )
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.padding(56.dp))
                            }
                        }
                    }
                } else {
                    LoadingProgressBar()
                }
            }
        }

        is UiStates.Loading -> {
            LoadingProgressBar()
        }

        is UiStates.Error -> {
            NoFlagsOrPackages()
        }
    }
}
