package ua.polodarb.gmsflags.ui.screens.flagChangeScreen.flagsType

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.core.Extensions.toSortMap
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.IntFloatStringValItem
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.SelectFlagsType
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.dialogs.FlagChangeDialog

@Composable
fun OtherTypesFlagsScreen(
    uiState: FlagChangeUiStates,
    viewModel: FlagChangeScreenViewModel,
    packageName: String?,
    flagName: String,
    flagValue: String,
    flagsType: SelectFlagsType,
    editTextValue: String,
    showDialog: Boolean,
    savedFlagsList: List<SavedFlags>,
    onFlagClick: (flagName: String, flagValue: String, editTextValue: String, showDialog: Boolean) -> Unit,
    dialogOnQueryChange: (String) -> Unit,
    dialogOnConfirm: () -> Unit,
    dialogOnDismiss: () -> Unit,
    haptic: HapticFeedback,
    context: Context
) {
    when (uiState) {
        is UiStates.Success -> {

            val textFlagType = when (flagsType) {
                SelectFlagsType.BOOLEAN -> "Boolean"
                SelectFlagsType.INTEGER -> "Integer"
                SelectFlagsType.FLOAT -> "Float"
                SelectFlagsType.STRING -> "String"
            }

            fun setViewModelMethods() = when (flagsType) {

                SelectFlagsType.BOOLEAN -> {}

                SelectFlagsType.INTEGER -> {
                    viewModel.overrideFlag(
                        packageName = packageName.toString(),
                        name = flagName,
                        intVal = editTextValue
                    )
                    viewModel.updateIntFlagValue(
                        flagName,
                        editTextValue
                    )
                }

                SelectFlagsType.FLOAT -> {
                    viewModel.overrideFlag(
                        packageName = packageName.toString(),
                        name = flagName,
                        floatVal = editTextValue
                    )
                    viewModel.updateFloatFlagValue(
                        flagName,
                        editTextValue
                    )
                }

                SelectFlagsType.STRING -> {
                    viewModel.overrideFlag(
                        packageName = packageName.toString(),
                        name = flagName,
                        stringVal = editTextValue
                    )
                    viewModel.updateStringFlagValue(
                        flagName,
                        editTextValue
                    )
                }
            }

            val listInt =
                uiState.data.toList().sortedBy { it.first }.toMap().toSortMap()

            if (listInt.isEmpty()) NoFlagsOrPackages()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                if (listInt.isNotEmpty()) {
                    LazyColumn {
                        itemsIndexed(listInt.toList()) { index, item ->

                            val targetFlag = SavedFlags(
                                packageName.toString(),
                                item.first,
                                flagsType.name
                            )
                            val isEqual =
                                savedFlagsList.any { (packageName, flag, selectFlagsType, _) ->
                                    packageName == targetFlag.pkgName &&
                                            flag == targetFlag.flagName &&
                                            selectFlagsType == targetFlag.type
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
                                            flagsType.name
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
                            dialogOnConfirm()
                            setViewModelMethods()
                        },
                        onDismiss = dialogOnDismiss,
                        onDefault = {
                            Toast.makeText(
                                context,
                                "Not implemented",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                } else {
                    LoadingProgressBar()
                }
            }
        }

        is UiStates.Loading -> {
            LoadingProgressBar()
        }

        is UiStates.Error -> {
            ErrorLoadScreen()
        }
    }
}