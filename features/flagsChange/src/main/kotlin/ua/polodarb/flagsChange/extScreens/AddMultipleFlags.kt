package ua.polodarb.flagsChange.extScreens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.flagsChange.SelectFlagsType
import ua.polodarb.flagsChange.dialogs.ProgressDialog
import ua.polodarb.flagschange.R
import ua.polodarb.ui.components.tabs.GFlagsTabRow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddFlagList(
    onBackPressed: () -> Unit,
    packageName: String,
) {

    val viewModel = koinViewModel<AddMultipleFlagsViewModel>(parameters = { parametersOf(packageName) })

    val booleanFlags = viewModel.booleanFlags.collectAsState().value
    val integerFlags = viewModel.integerFlags.collectAsState().value
    val floatFlags = viewModel.floatFlags.collectAsState().value
    val stringFlags = viewModel.stringFlags.collectAsState().value
    val boolSwitch = viewModel.boolSwitch.collectAsState().value

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)

    val showProgressDialog = rememberSaveable { mutableStateOf(false) }

    var tabState by remember { mutableIntStateOf(0) }
    val titles = persistentListOf("Bool", "Int", "Float", "String")
    val pagerState = rememberPagerState(pageCount = { 4 })

    val coroutineScope = rememberCoroutineScope()

    val haptic = LocalHapticFeedback.current

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.add_a_multiple_flags),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
                GFlagsTabRow(
                    list = titles,
                    tabState = tabState,
                    topBarState = topBarState,
                    onClick = { index ->
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                        tabState = index
                    }
                )
            }
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
                        onBackPressed()
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.close),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Button(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        viewModel.overrideFlags(
                            onStart = {
                                showProgressDialog.value = true
                            },
                            onComplete = {
                                showProgressDialog.value = false
                                coroutineScope.launch {
                                    delay(150)
                                    onBackPressed()
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = stringResource(R.string.save),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    ) {
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                when (page) {
                    0 -> tabState = 0
                    1 -> tabState = 1
                    2 -> tabState = 2
                    3 -> tabState = 3
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(top = it.calculateTopPadding())
        ) { page ->
            when (page) {
                0 -> AddFlagListContent(
                    flagList = booleanFlags,
                    flagType = SelectFlagsType.BOOLEAN,
                    onFlagListChange = { newFlag ->
                        viewModel.setBooleanFlag(newFlag)
                    },
                    boolCheckedState = boolSwitch,
                    boolOnStateChange = { newValue ->
                        viewModel.updateBoolSwitch(newValue)
                    }
                )

                1 -> AddFlagListContent(
                    flagList = integerFlags,
                    flagType = SelectFlagsType.INTEGER,
                    onFlagListChange = { newFlag ->
                        viewModel.setIntegerFlag(newFlag)
                    }
                )

                2 -> AddFlagListContent(
                    flagList = floatFlags,
                    flagType = SelectFlagsType.FLOAT,
                    onFlagListChange = { newFlag ->
                        viewModel.setFloatFlag(newFlag)
                    }
                )

                3 -> AddFlagListContent(
                    flagList = stringFlags,
                    flagType = SelectFlagsType.STRING,
                    onFlagListChange = { newFlag ->
                        viewModel.setStringFlag(newFlag)
                    }
                )
            }
        }

        ProgressDialog(showDialog = showProgressDialog.value)

    }
}

