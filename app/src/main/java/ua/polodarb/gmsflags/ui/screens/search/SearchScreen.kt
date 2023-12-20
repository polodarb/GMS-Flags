package ua.polodarb.gmsflags.ui.screens.search

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import my.nanihadesuka.compose.LazyColumnScrollbar
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.data.AppInfo
import ua.polodarb.gmsflags.ui.components.buttons.fab.GFlagsFab
import ua.polodarb.gmsflags.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.gmsflags.ui.components.inserts.NotFoundContent
import ua.polodarb.gmsflags.ui.components.searchBar.GFlagsSearchBar
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.search.dialog.AppsScreenDialog
import ua.polodarb.gmsflags.ui.screens.saved.CustomTabIndicatorAnimation

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AppsScreen(
    onSettingsClick: () -> Unit,
    onPackagesClick: () -> Unit,
    onPackageItemClick: (packageName: String) -> Unit,
    viewModel: SearchScreenViewModel = koinViewModel()
) {
    val focusRequester = remember { FocusRequester() }

    val uiState = viewModel.state.collectAsState()
    val dialogPackageText = viewModel.dialogPackage.collectAsState()
    val dialogDataState = viewModel.dialogDataState.collectAsState()

    val showDialog = rememberSaveable { mutableStateOf(false) }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    // Tabs
    var state by rememberSaveable { mutableIntStateOf(0) }
    val titles = persistentListOf("Apps", "All flags")
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        CustomTabIndicatorAnimation(
            tabPositions = tabPositions.toPersistentList(),
            selectedTabIndex = state
        )
    }
    val pagerState = rememberPagerState(pageCount = {
        2
    })

    var searchIconState by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(searchIconState) {
        if (searchIconState)
            focusRequester.requestFocus()
    }

    LaunchedEffect(viewModel.searchQuery.value) {
        viewModel.getAllInstalledApps()
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
                                if (!searchIconState) viewModel.searchQuery.value = ""
                            },
                            modifier = if (searchIconState) Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            else Modifier.background(Color.Transparent)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        }
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onPackagesClick()
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_packages),
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
                                    color = if (state == index) {
                                        MaterialTheme.colorScheme.primary
                                    } else {
                                        MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                )
                            },
                            modifier = Modifier
                                .padding(horizontal = 24.dp, vertical = 12.dp)
                                .height(40.dp)
                                .clip(MaterialTheme.shapes.extraLarge)
                        )
                    }
                }
                AnimatedVisibility(visible = searchIconState) {
                    GFlagsSearchBar(
                        query = viewModel.searchQuery.value,
                        onQueryChange = { newQuery ->
                            viewModel.searchQuery.value = newQuery
                        },
                        placeHolderText = stringResource(R.string.apps_search_advice),
                        iconVisibility = viewModel.searchQuery.value.isNotEmpty(),
                        iconOnClick = {
                            viewModel.searchQuery.value = ""
                            viewModel.getAllInstalledApps()
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
        Column(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {
            when (val result = uiState.value) {
                is UiStates.Success -> {

                    val appsList = result.data

                    if (appsList.isEmpty()) NotFoundContent(NoFlagsOrPackages.APPS)

                    LazyColumnScrollbar(
                        listState = listState,
                        thickness = 8.dp,
                        padding = 0.dp,
                        thumbColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                        thumbSelectedColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                    ) {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                            this.items(appsList) { item: AppInfo ->
                                AppListItem(
                                    appName = item.appName,
                                    pkg = item.applicationInfo.packageName,
                                    appIcon = item.icon,
                                    onClick = {
                                        showDialog.value = true
                                        coroutineScope.launch {
                                            withContext(Dispatchers.IO) {
                                                viewModel.getListByPackages(item.applicationInfo.packageName)
                                                viewModel.setPackageToDialog(item.applicationInfo.packageName)
                                            }
                                        }
                                    })
                            }
                            item {
                                Spacer(modifier = Modifier.padding(12.dp))
                            }
                        }
                    }

                    when (val dialogResult = dialogDataState.value) {
                        is UiStates.Success -> {
                            AppsScreenDialog(
                                showDialog.value,
                                onDismiss = {
                                    showDialog.value = false
                                    viewModel.setEmptyList()
                                },
                                pkgName = dialogPackageText.value,
                                list = dialogResult.data,
                                onPackageClick = {
                                    onPackageItemClick(it)
                                    showDialog.value = false
                                    viewModel.setEmptyList()
                                }
                            )
                        }

                        is UiStates.Loading -> {
                            LoadingProgressBar()
                        }

                        is UiStates.Error -> {
                            ErrorLoadScreen()
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
    }
}

@Stable
@Composable
fun AppListItem(
    appName: String,
    pkg: String,
    appIcon: Drawable,
    onClick: (String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick(pkg) },
        headlineContent = { Text(text = appName, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(text = pkg, fontSize = 13.sp) },
        leadingContent = {
            AsyncImage(model = appIcon, contentDescription = null, modifier = Modifier.size(52.dp))
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.offset(x = 12.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )
            }
        }
    )
}
