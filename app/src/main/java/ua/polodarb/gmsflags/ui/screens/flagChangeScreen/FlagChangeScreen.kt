package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.koin.ext.clearQuotes
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.dialogs.FlagChangeDialog
import ua.polodarb.gmsflags.ui.screens.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.screens.LoadingProgressBar
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FlagChangeScreen(
    onBackPressed: () -> Unit,
    packageName: String?
) {

    val viewModel =
        koinViewModel<FlagChangeScreenViewModel>(parameters = { parametersOf(packageName) })

    val uiState = viewModel.state.collectAsState()
    val uiStateBoolean = viewModel.stateBoolean.collectAsState()
    val uiStateInteger = viewModel.stateInteger.collectAsState()

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    // Tab bar
    var tabState by remember { mutableIntStateOf(0) }
    val titles = listOf(
        "Bool",
        "Int",
        "Float",
        "String",
//        "ExtVal"
    )
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomTabIndicatorAnimaton(tabPositions = tabPositions, selectedTabIndex = tabState)
    }
    val pagerState = rememberPagerState(pageCount = {
        4 // 5 with extVal
    })

    val coroutineScope = rememberCoroutineScope()

    var textWidthDp by remember {
        mutableStateOf(0.dp)
    }

    // Filter
    var selectedChips by remember { mutableIntStateOf(0) }
    val chipsList = listOf("All", "Enabled", "Disabled", "Changed")

    val localDensity = LocalDensity.current

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


    // Flag change dialog
    val showDialog = remember { mutableStateOf(false) }
    var flagName by remember { mutableStateOf("") }
    var flagValue by remember { mutableStateOf("") }


    // DropDown menu
    var dropDownExpanded by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current


    // Filter Chips


    // Lists of flags type
    val listBool = remember { mutableMapOf<String, Boolean>() }
    val listInt = remember { mutableMapOf<String, String>() }
    val listFloat = remember { mutableMapOf<String, String>() }
    val listString = remember { mutableMapOf<String, String>() }

    fun MutableMap<String, Boolean>.filterByEnabled(): MutableMap<String, Boolean> {
        val filteredMap = mutableMapOf<String, Boolean>()
        for ((key, value) in this) {
            if (value) {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    fun MutableMap<String, Boolean>.filterByDisabled(): MutableMap<String, Boolean> {
        val filteredMap = mutableMapOf<String, Boolean>()
        for ((key, value) in this) {
            if (!value) {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    val changedFilterBoolList = mutableMapOf<String, Boolean>()

    val filteredBoolList = when (viewModel.filterMethod) {
        ENABLED -> listBool.filterByEnabled()
        DISABLED -> listBool.filterByDisabled()
        CHANGED -> changedFilterBoolList
        else -> listBool
    }


    // Search
    var searchBoolQuery by rememberSaveable {
        mutableStateOf("")
    }

    // State to hold the filtered list
    val filteredIntListState = remember { mutableStateMapOf<String, String>() }
    val filteredBoolListState = remember { mutableStateMapOf<String, Boolean>() }

//    val filteredIntListState by remember(listInt, searchBoolQuery, filterMethod, pagerState) {
//        derivedStateOf {
//            val filteredList = listBool.filter { it.key.contains(searchBoolQuery, ignoreCase = true) }
//            mutableStateMapOf<String, Boolean>().apply {
//                putAll(filteredList)
//            }
//        }
//    }

//    val filteredIntListState by remember(listInt, searchBoolQuery, filterMethod, pagerState) {
//        derivedStateOf {
//            val filteredList = listInt.filter { it.key.contains(searchBoolQuery, ignoreCase = true) }
//            mutableStateMapOf<String, String>().apply {
//                putAll(filteredList)
//            }
//        }
//    }

    LaunchedEffect(uiState.value, searchBoolQuery, tabState) {
        when (val state = uiState.value) {
            is FlagChangeUiStates.Success -> {
                when (tabState) {
                    0 -> {
                        val filteredList = filteredBoolList.filter {
                            it.key.contains(
                                searchBoolQuery,
                                ignoreCase = true
                            )
                        }
                        filteredBoolListState.clear()
                        filteredBoolListState.putAll(filteredList)
                    }

                    1 -> {
                        val filteredList =
                            listInt.filter { it.key.contains(searchBoolQuery, ignoreCase = true) }
                        filteredIntListState.clear()
                        filteredIntListState.putAll(filteredList)
                    }

                    else -> {}
                }

            }

            else -> {}
        }
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
                        AnimatedVisibility(visible = tabFilterState) {
                            IconButton(
                                onClick = {
                                    if (searchIconState) searchIconState = false
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
                        }
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (filterIconState) filterIconState = false
                                searchIconState = !searchIconState
//                                if (!searchIconState) {
//                                    searchQuery = ""
//                                    filteredListState.clear()
//                                    filteredListState.putAll(filteredBoolList)
//                                }
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
                TabRow(
                    selectedTabIndex = tabState,
                    indicator = indicator,
                    containerColor = lerp(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp),
                        MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                        FastOutLinearInEasing.transform(topBarState.collapsedFraction)
                    )
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = tabState == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.scrollToPage(index)
                                }
                                if (index != 0) filterIconState = false
                                tabFilterState = index == 0
                                tabState = index
                            },
                            text = {
                                Text(
                                    text = title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (tabState == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.onGloballyPositioned { coordinates ->
                                        textWidthDp =
                                            with(localDensity) { coordinates.size.width.toDp() }
                                    }
                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 4.dp, vertical = 8.dp)
                                .height(36.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                        )
                    }
                }
                AnimatedVisibility(visible = filterIconState) {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 6.dp)
                            .height(36.dp)
                    ) {
                        chipsList.forEachIndexed { index, title ->
                            FilterChip(
                                selected = selectedChips == index,
                                onClick = {
                                    when (index) {
                                        0 -> viewModel.filterMethod = ALL
                                        1 -> viewModel.filterMethod = ENABLED
                                        2 -> viewModel.filterMethod = DISABLED
                                        3 -> viewModel.filterMethod = CHANGED
                                    }
                                    selectedChips = index
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                },
                                colors = SelectableChipColors(
                                    containerColor = Color.Transparent,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    leadingIconColor = Color.Transparent,
                                    trailingIconColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    disabledLabelColor = Color.Transparent,
                                    disabledLeadingIconColor = Color.Transparent,
                                    disabledTrailingIconColor = Color.Transparent,
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    disabledSelectedContainerColor = Color.Transparent,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                    selectedLeadingIconColor = Color.Transparent,
                                    selectedTrailingIconColor = Color.Transparent
                                ),
                                label = {
                                    Text(
                                        text = title,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                },
                                leadingIcon = null,
                                modifier = Modifier
                                    .weight(if (index != 0) 1f else 0.8f)
                                    .padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
                AnimatedVisibility(visible = searchIconState) {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                            .height(64.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DockedSearchBar(
                            query = searchBoolQuery,
                            onQueryChange = { newQuery ->
                                searchBoolQuery = newQuery
                            },
                            onSearch = {},
                            active = false,
                            placeholder = {
                                Text(text = "Search a package name")
                            },
                            trailingIcon = {
                                AnimatedVisibility(
                                    visible = searchBoolQuery.isNotEmpty(),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    IconButton(onClick = {
                                        searchBoolQuery = ""
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = null
                                        )
                                    }
                                }
                            },
                            onActiveChange = {},
                            modifier = Modifier.fillMaxWidth()
                        ) { }
                    }
                }
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
//        when (uiState.value) {
//            is FlagChangeUiStates.Success -> {
//
////                val listBoolVal =
//                listBool.putAll((uiState.value as FlagChangeUiStates.Success).data.boolFlagsMap)
//                listInt.putAll((uiState.value as FlagChangeUiStates.Success).data.intFlagsMap)
//                val listFloatVal = (uiState.value as FlagChangeUiStates.Success).data.floatFlagsMap
//                val listStringVal =
//                    (uiState.value as FlagChangeUiStates.Success).data.stringFlagsMap
////                val listExtensionsVal = (uiState.value as FlagChangeUiStates.Success).data.extensionsVal

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
        ) { page ->
            when (page) {
                0 -> {
                    when (uiStateBoolean.value) {
                        is FlagChangeBooleanUiStates.Success -> {

                            val listBool = (uiStateBoolean.value as FlagChangeBooleanUiStates.Success).data

                            Box(modifier = Modifier.fillMaxSize()) {
                                if (listBool.isNotEmpty()) {
                                    LazyColumn {
                                        itemsIndexed(listBool.toList()) { index, item ->
                                            BoolValItem(
                                                flagName = listBool.keys.toList()[index],
                                                checked = listBool.values.toList()[index],
                                                lastItem = index == listBool.size - 1,
                                                onCheckedChange = {
//                                                checked = it
                                                    changedFilterBoolList[listBool.keys.toList()[index]] =
                                                        listBool.values.toList()[index]
                                                }
                                            )
                                        }
                                        item {
                                            Spacer(modifier = Modifier.padding(12.dp))
                                        }
                                    }
                                } else {
                                    NoFlagsType()
                                }
                            }
                        }

                        is FlagChangeBooleanUiStates.Loading -> {
                            LoadingProgressBar()

                        }

                        is FlagChangeBooleanUiStates.Error -> {
                            ErrorLoadScreen()
                        }
                    }

                }

                1 -> {
                    when (uiStateInteger.value) {
                        is FlagChangeOtherTypesUiStates.Success -> {

                            val listInt = (uiStateInteger.value as FlagChangeOtherTypesUiStates.Success).data

                            Box(modifier = Modifier.fillMaxSize()) {
                                if (listInt.isNotEmpty()) {
                                    LazyColumn {
                                        itemsIndexed(listInt.toList()) { index, item ->
                                            IntFloatStringValItem(
                                                flagName = listInt.keys.toList()[index],
                                                flagValue = listInt.values.toList()[index],
                                                lastItem = index == listInt.size - 1,
                                                savedButtonChecked = false,
                                                savedButtonOnChecked = {},
                                                haptic = haptic,
                                                context = context,
                                                onClick = {
                                                    flagName = item.first
                                                    flagValue = item.second
                                                    showDialog.value = true
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
                                        showDialog = showDialog.value,
                                        flagName = flagName,
                                        flagValue = flagValue,
                                        onQueryChange = {
                                            flagValue = it
                                        },
                                        flagType = "Integer",
                                        onConfirm = {
                                            Toast.makeText(
                                                context,
                                                "Not implemented",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onDismiss = {
                                            showDialog.value = false
                                        },
                                        onDefault = {
                                            Toast.makeText(
                                                context,
                                                "Reset value",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    )
                                } else {
                                    NoFlagsType()
                                }
                            }
                        }

                        is FlagChangeOtherTypesUiStates.Loading -> {
                            LoadingProgressBar()
                            viewModel.getIntFlags()
                        }

                        is FlagChangeOtherTypesUiStates.Error -> {
                            ErrorLoadScreen()
                        }
                    }
                }

                2 -> {
//                            Box(modifier = Modifier.fillMaxSize()) {
//                                if (listFloatVal.isNotEmpty()) {
//                                    LazyColumn {
//                                        itemsIndexed(listFloatVal.toList()) { index, item ->
//                                            IntFloatStringValItem(
//                                                flagName = listFloatVal.keys.toList()[index],
//                                                flagValue = listFloatVal.values.toList()[index],
//                                                lastItem = index == listFloatVal.size - 1,
//                                                savedButtonChecked = false,
//                                                savedButtonOnChecked = {},
//                                                haptic = haptic,
//                                                context = context,
//                                                onClick = {
//                                                    flagName = item.first
//                                                    flagValue = item.second
//                                                    showDialog.value = true
//                                                },
//                                                onLongClick = {
//                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                                                    Toast.makeText(
//                                                        context,
//                                                        "onLongClick",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                }
//                                            )
//                                        }
//                                        item {
//                                            Spacer(modifier = Modifier.padding(12.dp))
//                                        }
//                                    }
//                                    FlagChangeDialog(
//                                        showDialog = showDialog.value,
//                                        flagName = flagName,
//                                        flagValue = flagValue,
//                                        onQueryChange = {
//                                            flagValue = it
//                                        },
//                                        flagType = "Float",
//                                        onConfirm = {
//                                            Toast.makeText(
//                                                context,
//                                                "Not implemented",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        },
//                                        onDismiss = {
//                                            showDialog.value = false
//                                        },
//                                        onDefault = {
//                                            Toast.makeText(
//                                                context,
//                                                "Reset value",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    )
//                                } else {
//                                    NoFlagsType()
//                                }
//                            }
                }

                3 -> {
//                            Box(modifier = Modifier.fillMaxSize()) {
//                                if (listStringVal.isNotEmpty()) {
//                                    LazyColumn {
//                                        itemsIndexed(listStringVal.toList()) { index, item ->
//                                            IntFloatStringValItem(
//                                                flagName = listStringVal.keys.toList()[index],
//                                                flagValue = listStringVal.values.toList()[index],
//                                                lastItem = index == listStringVal.size - 1,
//                                                savedButtonChecked = false,
//                                                savedButtonOnChecked = {},
//                                                haptic = haptic,
//                                                context = context,
//                                                onClick = {
//                                                    flagName = item.first
//                                                    flagValue = item.second
//                                                    showDialog.value = true
//                                                },
//                                                onLongClick = {
//                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                                                    Toast.makeText(
//                                                        context,
//                                                        "onLongClick",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                }
//                                            )
//                                        }
//                                        item {
//                                            Spacer(modifier = Modifier.padding(12.dp))
//                                        }
//                                    }
//                                    FlagChangeDialog(
//                                        showDialog = showDialog.value,
//                                        flagName = flagName,
//                                        flagValue = flagValue,
//                                        onQueryChange = {
//                                            flagValue = it
//                                        },
//                                        flagType = "String",
//                                        onConfirm = {
//                                            Toast.makeText(
//                                                context,
//                                                "Not implemented",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        },
//                                        onDismiss = {
//                                            showDialog.value = false
//                                        },
//                                        onDefault = {
//                                            Toast.makeText(
//                                                context,
//                                                "Reset value",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    )
//                                } else {
//                                    NoFlagsType()
//                                }
//                            }
                }

//                        4 -> {
//                            Box(modifier = Modifier.fillMaxSize()) {
//                                if (listExtensionsVal.isNotEmpty()) {
//                                    LazyColumn {
//                                        itemsIndexed(listExtensionsVal.toList()) { index, item ->
//                                            IntFloatStringValItem(
//                                                flagName = listExtensionsVal.keys.toList()[index],
//                                                flagValue = listExtensionsVal.values.toList()[index],
//                                                lastItem = index == listExtensionsVal.size - 1,
//                                                savedButtonChecked = false,
//                                                savedButtonOnChecked = {},
//                                                haptic = haptic,
//                                                context = context,
//                                                onClick = {
//                                                    flagName = item.first
//                                                    flagValue = item.second
//                                                    showDialog.value = true
//                                                },
//                                                onLongClick = {
//                                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                                                    Toast.makeText(
//                                                        context,
//                                                        "onLongClick",
//                                                        Toast.LENGTH_SHORT
//                                                    ).show()
//                                                }
//                                            )
//                                        }
//                                        item {
//                                            Spacer(modifier = Modifier.padding(12.dp))
//                                        }
//                                    }
//                                    FlagChangeDialog(
//                                        showDialog = showDialog.value,
//                                        flagName = flagName,
//                                        flagValue = flagValue,
//                                        onQueryChange = {
//                                            flagValue = it
//                                        },
//                                        flagType = "Extension",
//                                        onConfirm = {
//                                            Toast.makeText(
//                                                context,
//                                                "Not implemented",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        },
//                                        onDismiss = {
//                                            showDialog.value = false
//                                        },
//                                        onDefault = {
//                                            Toast.makeText(
//                                                context,
//                                                "Reset value",
//                                                Toast.LENGTH_SHORT
//                                            ).show()
//                                        }
//                                    )
//                                } else {
//                                    NoFlagsType()
//                                }
//                            }
//                        }

            }
        }
//            }
//
//            is FlagChangeUiStates.Loading -> {
//                LoadingProgressBar()
//                viewModel.getFlagsData(packageName ?: "null")
//            }
//
//            is FlagChangeUiStates.Error -> {
//                ErrorLoadScreen()
//            }
//        }
    }
}


@Composable
fun CustomTabIndicator(color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier
            .wrapContentSize(Alignment.BottomCenter)
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .height(3.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(color)
    )
}

@Composable
fun CustomTabIndicatorAnimaton(tabPositions: List<TabPosition>, selectedTabIndex: Int) {
    val transition = updateTransition(selectedTabIndex, label = "")
    val indicatorStart by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 500f)
            } else {
                spring(dampingRatio = 1f, stiffness = 1500f)
            }
        }, label = ""
    ) {
        tabPositions[it].left
    }

    val indicatorEnd by transition.animateDp(
        transitionSpec = {
            if (initialState < targetState) {
                spring(dampingRatio = 1f, stiffness = 1500f)
            } else {
                spring(dampingRatio = 1f, stiffness = 500f)
            }
        }, label = ""
    ) {
        tabPositions[it].right
    }

    CustomTabIndicator(
        color = MaterialTheme.colorScheme.primary, modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorStart)
            .width(indicatorEnd - indicatorStart)
    )
}

@Composable
fun NoFlagsType() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "¯\\_(ツ)_/¯\n\nNo flags of this type",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun FlagChangeDropDown(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(top = 44.dp)
    ) {
        MaterialTheme(
            shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
        ) {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = onDismissRequest
            )
            {
                DropdownMenuItem(
                    text = { Text("Choose an account") },
                    onClick = { /* Handle onClick */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.AccountCircle,
                            contentDescription = null
                        )
                    })
                DropdownMenuItem(
                    text = { Text("Reset all overridden flags") },
                    onClick = { /* Handle onClick */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = null
                        )
                    })
                DropdownMenuItem(
                    text = { Text("Add flag") },
                    onClick = { /* Handle onClick */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = null
                        )
                    })
            }
        }
    }
}