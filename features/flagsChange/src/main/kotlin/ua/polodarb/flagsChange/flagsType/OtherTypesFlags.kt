package ua.polodarb.flagsChange.flagsType

import android.content.Context
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
import ua.polodarb.common.FlagsTypes
import ua.polodarb.common.Extensions.toSortMap
import ua.polodarb.flagsChange.FlagChangeScreenViewModel
import ua.polodarb.flagsChange.FlagChangeUiStates
import ua.polodarb.flagsChange.IntFloatStringValItem
import ua.polodarb.flagsChange.dialogs.FlagChangeDialog
import ua.polodarb.repository.databases.local.model.SavedFlags
import ua.polodarb.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.ui.components.inserts.LoadingProgressBar
import ua.polodarb.ui.components.inserts.NotFoundContent
import ua.polodarb.ui.components.scrollbars.ListScrollbar

@Composable
fun OtherTypesFlagsScreen(
    uiState: FlagChangeUiStates,
    viewModel: FlagChangeScreenViewModel,
    packageName: String?,
    flagName: String,
    flagValue: String,
    flagsType: FlagsTypes,
    editTextValue: String,
    showDialog: Boolean,
    savedFlagsList: List<SavedFlags>,
    onFlagClick: (flagName: String, flagValue: String, editTextValue: String, showDialog: Boolean) -> Unit,
    dialogOnQueryChange: (String) -> Unit,
    dialogOnConfirm: () -> Unit,
    dialogOnDismiss: () -> Unit,
    dialogOnDefault: () -> Unit,
    haptic: HapticFeedback,
    context: Context
) {
    val lazyListState = rememberLazyListState()

    when (uiState) {
        is ua.polodarb.repository.uiStates.UiStates.Success -> {

            val textFlagType = when (flagsType) {
                FlagsTypes.BOOLEAN -> "Boolean"
                FlagsTypes.INTEGER -> "Integer"
                FlagsTypes.FLOAT -> "Float"
                FlagsTypes.STRING -> "String"
                FlagsTypes.EXTENSION -> "Extensions"
                FlagsTypes.UNKNOWN -> "Unknown"
            }

            fun setViewModelMethods() = when (flagsType) {

                FlagsTypes.BOOLEAN -> {}

                FlagsTypes.INTEGER -> {
                    viewModel.updateIntFlagValue(
                        flagName,
                        editTextValue
                    )
                    viewModel.overrideFlag(
                        packageName = packageName.toString(),
                        name = flagName,
                        intVal = editTextValue
                    )
                }

                FlagsTypes.FLOAT -> {
                    viewModel.updateFloatFlagValue(
                        flagName,
                        editTextValue
                    )
                    viewModel.overrideFlag(
                        packageName = packageName.toString(),
                        name = flagName,
                        floatVal = editTextValue
                    )
                }

                FlagsTypes.STRING -> {
                    viewModel.updateStringFlagValue(
                        flagName,
                        editTextValue
                    )
                    viewModel.overrideFlag(
                        packageName = packageName.toString(),
                        name = flagName,
                        stringVal = editTextValue
                    )
                }

                FlagsTypes.EXTENSION -> {
//                    viewModel.updateExtensionFlagValue(
//                        flagName,
//                        editTextValue
//                    )
//                    viewModel.overrideFlag(
//                        packageName = packageName.toString(),
//                        name = flagName,
//                        extensionVal = editTextValue
//                    )
                }

                FlagsTypes.UNKNOWN -> Unit
            }

            val listInt = uiState.data.toList().sortedBy { it.first }.toMap().toSortMap()

            if (listInt.isEmpty()) NotFoundContent()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                if (listInt.isNotEmpty()) {
                    ListScrollbar(
                        listState = lazyListState,
                        enabled = listInt.size >= 15
                    ) {
                        LazyColumn(
                            state = lazyListState
                        ) {
                            itemsIndexed(listInt.toList()) { index, item ->

                                val targetFlag = SavedFlags(
                                    packageName.toString(),
                                    item.first,
                                    flagsType
                                )
                                val isEqual =
                                    savedFlagsList.any { (packageName, flag, FlagsTypes) ->
                                        packageName == targetFlag.pkgName &&
                                                flag == targetFlag.flagName &&
                                                FlagsTypes == targetFlag.type
                                    }

                                IntFloatStringValItem(
                                    flagName = listInt.keys.toList()[index],
                                    flagValue = listInt.values.toList()[index],
                                    lastItem = index == listInt.size - 1,
                                    saveChecked = isEqual,
                                    saveOnCheckedChange = {
                                        if (it) {
                                            viewModel.saveFlag(
                                                item.first,
                                                packageName.toString(),
                                                flagsType
                                            )
                                        } else {
                                            viewModel.deleteSavedFlag(
                                                item.first,
                                                packageName.toString()
                                            )
                                        }
                                    },
                                    onClick = {
                                        onFlagClick(
                                            item.first,
                                            item.second,
                                            flagValue,
                                            showDialog
                                        )
                                    },
                                    onLongClick = { }
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.padding(12.dp))
                            }
                        }
                    }

                    FlagChangeDialog(
                        showDialog = showDialog,
                        flagName = flagName,
                        flagValue = flagValue,
                        onQueryChange = {
                            dialogOnQueryChange(it)
                        },
                        flagType = textFlagType,
                        onConfirm = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            setViewModelMethods()
                            dialogOnConfirm()
                        },
                        onDismiss = dialogOnDismiss,
                        onDefault = dialogOnDefault
                    )
                } else {
                    LoadingProgressBar()
                }
            }
        }

        is ua.polodarb.repository.uiStates.UiStates.Loading -> {
            LoadingProgressBar()
        }

        is ua.polodarb.repository.uiStates.UiStates.Error -> {
            ErrorLoadScreen()
        }
    }
}