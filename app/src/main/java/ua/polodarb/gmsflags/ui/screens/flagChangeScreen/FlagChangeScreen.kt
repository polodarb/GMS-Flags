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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.core.Extensions.toInt
import ua.polodarb.gmsflags.ui.dialogs.FlagChangeDialog
import ua.polodarb.gmsflags.ui.screens.LoadingProgressBar
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.ALL
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.CHANGED
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.DISABLED
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FilterMethod.ENABLED

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

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val localDensity = LocalDensity.current
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

    // Flags values
    var booleanValue by remember {
        mutableStateOf(false)
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


    // Flag change dialog
    val showDialog = remember { mutableStateOf(false) }
    var flagName by remember { mutableStateOf("") }
    var flagValue by remember { mutableStateOf("") }


    // Keyboard
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(searchIconState) {
        if (searchIconState)
            focusRequester.requestFocus()
    }


    // DropDown menu
    var dropDownExpanded by remember { mutableStateOf(false) }

    // IntFloatStrValues
    val intEditTextValue = rememberSaveable {
        mutableStateOf("")
    }
    val floatEditTextValue = rememberSaveable {
        mutableStateOf("")
    }
    val stringEditTextValue = rememberSaveable {
        mutableStateOf("")
    }


    LaunchedEffect(
        viewModel.filterMethod.value,
        viewModel.searchQuery.value,
        pagerState.targetPage
    ) {
        viewModel.getBoolFlags()
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
                        AnimatedVisibility(visible = tabFilterState) {
                            IconButton(
                                onClick = {
                                    if (searchIconState) searchIconState = false
                                    Log.e("packageName", packageName.toString())
                                    viewModel.initOverriddenBoolFlags(packageName!!) //todo
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
                                onClearCache = {
                                    viewModel.clearPhenotypeCache(packageName!!)
                                    Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
                                },
                                onDeleteOverriddenFlags = {
                                    viewModel.deleteOverriddenFlagByPackage(packageName = packageName.toString())
//                                    when (tabState) {
//                                        0 -> viewModel.initBoolValues(delay = false)
//                                        1 -> viewModel.initIntValues(delay = false)
//                                        2 -> viewModel.initFloatValues(delay = false)
//                                        3 -> viewModel.initStringValues(delay = false)
//                                    }
                                    viewModel.initBoolValues(delay = false)
                                    viewModel.initIntValues(delay = false)
                                    viewModel.initFloatValues(delay = false)
                                    viewModel.initStringValues(delay = false)
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    dropDownExpanded = false
                                    Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
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
                                        0 -> viewModel.filterMethod.value = ALL
                                        1 -> viewModel.filterMethod.value = ENABLED
                                        2 -> viewModel.filterMethod.value = DISABLED
                                        3 -> viewModel.filterMethod.value = CHANGED
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
                                    selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    disabledSelectedContainerColor = Color.Transparent,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    selectedLeadingIconColor = Color.Transparent,
                                    selectedTrailingIconColor = Color.Transparent
                                ),
                                label = {
                                    Text(
                                        text = title,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
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
                            query = viewModel.searchQuery.value,
                            onQueryChange = { newQuery ->
                                viewModel.searchQuery.value = newQuery
                            },
                            onSearch = {},
                            active = false,
                            placeholder = {
                                Text(text = "Search a package name")
                            },
                            trailingIcon = {
                                AnimatedVisibility(
                                    visible = viewModel.searchQuery.value.isNotEmpty(),
                                    enter = fadeIn(),
                                    exit = fadeOut()
                                ) {
                                    IconButton(onClick = {
                                        viewModel.searchQuery.value = ""
                                        viewModel.getBoolFlags()
                                        viewModel.getIntFlags()
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(focusRequester = focusRequester)
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

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
        ) { page ->
            when (page) {
                0 -> {
                    when (uiStateBoolean.value) {
                        is FlagChangeUiStates.Success -> {

                            val listBool =
                                (uiStateBoolean.value as FlagChangeUiStates.Success).data

                            Box(modifier = Modifier.fillMaxSize()) {
                                if (listBool.isNotEmpty()) {
                                    LazyColumn {
                                        itemsIndexed(listBool.keys.toList()) { index, flagName ->
                                            val checked =
                                                if (listBool.values.toList()[index] == "1") true else false
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
                            NoFlagsType()
                        }
                    }

                }

                1 -> {
                    when (uiStateInteger.value) {
                        is FlagChangeUiStates.Success -> {

                            val listInt =
                                (uiStateInteger.value as FlagChangeUiStates.Success).data

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
                                                    intEditTextValue.value = flagValue
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
                                        flagValue = intEditTextValue.value,
                                        onQueryChange = {
                                            intEditTextValue.value = it
                                        },
                                        flagType = "Integer",
                                        onConfirm = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showDialog.value = false
                                            viewModel.overrideFlag(
                                                packageName = packageName.toString(),
                                                name = flagName,
                                                intVal = intEditTextValue.value
                                            )
                                            viewModel.updateIntFlagValue(
                                                flagName,
                                                intEditTextValue.value
                                            )
                                        },
                                        onDismiss = {
                                            showDialog.value = false
                                            intEditTextValue.value = flagValue
                                        },
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
                            NoFlagsType()
                        }
                    }
                }

                2 -> {
                    when (uiStateFloat.value) {
                        is FlagChangeUiStates.Success -> {

                            val listFloat =
                                (uiStateFloat.value as FlagChangeUiStates.Success).data

                            Box(modifier = Modifier.fillMaxSize()) {
                                if (listFloat.isNotEmpty()) {
                                    LazyColumn {
                                        itemsIndexed(listFloat.toList()) { index, item ->
                                            IntFloatStringValItem(
                                                flagName = listFloat.keys.toList()[index],
                                                flagValue = listFloat.values.toList()[index],
                                                lastItem = index == listFloat.size - 1,
                                                savedButtonChecked = false,
                                                savedButtonOnChecked = {},
                                                haptic = haptic,
                                                context = context,
                                                onClick = {
                                                    flagName = item.first
                                                    flagValue = item.second
                                                    floatEditTextValue.value = flagValue
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
                                        flagValue = floatEditTextValue.value,
                                        onQueryChange = {
                                            floatEditTextValue.value = it
                                        },
                                        flagType = "Float",
                                        onConfirm = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showDialog.value = false
                                            viewModel.overrideFlag(
                                                packageName = packageName.toString(),
                                                name = flagName,
                                                floatVal = floatEditTextValue.value
                                            )
                                            viewModel.updateFloatFlagValue(
                                                flagName,
                                                floatEditTextValue.value
                                            )
                                        },
                                        onDismiss = {
                                            showDialog.value = false
                                            floatEditTextValue.value = flagValue
                                        },
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
                            NoFlagsType()
                        }
                    }
                }

                3 -> {
                    when (uiStateString.value) {
                        is FlagChangeUiStates.Success -> {

                            val listString =
                                (uiStateString.value as FlagChangeUiStates.Success).data

                            Box(modifier = Modifier.fillMaxSize()) {
                                if (listString.isNotEmpty()) {
                                    LazyColumn {
                                        itemsIndexed(listString.toList()) { index, item ->
                                            IntFloatStringValItem(
                                                flagName = listString.keys.toList()[index],
                                                flagValue = listString.values.toList()[index],
                                                lastItem = index == listString.size - 1,
                                                savedButtonChecked = false,
                                                savedButtonOnChecked = {},
                                                haptic = haptic,
                                                context = context,
                                                onClick = {
                                                    flagName = item.first
                                                    flagValue = item.second
                                                    stringEditTextValue.value = flagValue
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
                                        flagValue = stringEditTextValue.value,
                                        onQueryChange = {
                                            stringEditTextValue.value = it
                                        },
                                        flagType = "String",
                                        onConfirm = {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            showDialog.value = false
                                            viewModel.overrideFlag(
                                                packageName = packageName.toString(),
                                                name = flagName,
                                                stringVal = stringEditTextValue.value
                                            )
                                            viewModel.updateStringFlagValue(
                                                flagName,
                                                stringEditTextValue.value
                                            )
                                        },
                                        onDismiss = {
                                            showDialog.value = false
                                            stringEditTextValue.value = flagValue
                                        },
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
                            NoFlagsType()
                        }
                    }
                }
            }
        }
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
            text = "¯\\_(ツ)_/¯\n\nFlags not found",
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
    onClearCache: () -> Unit,
    onDeleteOverriddenFlags: () -> Unit,
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
                    text = { Text("Add flag") },
                    onClick = { /* Handle onClick */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = null
                        )
                    },
                    enabled = false
                )
                DropdownMenuItem( // todo: implement condition on flag type
                    text = { Text("Activate all visible flags") },
                    onClick = { /* Handle onClick */ },
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_activate_all),
                            contentDescription = null
                        )
                    },
                    enabled = false
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text("Reset all overridden flags") },
                    onClick = onDeleteOverriddenFlags,
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_reset_flags),
                            contentDescription = null
                        )
                    },
                    enabled = true
                )
                DropdownMenuItem(
                    text = { Text("Refresh flags list") },
                    onClick = { /* Handle onClick */ },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = null
                        )
                    },
                    enabled = false
                )
                DropdownMenuItem(
                    text = { Text("Force stop this app package") },
                    onClick = { /* Handle onClick */ },
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_force_stop),
                            contentDescription = null
                        )
                    },
                    enabled = false
                )
            }
        }
    }
}