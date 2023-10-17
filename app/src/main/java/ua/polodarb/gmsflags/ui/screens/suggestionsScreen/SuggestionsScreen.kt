package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.data.remote.flags.dto.FlagInfo
import ua.polodarb.gmsflags.data.remote.flags.dto.FlagType
import ua.polodarb.gmsflags.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.suggestionsScreen.dialog.ResetFlagToDefaultDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsScreen(
    isFirstStart: Boolean,
    onSettingsClick: () -> Unit,
    onPackagesClick: () -> Unit
) {
    val viewModel = koinViewModel<SuggestionScreenViewModel>()

    val overriddenFlags = viewModel.stateSuggestionsFlags.collectAsState()

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    val listState = rememberLazyListState()
    val expandedFab by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 1 || listState.firstVisibleItemIndex == 0
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getAllOverriddenBoolFlags()
    }

    var showResetDialog by rememberSaveable {
        mutableStateOf(false)
    }

    // Reset Flags list
    var resetFlagsList: MutableList<FlagInfo> = mutableListOf()
    var resetFlagPackage = ""

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "Suggestions",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onPackagesClick()
                    }) {
                        Icon(
                            painterResource(id = R.drawable.ic_packages),
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSettingsClick()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            when (overriddenFlags.value) {
                is UiStates.Success -> {
                    val data = (overriddenFlags.value as UiStates.Success).data
                    LazyColumn(
                        state = listState
                    ) {
                        item {
                            WarningBanner(isFirstStart)
                        }
                        itemsIndexed(data.toList()) { index, item ->
                            SuggestedFlagItem(
                                flagName = item.flag.name,
                                senderName = item.flag.author,
                                flagValue = item.enabled,
                                flagOnCheckedChange = { bool ->
                                    coroutineScope.launch {
                                        withContext(Dispatchers.IO) {
                                            viewModel.updateFlagValue(bool, index)
                                            item.flag.flags.forEach { flag ->
                                                when (flag.type) {
                                                    FlagType.BOOL -> {
                                                        viewModel.overrideFlag(
                                                            packageName = item.flag.packageName,
                                                            name = flag.tag,
                                                            boolVal = if (bool) flag.value else "0"
                                                        )
                                                    }
                                                    FlagType.INTEGER -> {
                                                        viewModel.overrideFlag(
                                                            packageName = item.flag.packageName,
                                                            name = flag.tag,
                                                            intVal = if (bool) flag.value else "0"
                                                        )
                                                    }
                                                    FlagType.FLOAT -> {
                                                        viewModel.overrideFlag(
                                                            packageName = item.flag.packageName,
                                                            name = flag.tag,
                                                            floatVal = if (bool) flag.value else "0"
                                                        )
                                                    }
                                                    FlagType.STRING -> {
                                                        viewModel.overrideFlag(
                                                            packageName = item.flag.packageName,
                                                            name = flag.tag,
                                                            stringVal = if (bool) flag.value else ""
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                },
                                onFlagLongClick = {
                                    resetFlagPackage = item.flag.packageName
                                    resetFlagsList.addAll(item.flag.flags)
                                    showResetDialog = true
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                                    Toast.makeText(context, "${item.flag.flags}", Toast.LENGTH_SHORT).show()
                                }
                            )
                            ResetFlagToDefaultDialog(
                                showDialog = showResetDialog,
                                onDismiss = { showResetDialog = false }
                            ) {
                                showResetDialog = false
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.resetSuggestedFlagValue(resetFlagPackage, resetFlagsList)

                            }

                        }
                        item {
                            Spacer(modifier = Modifier.padding(44.dp))
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
//        FlagReportDialog(
//            showDialog.value,
//            onDismiss = { showDialog.value = false }
//        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SuggestedFlagItem(
    flagName: String,
    senderName: String,
    flagValue: Boolean,
    flagOnCheckedChange: (Boolean) -> Unit,
    onFlagLongClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(flagName) },
        supportingContent = { Text(stringResource(R.string.finder) + senderName) },
        trailingContent = {
            Row {
                Switch(
                    checked = flagValue,
                    onCheckedChange = {
                        flagOnCheckedChange(it)
                    },
                    enabled = true
                )
            }
        },
        modifier = Modifier.combinedClickable (
            onClick = {},
            onLongClick = onFlagLongClick
        )
    )
}

@Composable
fun WarningBanner(
    isFirstStart: Boolean
) {
    var visibility by rememberSaveable {
        mutableStateOf(isFirstStart)
    }

    AnimatedVisibility(
        visible = visibility,
        enter = fadeIn(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_force_stop),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp)
                )
                Text(
                    text = "To apply the flag, you need to click \"Force stop\" several times in the settings on the *target* App info settings page.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { visibility = false },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            disabledContainerColor = MaterialTheme.colorScheme.outline,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(text = "Close")
                    }
                }
            }
        }
    }
}