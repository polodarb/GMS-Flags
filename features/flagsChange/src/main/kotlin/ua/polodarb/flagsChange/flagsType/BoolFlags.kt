package ua.polodarb.flagsChange.flagsType

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.polodarb.common.FlagsTypes
import ua.polodarb.common.Extensions.toInt
import ua.polodarb.domain.override.models.OverriddenFlagsContainer
import ua.polodarb.flagsChange.BoolValItem
import ua.polodarb.flagsChange.FlagChangeScreenViewModel
import ua.polodarb.flagsChange.FlagChangeUiStates
import ua.polodarb.repository.databases.local.model.SavedFlags
import ua.polodarb.ui.components.inserts.LoadingProgressBar
import ua.polodarb.ui.components.inserts.NotFoundContent
import ua.polodarb.ui.components.scrollbars.ListScrollbar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BooleanFlagsScreen(
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
    val coroutineScope = rememberCoroutineScope()

    when (val listBool = uiState) {
        is ua.polodarb.repository.uiStates.UiStates.Success -> {

            if (listBool.data.isEmpty()) NotFoundContent()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
            ) {
                if (listBool.data.isNotEmpty()) {
                    ListScrollbar(
                        listState = lazyListState,
                        enabled = listBool.data.size >= 15
                    ) {
                        LazyColumn(
                            state = lazyListState
                        ) {
                            itemsIndexed(listBool.data.keys.toList()) { index, flagName ->

                                val isSelected = isSelectedList.contains(flagName)

                                val checked = listBool.data.values.toList()[index] == "1"
                                val targetFlag = SavedFlags(
                                    packageName.toString(),
                                    flagName,
                                    FlagsTypes.BOOLEAN
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
                                        coroutineScope.launch {
                                            viewModel.updateBoolFlagValues(
                                                listOf(flagName),
                                                newValue.toInt().toString()
                                            )
                                            viewModel.overrideFlag(
                                                packageName = packageName.toString(),
                                                flags = OverriddenFlagsContainer(
                                                    boolValues = mapOf(flagName to newValue.toInt().toString())
                                                )
                                            )
                                            viewModel.initOverriddenBoolFlags(packageName.toString())
                                        }
                                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    },
                                    saveChecked = isEqual,
                                    saveOnCheckedChange = {
                                        if (it) {
                                            viewModel.saveFlag(
                                                flagName,
                                                packageName.toString(),
                                                FlagsTypes.BOOLEAN
                                            )
                                        } else {
                                            viewModel.deleteSavedFlag(
                                                flagName,
                                                packageName.toString()
                                            )
                                        }
                                    },
                                    lastItem = index == listBool.data.size - 1,
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

        is ua.polodarb.repository.uiStates.UiStates.Loading -> {
            LoadingProgressBar()
        }

        is ua.polodarb.repository.uiStates.UiStates.Error -> {
            NotFoundContent()
        }
    }
}
