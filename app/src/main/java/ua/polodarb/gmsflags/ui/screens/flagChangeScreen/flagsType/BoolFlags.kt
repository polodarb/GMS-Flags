package ua.polodarb.gmsflags.ui.screens.flagChangeScreen.flagsType

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.core.Extensions.toInt
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.BoolValItem
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.SelectFlagsType

@Composable
fun BooleanFlagsScreen(
    lazyListState: LazyListState,
    listBool: Map<String, String>,
    uiState: FlagChangeUiStates,
    savedFlagsList: List<SavedFlags>,
    viewModel: FlagChangeScreenViewModel,
    packageName: String?,
    haptic: HapticFeedback
) {
    when (uiState) {
        is FlagChangeUiStates.Success -> {

            if (listBool.isEmpty()) NoFlagsOrPackages()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                if (listBool.isNotEmpty()) {
                    LazyColumn(
                        state = lazyListState
                    ) {
                        itemsIndexed(listBool.keys.toList()) { index, flagName ->

                            val checked = listBool.values.toList()[index] == "1"
                            val targetFlag = SavedFlags(
                                packageName.toString(),
                                flagName,
                                SelectFlagsType.BOOLEAN.name
                            )
                            val isEqual =
                                savedFlagsList.any { (packageName, flag, selectFlagsType, _) ->
                                    packageName == targetFlag.pkgName &&
                                            flag == targetFlag.flagName &&
                                            selectFlagsType == targetFlag.type
                                }

                            BoolValItem(
                                flagName = flagName,
                                checked = checked,
                                onCheckedChange = { newValue ->
                                    viewModel.updateBoolFlagValue(
                                        flagName,
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
                                        viewModel.deleteSavedFlag(flagName, packageName.toString())
                                    }
                                },
                                lastItem = index == listBool.size - 1,
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(12.dp))
                        }
                    }
                } else {
                    LoadingProgressBar()
                }
            }
        }

        is FlagChangeUiStates.Loading -> {
            LoadingProgressBar()
        }

        is FlagChangeUiStates.Error -> {
            NoFlagsOrPackages()
        }
    }
}