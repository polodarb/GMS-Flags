package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.screens.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.screens.LoadingProgressBar
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FlagChangeScreen(
    onBackPressed: () -> Unit,
    packageName: String?
) {

    val viewModel = koinViewModel<FlagChangeScreenViewModel>()
    val uiState = viewModel.state.collectAsState()

    val boolValList: MutableList<String> by remember {
        mutableStateOf(mutableListOf())
    }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val lazyListState: LazyListState = rememberLazyListState()

    var state by remember { mutableStateOf(0) }
    val titles = listOf("Bool", "Int", "Float", "String")
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomTabIndicatorAnimaton(tabPositions = tabPositions, selectedTabIndex = state)
    }
    val pagerState = rememberPagerState(pageCount = {
        4
    })

    val coroutineScope = rememberCoroutineScope()

    var textWidthDp by remember {
        mutableStateOf(0.dp)
    }

    var selectedChips by remember { mutableIntStateOf(0) }
    val chipsList = listOf("All", "Enabled", "Disabled", "Changed")

    val localDensity = LocalDensity.current

    // TopBar icons state
    var filterIconState by rememberSaveable {
        mutableStateOf(false)
    }
    var searchIconState by rememberSaveable {
        mutableStateOf(false)
    }

    val clipboardManager = LocalClipboardManager.current

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
                                imageVector = Icons.Filled.Search,
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
                    selectedTabIndex = state,
                    indicator = indicator,
                    containerColor = lerp(
                        MaterialTheme.colorScheme.surfaceColorAtElevation(0.dp),
                        MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
                        FastOutLinearInEasing.transform(topBarState.collapsedFraction)
                    )
                ) {
                    titles.forEachIndexed { index, title ->
                        Tab(
                            selected = state == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                                state = index
                            },
                            text = {
                                Text(
                                    text = title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (state == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
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
//                        .horizontalScroll(rememberScrollState())
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 6.dp)
                            .height(36.dp)
                    ) {
                        chipsList.forEachIndexed { index, title ->
                            FilterChip(
                                selected = selectedChips == index,
                                onClick = {
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
//                        .horizontalScroll(rememberScrollState())
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp)
                            .height(64.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DockedSearchBar(
                            query = "",
                            onQueryChange = {},
                            onSearch = {},
                            active = false,
                            placeholder = {
                                Text(text = "Search a flag by name")
                            },
                            onActiveChange = {},
                            modifier = Modifier.fillMaxWidth()
                        ) {

                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                when (page) {
                    0 -> state = 0
                    1 -> state = 1
                    2 -> state = 2
                    3 -> state = 3
                }
            }
        }
        when (uiState.value) {
            is ScreenUiStates.Success -> {

//                boolValList.addAll((uiState.value as ScreenUiStates.Success).data)

                val list = (uiState.value as ScreenUiStates.Success).data

                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
                ) { page ->
                    when (page) {
                        0 -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyColumn {
                                    itemsIndexed(list) { index, item ->
                                        BoolValItem(
                                            flagName = item.split("|")[0],
                                            checked = if (item.split("|")[1] == "1" || item.split("|")[1] == "0") (item.split(
                                                "|"
                                            )[1]).toInt() != 0 else false,
                                            lastItem = index == list.size - 1,
                                            onCheckedChange = {
//                                                checked = it
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        1 -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyColumn {
                                    items(20) {
                                        IntFloatStringValItem("flagName", false, false, { }, { })
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(12.dp))
                                    }
                                }
                            }
                        }

                        2 -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyColumn {
                                    items(20) {
                                        IntFloatStringValItem("flagName", false, false, { }, { })
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(12.dp))
                                    }
                                }
                            }
                        }

                        3 -> {
                            Box(modifier = Modifier.fillMaxSize()) {
                                LazyColumn {
                                    items(20) {
                                        BoolValItem("flagName", false, false, { })
                                    }
                                    item {
                                        Spacer(modifier = Modifier.padding(12.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is ScreenUiStates.Loading -> {
                viewModel.getBoolFlags(packageName ?: "null")
                LoadingProgressBar()
            }

            is ScreenUiStates.Error -> {
                ErrorLoadScreen()
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