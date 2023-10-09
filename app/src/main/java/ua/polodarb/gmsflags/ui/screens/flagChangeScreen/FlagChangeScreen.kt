package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.core.Extensions.toInt
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.ui.components.chips.GFlagFilterChipRow
import ua.polodarb.gmsflags.ui.components.dropDown.FlagChangeDropDown
import ua.polodarb.gmsflags.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.gmsflags.ui.components.searchBar.GFlagsSearchBar
import ua.polodarb.gmsflags.ui.components.tabs.GFlagsTabRow
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.ALL
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.CHANGED
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.DISABLED
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.ENABLED
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.dialogs.AddFlagDialog
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.dialogs.FlagChangeDialog

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FlagChangeScreen(
    onBackPressed: () -> Unit,
    packageName: String?
) {

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
    val localDensity = LocalDensity.current
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()


    // Tab bar
    var tabState by remember { mutableIntStateOf(0) }
    val titles = listOf(
        "Bool",
        "Int",
        "Float",
        "String",
//        "ExtVal"
    )

    val pagerState = rememberPagerState(pageCount = {
        4 // 5 with extVal
    })


    var textWidthDp by remember {
        mutableStateOf(0.dp)
    }

    // Filter
    var selectedChips by remember { mutableIntStateOf(0) }
    val chipsList = listOf("All", "Enabled", "Disabled", "Changed")

    // Tab state for filter button
    var tabFilterState by rememberSaveable {
        mutableStateOf(true)
    }


    // TopBar icons state
    var filterIconState by rememberSaveable {
        mutableStateOf(false)
    }
    var searchIconState by rememberSaveable {
        mutableStateOf(false)
    }

    // Chips index
    val chipIndex by remember {
        mutableIntStateOf(0)
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

    // Keyboard
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(searchIconState) {
        if (searchIconState)
            focusRequester.requestFocus()
    }

    val androidPackage = viewModel.getAndroidPackage(packageName.toString())

    // DropDown menu
    var dropDownExpanded by remember { mutableStateOf(false) }

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

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            packageName ?: "Null package name",
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
                                    viewModel.deleteOverriddenFlagByPackage(packageName = packageName.toString())
                                    viewModel.initBoolValues(delay = false)
                                    viewModel.initIntValues(delay = false)
                                    viewModel.initFloatValues(delay = false)
                                    viewModel.initStringValues(delay = false)
                                    viewModel.initOverriddenBoolFlags(packageName.toString())
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    dropDownExpanded = false
                                    Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
                                },
                                onOpenAppDetailsSettings = {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", androidPackage, null)
                                    intent.setData(uri)
                                    startActivity(context, intent, null)
                                },
                                onTurnOnAllBooleans = {
                                    viewModel.overrideAllFlag()
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
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                )
                GFlagsTabRow(
                    list = titles,
                    tabState = tabState,
                    topBarState = topBarState,
                    onClick = { index ->
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                        tabFilterState = index == 0
                        tabState = index
                    }
                )
                AnimatedVisibility(visible = filterIconState) {
                    GFlagFilterChipRow(
                        list = chipsList,
                        selectedChips = selectedChips,
                        pagerCurrentState = pagerState.currentPage,
                        chipOnClick = {
                            when (it) {
                                0 -> viewModel.filterMethod.value = ALL
                                1 -> viewModel.filterMethod.value = ENABLED
                                2 -> viewModel.filterMethod.value = DISABLED
                                3 -> viewModel.filterMethod.value = CHANGED
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
                        keyboardFocus = focusRequester
                    )
                }
            }
        },
//        bottomBar = {
//            BottomAppBar(
//                actions = {},
//                floatingActionButton = {
//                    FloatingActionButton(onClick = { /*TODO*/ }) {
//
//                    }
//                })
//        }
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
            userScrollEnabled = true,
            contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
        ) { page ->
            when (page) {
                0 -> {
                    when (uiStateBoolean.value) {
                        is FlagChangeUiStates.Success -> {

                            val listBool =
                                (uiStateBoolean.value as FlagChangeUiStates.Success).data.toSortedMap(
                                    compareByDescending<String> {
                                        it.toIntOrNull() ?: 0
                                    }.thenBy { it })

                            BooleanFlagsScreen(
                                listBool = listBool,
                                uiState = uiStateBoolean.value,
                                viewModel = viewModel,
                                packageName = packageName.toString(),
                                haptic = haptic,
                                savedFlagsList = savedFlags.value
                            )
                        }

                        is FlagChangeUiStates.Loading -> {}
                        is FlagChangeUiStates.Error -> {}
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
                    onFlagClick = { newFlagName, newFlagValue, newEditTextValue, newShowDialog ->
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
                    onFlagClick = { newFlagName, newFlagValue, newEditTextValue, newShowDialog ->
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
                    onFlagClick = { newFlagName, newFlagValue, newEditTextValue, newShowDialog ->
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
                viewModel.initOverriddenBoolFlags(packageName.toString(), false)
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

@Composable
fun BooleanFlagsScreen(
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
                    LazyColumn {
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
        is FlagChangeUiStates.Success -> {

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
                uiState.data.toList().sortedBy { it.first }.toMap()

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
                                onLongClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    Toast.makeText(
                                        context,
                                        "onLongClick",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
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

        is FlagChangeUiStates.Loading -> {
            LoadingProgressBar()
        }

        is FlagChangeUiStates.Error -> {
            ErrorLoadScreen()
        }
    }
}

enum class SelectFlagsType {
    BOOLEAN, INTEGER, FLOAT, STRING
}