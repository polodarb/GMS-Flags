package ua.polodarb.gmsflags.ui.screens.packagesScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.components.searchBar.GFlagsSearchBar
import ua.polodarb.gmsflags.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(
    onFlagClick: (packageName: String) -> Unit,
    onBackPressed: () -> Unit
) {

    val viewModel = koinViewModel<PackagesScreenViewModel>()
    val uiState = viewModel.state.collectAsState()

    val list = remember {
        mutableStateMapOf<String, String>()
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val haptic = LocalHapticFeedback.current

    // Keyboard
    val focusRequester = remember { FocusRequester() }

    var searchIconState by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(searchIconState) {
        if (searchIconState)
            focusRequester.requestFocus()
    }

    // Search
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    // state to hold the filtered list
    val filteredListState = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(uiState.value, searchQuery) {
        when (val state = uiState.value) {
            is ScreenUiStates.Success -> {
                val filteredList =
                    state.data.filter { it.key.contains(searchQuery, ignoreCase = true) }
                filteredListState.clear()
                filteredListState.putAll(filteredList)
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
                            stringResource(id = R.string.packages_title),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                searchIconState = !searchIconState
                                if (!searchIconState) searchQuery = ""
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
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
                AnimatedVisibility(visible = searchIconState) {
                    GFlagsSearchBar(
                        query = searchQuery,
                        onQueryChange = { newQuery ->
                            searchQuery = newQuery
                        },
                        iconVisibility = searchQuery.isNotEmpty(),
                        iconOnClick = {
                            searchQuery = ""
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        },
                        placeHolderText = stringResource(id = R.string.packages_search_advice),
                        keyboardFocus = focusRequester
                    )
                }
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            when (uiState.value) {
                is ScreenUiStates.Success -> {
                    list.putAll((uiState.value as ScreenUiStates.Success).data)
                    SuccessListItems(
                        list = filteredListState,
                        onFlagClick = onFlagClick
                    )
                }
                is ScreenUiStates.Loading -> LoadingProgressBar()
                is ScreenUiStates.Error -> ErrorLoadScreen()
            }
        }
    }
}

@Composable
private fun SuccessListItems(
    list: Map<String, String>,
    onFlagClick: (packageName: String) -> Unit
) {

    //todo: This code have problems with recompositions

    LazyColumn {
        itemsIndexed(list.toList()) { index, item ->
            LazyItem(
                packageName = item.first,
                packagesCount = item.second.toInt(),
                lastItem = index == list.size - 1,
                modifier = Modifier.clickable {
                    onFlagClick(item.first)
                })
        }
        item {
            Spacer(modifier = Modifier.padding(12.dp))
        }
    }

}

@Composable
fun LazyItem(
    modifier: Modifier = Modifier,
    packageName: String,
    packagesCount: Int,
    lastItem: Boolean = false,
) {
    var checkedState by rememberSaveable {
        mutableStateOf(false)
    }

    LazyItem(
        packageName = packageName,
        packagesCount = packagesCount,
        modifier = modifier,
        checked = checkedState,
        onCheckedChange = { checked ->
            checkedState = checked
        },
        lastItem = lastItem
    )
}

@Composable
fun LazyItem(
    modifier: Modifier = Modifier,
    packageName: String,
    packagesCount: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    lastItem: Boolean,
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            IconToggleButton(checked = checked, onCheckedChange = onCheckedChange) {
                if (checked) {
                    Icon(
                        painterResource(id = R.drawable.ic_save_active),
                        contentDescription = null
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_save_inactive),
                        contentDescription = null
                    )
                }
            }
            Column(Modifier.weight(0.9f)) {
                Text(text = packageName, style = Typography.bodyMedium)
                Text(
                    text = "Flags: $packagesCount",
                    style = Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            Icon(
                modifier = Modifier
                    .padding(16.dp),
                painter = painterResource(id = R.drawable.ic_next),
                contentDescription = null
            )
        }
    }
    if (!lastItem) HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}
