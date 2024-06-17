package ua.polodarb.flagsfile

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.ui.components.dialogs.ProgressValueDialog
import ua.polodarb.repository.uiStates.UiStates
import ua.polodarb.ui.modifiers.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadFileScreen(
    fileUri: Uri?,
    onExit: () -> Unit
) {

    val viewModel = koinViewModel<LoadFileScreenViewModel>(parameters = { parametersOf(fileUri) })
    val flagsData = viewModel.flagsData.collectAsState()

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    val dialogShow = remember {
        mutableStateOf(false)
    }
    val dialogProgress = remember {
        mutableFloatStateOf(0f)
    }

    val isApplyButtonEnabled = remember {
        mutableStateOf(true)
    }

    when (val result = flagsData.value) {
        is UiStates.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Loading...")
            }
        }

        is UiStates.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: ${result.throwable?.message}")
            }
        }

        is UiStates.Success -> {
            Scaffold(
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = result.data.packageName,
                            )
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                bottomBar = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(start = 24.dp, end = 24.dp, bottom = 32.dp, top = 20.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                onExit()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.exit),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.1f))
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                dialogShow.value = true
                                isApplyButtonEnabled.value = false
                                viewModel.overrideFlags(
                                    progress = {
                                        dialogProgress.floatValue = it
                                    },
                                    onComplete = {
                                        dialogShow.value = false
                                        coroutineScope.launch(Dispatchers.Main) {
                                            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
                                            delay(300)
                                            onExit()
                                        }
                                    }
                                )
                            },
                            enabled = isApplyButtonEnabled.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.apply),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = it.calculateTopPadding()),
                ) {
                    LazyColumn {
                        val data = result.data.flags
                        itemsIndexed(data) { index, item ->
                            LoadFromFileListItem(
                                name = item.name,
                                value = item.value,
                                type = item.type,
                                isLastValue = index == data.size - 1,
                                checked = item.override,
                                onCheckedChange = { newValue ->
                                    viewModel.updateFlagOverride(item.name, newValue)
                                }
                            )
                        }
                    }
                }
            }

            ProgressValueDialog(showDialog = dialogShow.value, progress = dialogProgress.floatValue)

        }
    }
}

@Composable
fun LoadFromFileListItem(
    modifier: Modifier = Modifier,
    name: String,
    value: Any,
    type: String,
    isLastValue: Boolean,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(0.9f)) {
                Text(text = name, fontSize = 16.sp)
                Text(
                    text = "$type: $value",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        }
        if (!isLastValue) HorizontalDivider()
    }
}

@Composable
fun LoadFromFileListItemShimmer() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .shimmer(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(0.9f)) {
                Text(text = "Name", fontSize = 16.sp)
                Text(
                    text = "Type: Value",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}