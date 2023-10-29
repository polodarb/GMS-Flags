package ua.polodarb.gmsflags.ui.screens.flagChange

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.internal.immutableListOf
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.utils.Extensions.toSortMap
import ua.polodarb.gmsflags.ui.components.chips.GFlagFilterChipRow
import ua.polodarb.gmsflags.ui.components.dropDown.FlagChangeDropDown
import ua.polodarb.gmsflags.ui.components.dropDown.FlagSelectDropDown
import ua.polodarb.gmsflags.ui.components.searchBar.GFlagsSearchBar
import ua.polodarb.gmsflags.ui.components.tabs.GFlagsTabRow
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.flagChange.FilterMethod.ALL
import ua.polodarb.gmsflags.ui.screens.flagChange.FilterMethod.CHANGED
import ua.polodarb.gmsflags.ui.screens.flagChange.FilterMethod.DISABLED
import ua.polodarb.gmsflags.ui.screens.flagChange.FilterMethod.ENABLED
import ua.polodarb.gmsflags.ui.screens.flagChange.dialogs.AddFlagDialog
import ua.polodarb.gmsflags.ui.screens.flagChange.dialogs.ProgressDialog
import ua.polodarb.gmsflags.ui.screens.flagChange.dialogs.ReportFlagsDialog
import ua.polodarb.gmsflags.ui.screens.flagChange.dialogs.SuggestFlagsDialog
import ua.polodarb.gmsflags.ui.screens.flagChange.flagsType.BooleanFlagsScreen
import ua.polodarb.gmsflags.ui.screens.flagChange.flagsType.OtherTypesFlagsScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FlagChangeScreen(
    onBackPressed: () -> Unit,
    packageName: String?
) {
    val focusRequester = remember { FocusRequester() }

    val viewModel =
        koinViewModel<FlagChangeScreenViewModel>(parameters = { parametersOf(packageName) })

    val uiStateBoolean = viewModel.stateBoolean.collectAsState()
    val uiStateInteger = viewModel.stateInteger.collectAsState()
    val uiStateFloat = viewModel.stateFloat.collectAsState()
    val uiStateString = viewModel.stateString.collectAsState()

    val savedFlags = viewModel.stateSavedFlags.collectAsState()

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()


    // Tab bar
    var tabState by remember { mutableIntStateOf(0) }
    val titles = persistentListOf("Bool", "Int", "Float", "String")

    val pagerState = rememberPagerState(pageCount = {
        4 // 5 with extVal
    })


    // Select states
    var isInSelectionMode by rememberSaveable {
        mutableStateOf(false)
    }

    val resetSelectionMode = {
        isInSelectionMode = false
        viewModel.selectedItems.clear()
    }

    BackHandler(
        enabled = isInSelectionMode,
    ) {
        resetSelectionMode()
    }

    LaunchedEffect(
        key1 = isInSelectionMode,
        key2 = viewModel.selectedItems.size,
    ) {
        if (isInSelectionMode && viewModel.selectedItems.isEmpty()) {
            isInSelectionMode = false
        }
    }

    // Filter
    var selectedChips by remember { mutableIntStateOf(0) }
    val chipsList = persistentListOf("All", "Changed", "Disabled", "Enabled")

    // Tab state for filter button
    var tabFilterState by rememberSaveable {
        mutableStateOf(true)
    }


    // TopBar icons state
    var filterIconState by rememberSaveable {
        mutableStateOf(false)
    }
    var searchIconState by remember {
        mutableStateOf(false)
    }

    // Flag change dialog
    val showDialog = remember { mutableStateOf(false) }
    var flagName by remember { mutableStateOf("") }
    var flagValue by remember { mutableStateOf("") }

    // Add flag dialog
    val showAddFlagDialog = remember { mutableStateOf(false) }
    var flagType by rememberSaveable { mutableIntStateOf(0) }
    var flagBoolean by rememberSaveable { mutableIntStateOf(0) }
    var flagAddValue by rememberSaveable { mutableStateOf("") }
    var flagAddName by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(searchIconState) {
        if (searchIconState)
            focusRequester.requestFocus()
    }

    val androidPackage = viewModel.getAndroidPackage(packageName.toString())

    // DropDown menu
    var dropDownExpanded by remember { mutableStateOf(false) }
    var selectDropDownExpanded by remember { mutableStateOf(false) }

    // IntFloatStrValues
    val editTextValue = rememberSaveable {
        mutableStateOf("")
    }

    LaunchedEffect(
        viewModel.filterMethod.value,
        viewModel.searchQuery.value,
        pagerState.targetPage,
        showAddFlagDialog.value
    ) {
        viewModel.getBoolFlags()
        viewModel.initOverriddenBoolFlags(packageName.toString())
        viewModel.getIntFlags()
        viewModel.getFloatFlags()
        viewModel.getStringFlags()
    }


    // SuggestFlagsDialog
    val showSendSuggestDialog = remember { mutableStateOf(false) }
    val suggestFlagDesc = remember { mutableStateOf("") }
    val senderName = remember { mutableStateOf("") }

    // ReportFlagsDialog
    val showSendReportDialog = remember { mutableStateOf(false) }
    val reportFlagDesc = remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = if (isInSelectionMode) "Selected: ${viewModel.selectedItems.size}" else packageName
                                ?: "Null package name",
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .combinedClickable(
                                    onClick = { },
                                    onLongClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        clipboardManager.setText(AnnotatedString(packageName.toString()))
                                    },
                                    onLongClickLabel = ""
                                ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                if (searchIconState) searchIconState = false
                                viewModel.initOverriddenBoolFlags(packageName.toString()) //todo
                                filterIconState = !filterIconState
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            },
                            modifier = if (filterIconState) Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            else Modifier.background(Color.Transparent)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_filter),
                                contentDescription = "Filter"
                            )
                        }
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (filterIconState) filterIconState = false
                                searchIconState = !searchIconState
                            },
                            modifier = if (searchIconState) Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            else Modifier.background(Color.Transparent)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = "Localized description"
                            )
                        }
                        IconButton(
                            onClick = {
                                dropDownExpanded = !dropDownExpanded
                            }
                        ) {
                            FlagChangeDropDown(
                                expanded = dropDownExpanded,
                                onDismissRequest = { dropDownExpanded = false },
                                onAddFlag = {
                                    showAddFlagDialog.value = true
                                },
                                onDeleteOverriddenFlags = {
                                    dropDownExpanded = false
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    coroutineScope.launch {
                                        viewModel.showProgressDialog.value = true
                                        viewModel.showFalseProgressDialog()
                                        viewModel.deleteOverriddenFlagByPackage(packageName = packageName.toString())
                                        viewModel.initBoolValues()
                                    }
                                },
                                onOpenAppDetailsSettings = {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", androidPackage, null)
                                    intent.data = uri
                                    startActivity(context, intent, null)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        if (isInSelectionMode) {
                            IconButton(onClick = {
                                resetSelectionMode()
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Localized description"
                                )
                            }
                        } else {
                            IconButton(onClick = onBackPressed) {
                                Icon(
                                    imageVector = Icons.Filled.ArrowBack,
                                    contentDescription = "Localized description"
                                )
                            }
                        }
                    },
                )
                AnimatedVisibility(visible = !isInSelectionMode) {
                    GFlagsTabRow(
                        list = titles,
                        tabState = tabState,
                        topBarState = topBarState,
                        enabled = !isInSelectionMode,
                        onClick = { index ->
                            coroutineScope.launch {
                                pagerState.scrollToPage(index)
                            }
                            tabFilterState = index == 0
                            tabState = index
                        }
                    )
                }
                AnimatedVisibility(visible = filterIconState) {
                    GFlagFilterChipRow(
                        list = chipsList,
                        selectedChips = selectedChips,
                        pagerCurrentState = pagerState.currentPage,
                        colorFraction = FastOutLinearInEasing.transform(topBarState.collapsedFraction),
                        chipOnClick = {
                            when (it) {
                                0 -> viewModel.filterMethod.value = ALL
                                1 -> viewModel.filterMethod.value = CHANGED
                                2 -> viewModel.filterMethod.value = DISABLED
                                3 -> viewModel.filterMethod.value = ENABLED
                            }
                            selectedChips = it
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        })
                }
                AnimatedVisibility(visible = searchIconState) {
                    GFlagsSearchBar(
                        query = viewModel.searchQuery.value,
                        onQueryChange = { newQuery ->
                            viewModel.searchQuery.value = newQuery
                        },
                        placeHolderText = "Search a flags by name",
                        iconVisibility = viewModel.searchQuery.value.isNotEmpty(),
                        iconOnClick = {
                            viewModel.searchQuery.value = ""
                            viewModel.getBoolFlags()
                            viewModel.getIntFlags()
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        colorFraction = FastOutLinearInEasing.transform(topBarState.collapsedFraction),
                        keyboardFocus = focusRequester
                    )
                }
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = isInSelectionMode,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                BottomAppBar(
                    actions = {
                        IconButton(onClick = { selectDropDownExpanded = !selectDropDownExpanded }) {
                            FlagSelectDropDown(
                                expanded = selectDropDownExpanded,
                                onDismissRequest = { selectDropDownExpanded = false },
                                onEnableSelected = {
                                    viewModel.showProgressDialog.value = true
                                    viewModel.enableSelectedFlag()
                                    viewModel.showFalseProgressDialog(viewModel.selectedItems.size)
                                },
                                onDisableSelected = {
                                    viewModel.showProgressDialog.value = true
                                    viewModel.disableSelectedFlag()
                                    viewModel.showFalseProgressDialog(viewModel.selectedItems.size)
                                },
                                onSelectAllItems = {
                                    viewModel.selectAllItems()
                                })
                            Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                        }
                        IconButton(onClick = {
                            showSendReportDialog.value = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }, enabled = true) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_report),
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {
                            showSendSuggestDialog.value = true
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }, enabled = true) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_navbar_suggestions_inactive),
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {

                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                            when (val result = uiStateBoolean.value) {
                                is UiStates.Success -> {

                                    val listBool = result.data

                                    val selectedItemsWithValues =
                                        viewModel.selectedItems.mapNotNull { selectedItem ->
                                            val value = listBool[selectedItem]
                                            if (value != null) {
                                                "$selectedItem: $value"
                                            } else {
                                                null
                                            }
                                        }

                                    val flagsText = selectedItemsWithValues.joinToString("\n")

                                    val intent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(
                                            Intent.EXTRA_SUBJECT,
                                            "Extracted flags from GMS Flags"
                                        )
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "Package: \n${packageName.toString()}\n\n" +
                                                    "Flags: \n$flagsText"
                                        )
                                    }
                                    context.startActivity(intent)
                                }
                                else -> {}
                            }
                        }, enabled = true) {
                            Icon(imageVector = Icons.Outlined.Share, contentDescription = null)
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                viewModel.saveSelectedFlags()
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.showProgressDialog.value = true
                                viewModel.showFalseProgressDialog(viewModel.selectedItems.size)
                                coroutineScope.launch {
                                    delay(35)
                                    resetSelectionMode()
                                }
                            },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_save_inactive),
                                "Localized description"
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
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
            userScrollEnabled = !isInSelectionMode,
            contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
        ) { page ->
            when (page) {
                0 -> {
                    when (val result = uiStateBoolean.value) {
                        is UiStates.Success -> {

                            val listBool = result.data

//                            if (listBool.isNotEmpty()) { // todo
//                                val itemIndex = listBool.keys.indexOf(item)
//                                if (itemIndex != -1) {
//                                    LaunchedEffect(Unit) {
//                                        delay(50)
//                                        coroutineScope.launch {
//                                            listState.scrollToItem(itemIndex)
//                                        }
//                                    }
//                                } else {
//                                    Toast.makeText(context, "Flag not found!", Toast.LENGTH_SHORT).show()
//                                }
//                            }

                            BooleanFlagsScreen(
                                listBool = listBool,
                                uiState = uiStateBoolean.value,
                                viewModel = viewModel,
                                packageName = packageName.toString(),
                                haptic = haptic,
                                savedFlagsList = savedFlags.value,
                                isSelectedList = viewModel.selectedItems,
                                selectedItemLongClick = { isSelected, flagName ->
                                    if (isInSelectionMode) {
                                        if (isSelected) {
                                            viewModel.selectedItems.remove(flagName)
                                        } else {
                                            viewModel.selectedItems.add(flagName)
                                        }
                                    } else {
                                        isInSelectionMode = true
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        viewModel.selectedItems.add(flagName)
                                    }
                                },
                                selectedItemShortClick = { isSelected, flagName ->
                                    if (isInSelectionMode) {
                                        if (isSelected) {
                                            viewModel.selectedItems.remove(flagName)
                                        } else {
                                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                            viewModel.selectedItems.add(flagName)
                                        }
                                    }
                                }
                            )
                        }

                        is UiStates.Loading -> {}
                        is UiStates.Error -> {}
                    }

                }

                1 -> OtherTypesFlagsScreen(
                    uiState = uiStateInteger.value,
                    viewModel = viewModel,
                    packageName = packageName.toString(),
                    flagName = flagName,
                    flagValue = flagValue,
                    flagsType = SelectFlagsType.INTEGER,
                    editTextValue = editTextValue.value,
                    showDialog = showDialog.value,
                    onFlagClick = { newFlagName, newFlagValue, newEditTextValue, _ ->
                        flagName = newFlagName
                        flagValue = newFlagValue
                        editTextValue.value = newEditTextValue
                        showDialog.value = true
                    },
                    dialogOnQueryChange = {
                        editTextValue.value = it
                        flagValue = editTextValue.value
                    },
                    dialogOnConfirm = {
                        showDialog.value = false
                    },
                    dialogOnDismiss = {
                        showDialog.value = false
                        editTextValue.value = flagValue
                    },
                    haptic = haptic,
                    context = context,
                    savedFlagsList = savedFlags.value
                )

                2 -> OtherTypesFlagsScreen(
                    uiState = uiStateFloat.value,
                    viewModel = viewModel,
                    packageName = packageName.toString(),
                    flagName = flagName,
                    flagValue = flagValue,
                    flagsType = SelectFlagsType.FLOAT,
                    editTextValue = editTextValue.value,
                    showDialog = showDialog.value,
                    onFlagClick = { newFlagName, newFlagValue, newEditTextValue, _ ->
                        flagName = newFlagName
                        flagValue = newFlagValue
                        editTextValue.value = newEditTextValue
                        showDialog.value = true
                    },
                    dialogOnQueryChange = {
                        editTextValue.value = it
                        flagValue = editTextValue.value
                    },
                    dialogOnConfirm = {
                        showDialog.value = false
                    },
                    dialogOnDismiss = {
                        showDialog.value = false
                        editTextValue.value = flagValue
                    },
                    haptic = haptic,
                    context = context,
                    savedFlagsList = savedFlags.value
                )

                3 -> OtherTypesFlagsScreen(
                    uiState = uiStateString.value,
                    viewModel = viewModel,
                    packageName = packageName.toString(),
                    flagName = flagName,
                    flagValue = flagValue,
                    flagsType = SelectFlagsType.STRING,
                    editTextValue = editTextValue.value,
                    showDialog = showDialog.value,
                    onFlagClick = { newFlagName, newFlagValue, newEditTextValue, _ ->
                        flagName = newFlagName
                        flagValue = newFlagValue
                        editTextValue.value = newEditTextValue
                        showDialog.value = true
                    },
                    dialogOnQueryChange = {
                        editTextValue.value = it
                        flagValue = editTextValue.value
                    },
                    dialogOnConfirm = {
                        showDialog.value = false
                    },
                    dialogOnDismiss = {
                        showDialog.value = false
                        editTextValue.value = flagValue
                    },
                    haptic = haptic,
                    context = context,
                    savedFlagsList = savedFlags.value
                )
            }
        }

        SuggestFlagsDialog(
            showDialog = showSendSuggestDialog.value,
            flagDesc = suggestFlagDesc.value,
            onFlagDescChange = {
                suggestFlagDesc.value = it
            },
            senderName = senderName.value,
            onSenderNameChanged = {
                senderName.value = it
            },
            onSend = {
                when (val result = uiStateBoolean.value) {
                    is UiStates.Success -> {

                        val listBool = result.data.toSortMap()

                        val selectedItemsWithValues =
                            viewModel.selectedItems.mapNotNull { selectedItem ->
                                val value = listBool[selectedItem]
                                if (value != null) {
                                    "$selectedItem: $value"
                                } else {
                                    null
                                }
                            }

                        val flagsText = selectedItemsWithValues.joinToString("\n")

                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("gmsflags@gmail.com"))
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                "Flags suggestion by ${senderName.value}"
                            )
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Sender: ${senderName.value}\n\n" +
                                        "Package: ${packageName.toString()}\n\n" +
                                        "Description: \n${suggestFlagDesc.value}\n\n" +
                                        "Flags: \n${flagsText}"
                            )
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                context, "No app to send email. Please install at least one",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        showSendSuggestDialog.value = false
                        suggestFlagDesc.value = ""
                        senderName.value = ""
                    }

                    else -> {}
                }
            },
            onDismiss = {
                showSendSuggestDialog.value = false
                suggestFlagDesc.value = ""
                senderName.value = ""
            })

        ReportFlagsDialog(
            showDialog = showSendReportDialog.value,
            flagDesc = reportFlagDesc.value,
            onFlagDescChange = {
                reportFlagDesc.value = it
            },
            onSend = {
                when (val result = uiStateBoolean.value) {
                    is UiStates.Success -> {

                        val listBool = result.data.toSortMap()

                        val selectedItemsWithValues =
                            viewModel.selectedItems.mapNotNull { selectedItem ->
                                val value = listBool[selectedItem]
                                if (value != null) {
                                    "$selectedItem: $value"
                                } else {
                                    null
                                }
                            }

                        val flagsText = selectedItemsWithValues.joinToString("\n")

                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:")
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("gmsflags@gmail.com"))
                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                "Problem report"
                            )
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Package: ${packageName.toString()}\n\n" +
                                        "Description: \n${reportFlagDesc.value}\n\n" +
                                        "Flags: \n${flagsText}"
                            )
                        }
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(
                                context, "No app to send email. Please install at least one",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        showSendReportDialog.value = false
                        reportFlagDesc.value = ""
                    }

                    else -> {}
                }
            },
            onDismiss = {
                showSendReportDialog.value = false
                reportFlagDesc.value = ""
            })


        ProgressDialog(showDialog = viewModel.showProgressDialog.value)

        AddFlagDialog(
            showDialog = showAddFlagDialog.value,
            flagType = flagType,
            onFlagTypeChange = { flagType = it },
            flagBoolean = flagBoolean,
            onFlagBooleanChange = { flagBoolean = it },
            flagName = flagAddName,
            flagNameChange = { flagAddName = it },
            flagValue = flagAddValue,
            flagValueChange = { flagAddValue = it },
            onAddFlag = {
                when (flagType) {
                    0 -> {
                        viewModel.overrideFlag(
                            packageName = packageName.toString(),
                            name = flagAddName,
                            boolVal = if (flagBoolean == 0) "1" else "0"
                        )
                    }

                    1 -> {
                        viewModel.overrideFlag(
                            packageName = packageName.toString(),
                            name = flagAddName,
                            intVal = flagAddValue
                        )
                    }

                    2 -> {
                        viewModel.overrideFlag(
                            packageName = packageName.toString(),
                            name = flagAddName,
                            floatVal = flagAddValue
                        )
                    }

                    3 -> {
                        viewModel.overrideFlag(
                            packageName = packageName.toString(),
                            name = flagAddName,
                            stringVal = flagAddValue
                        )
                    }
                }
                viewModel.initOverriddenBoolFlags(packageName.toString())
                viewModel.clearPhenotypeCache(packageName.toString())
                dropDownExpanded = false
                showAddFlagDialog.value = false
                flagAddName = ""
                flagAddValue = ""
            },
            onDismiss = {
                showAddFlagDialog.value = false
                flagAddName = ""
                flagAddValue = ""
            }
        )
    }
}

enum class SelectFlagsType {
    BOOLEAN, INTEGER, FLOAT, STRING
}
