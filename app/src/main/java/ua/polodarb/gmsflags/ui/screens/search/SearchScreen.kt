package ua.polodarb.gmsflags.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.stopScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabPosition
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.components.buttons.fab.GFlagsFab
import ua.polodarb.gmsflags.ui.components.searchBar.GFlagsSearchBar
import ua.polodarb.gmsflags.ui.components.tabs.GFlagsTabRow
import ua.polodarb.gmsflags.ui.screens.packages.PackagesScreenUiStates
import ua.polodarb.gmsflags.ui.screens.saved.CustomTabIndicatorAnimation
import ua.polodarb.gmsflags.ui.screens.search.subScreens.SearchAppsScreen
import ua.polodarb.gmsflags.ui.screens.search.subScreens.SearchFlagsScreen
import ua.polodarb.gmsflags.ui.screens.search.subScreens.SearchPackagesScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AppsScreen(
    onSettingsClick: () -> Unit,
    onDialogPackageItemClick: (packageName: String) -> Unit,
    onAllPackagesItemClick: (packageName: String) -> Unit,
    viewModel: SearchScreenViewModel = koinViewModel()
) {

    // Keyboard
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    // Apps List Screen
    val appsListUiState: State<AppInfoList> = viewModel.appsListUiState.collectAsState()
    val dialogPackagesUiState = viewModel.dialogDataState.collectAsState()
    val dialogTitle = viewModel.dialogPackage.collectAsState()
    val showPackagesDialog = rememberSaveable { mutableStateOf(false) }

    // All Packages Screen
    val packagesListUiState: State<PackagesScreenUiStates> =
        viewModel.packagesListUiState.collectAsState()
    val savedPackagesListUiState: State<List<String>> =
        viewModel.stateSavedPackages.collectAsState()

    // All Flags Screen

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val appsLazyListState = rememberLazyListState()
    val packagesLazyListState = rememberLazyListState()

    // Tabs
    var state by rememberSaveable { mutableIntStateOf(0) }
    val titles = persistentListOf("Apps", "All packages", "All flags")
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomTabIndicatorAnimation(
            tabPositions = tabPositions.toPersistentList(),
            selectedTabIndex = state
        )
    }
    val pagerState = rememberPagerState(pageCount = {
        3
    })

    var searchIconState by remember {
        mutableStateOf(false)
    }

    val searchPlaceHolderText = when (pagerState.currentPage) {
        0 -> stringResource(R.string.apps_search_advice)
        1 -> stringResource(R.string.packages_search_advice)
        else -> stringResource(R.string.flags_search_advice)
    }

    LaunchedEffect(pagerState.targetPage) {
        appsLazyListState.stopScroll()
        packagesLazyListState.stopScroll()
    }

    LaunchedEffect(searchIconState) {
        if (searchIconState)
            focusRequester.requestFocus()
    }

    LaunchedEffect(
        key1 = viewModel.appsSearchQuery.value,
        key2 = viewModel.packagesSearchQuery.value,
        key3 = viewModel.allFlagsSearchQuery.value
    ) {
        when (pagerState.currentPage) {
            0 -> viewModel.getAllInstalledApps()
            1 -> viewModel.getGmsPackagesList()
            else -> {} // viewModel.getAllFlags()
        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            stringResource(id = R.string.nav_bar_search),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                searchIconState = !searchIconState
                                if (!searchIconState) {
                                    when (pagerState.currentPage) {
                                        0 -> {
                                            viewModel.appsSearchQuery.value = ""
                                            viewModel.getAllInstalledApps()
                                        }
                                        1 -> {
                                            viewModel.packagesSearchQuery.value = ""
                                            viewModel.getGmsPackagesList()
                                        }
                                        else -> {
                                            viewModel.allFlagsSearchQuery.value = ""
                                            // viewModel.getAllFlags()
                                        }
                                    }
                                }
                            },
                            modifier = if (searchIconState) Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                                .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                            else Modifier.background(Color.Transparent)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onSettingsClick()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = null
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
                GFlagsTabRow(
                    list = titles,
                    tabState = state,
                    topBarState = topBarState,
                    enabled = true,
                    onClick = { index ->
                        coroutineScope.launch {
                            pagerState.scrollToPage(index)
                        }
                        state = index
                    }
                )
                AnimatedVisibility(visible = searchIconState) {
                    GFlagsSearchBar(
                        query = when (pagerState.currentPage) {
                            0 -> viewModel.appsSearchQuery.value
                            1 -> viewModel.packagesSearchQuery.value
                            else -> viewModel.allFlagsSearchQuery.value
                        },
                        onQueryChange = { newQuery ->
                            when (pagerState.currentPage) {
                                0 -> viewModel.appsSearchQuery.value = newQuery
                                1 -> viewModel.packagesSearchQuery.value = newQuery
                                else -> viewModel.allFlagsSearchQuery.value = newQuery
                            }
                        },
                        placeHolderText = searchPlaceHolderText,
                        iconVisibility = when (pagerState.currentPage) {
                            0 -> viewModel.appsSearchQuery.value.isNotEmpty()
                            1 -> viewModel.packagesSearchQuery.value.isNotEmpty()
                            else -> viewModel.allFlagsSearchQuery.value.isNotEmpty()
                        },
                        iconOnClick = {
                            when (pagerState.currentPage) {
                                0 -> {
                                    viewModel.appsSearchQuery.value = ""
                                    viewModel.getAllInstalledApps()
                                }
                                1 -> {
                                    viewModel.packagesSearchQuery.value = ""
                                    viewModel.getGmsPackagesList()
                                }
                                2 -> {
                                    viewModel.allFlagsSearchQuery.value = ""
                                    // viewModel.getAllFlags()
                                }
                            }
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        colorFraction = FastOutLinearInEasing.transform(topBarState.collapsedFraction),
                        keyboardFocus = focusRequester
                    )
                }
            }
        },
        floatingActionButton = {
            GFlagsFab(
                onClick = { /*TODO*/ },
                visible = if (state == 0) true else false,
                backgroundColor = MaterialTheme.colorScheme.primary,
                modifier = Modifier.offset(x = (-12).dp, y = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sort),
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                when (page) {
                    0 -> state = 0
                    1 -> state = 1
                    2 -> state = 2
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = true,
            contentPadding = PaddingValues(top = paddingValues.calculateTopPadding())
        ) { page ->
            when (page) {
                0 -> SearchAppsScreen(
                    appsListUIState = appsListUiState,
                    packagesListUIState = dialogPackagesUiState,
                    listState = appsLazyListState,
                    showPackagesDialog = showPackagesDialog.value,
                    dialogPackageText = dialogTitle.value,
                    onAppClick = { item ->
                        showPackagesDialog.value = true
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                viewModel.getListByPackages(item.applicationInfo.packageName)
                                viewModel.setPackageToDialog(item.applicationInfo.packageName)
                            }
                        }
                    },
                    onDialogPackageClick = {
                        onDialogPackageItemClick(it)
                        showPackagesDialog.value = false
                        viewModel.setEmptyList()
                    },
                    onPackagesDialogDismiss = {
                        showPackagesDialog.value = false
                        viewModel.setEmptyList()
                    }
                )

                1 -> SearchPackagesScreen(
                    uiState = packagesListUiState,
                    savedPackagesList = savedPackagesListUiState.value,
                    lazyListState = packagesLazyListState,
                    onPackageClick = { pkgName ->
                        focusManager.clearFocus()
                        keyboardController?.hide()
                        onAllPackagesItemClick(pkgName)
                    },
                    onSavePackageClick = { value, item ->
                        if (value) {
                            viewModel.savePackage(item.first)
                        } else {
                            viewModel.deleteSavedPackage(item.first)
                        }
                    }
                )

                2 -> SearchFlagsScreen()
            }
        }

    }
}