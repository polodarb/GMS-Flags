package ua.polodarb.gmsflags.ui.screens.appsScreen

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.data.AppInfo
import ua.polodarb.gmsflags.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.gmsflags.ui.components.searchBar.GFlagsSearchBar
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.appsScreen.dialog.AppsScreenDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(
    onSettingsClick: () -> Unit,
    onPackagesClick: () -> Unit,
    onPackageItemClick: (packageName: String) -> Unit,
) {

    val viewModel = koinViewModel<AppsScreenViewModel>()
    val uiState = viewModel.state.collectAsState()
    val dialogPackageText = viewModel.dialogPackage.collectAsState()
    val dialogDataState = viewModel.dialogDataState.collectAsState()

    val showDialog = rememberSaveable { mutableStateOf(false) }

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    // Keyboard
    val focusRequester = remember { FocusRequester() }

    var searchIconState by rememberSaveable {
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
                            "Apps",
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
                                contentDescription = "Settings"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
                AnimatedVisibility(visible = searchIconState) {
                    GFlagsSearchBar(
                        query = viewModel.searchQuery.value,
                        onQueryChange = { newQuery ->
                            viewModel.searchQuery.value = newQuery
                        },
                        placeHolderText = "Search a package name",
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
        }
    ) { it ->
        Column(
            modifier = Modifier.padding(top = it.calculateTopPadding())
        ) {
            when (uiState.value) {
                is UiStates.Success -> {

                    val appsList = (uiState.value as UiStates.Success).data

                    if (appsList.isEmpty()) NoFlagsOrPackages(NoFlagsOrPackages.APPS)

                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
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

                    when (dialogDataState.value) {
                        is UiStates.Success -> {

                            val dialogPackagesList =
                                (dialogDataState.value as UiStates.Success).data.toMutableList()

                            AppsScreenDialog(
                                showDialog.value,
                                onDismiss = {
                                    showDialog.value = false
                                    viewModel.setEmptyList()
                                    dialogPackagesList.clear()
                                },
                                pkgName = dialogPackageText.value,
                                list = dialogPackagesList.toList(),
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
        })
}
