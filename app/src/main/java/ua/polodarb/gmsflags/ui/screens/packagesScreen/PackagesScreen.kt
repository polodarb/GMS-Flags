package ua.polodarb.gmsflags.ui.screens.packagesScreen

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemGestures
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(
    onFlagClick: (packageName: String) -> Unit,
    onBackPressed: () -> Unit
) {

    val viewModel = koinViewModel<PackagesScreenViewModel>()
    val uiState = viewModel.state.collectAsState()

    val list: MutableList<String> by remember {
        mutableStateOf(mutableListOf())
    }

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val coroutineScope = rememberCoroutineScope()

    // SearchBar
    var textSearchBar by remember {
        mutableStateOf("")
    }
    var activeSearchBar by remember {
        mutableStateOf(false)
    }
    val searchBarSizeWidth = animateDpAsState(
        if (!activeSearchBar) 56.dp else LocalConfiguration.current.screenWidthDp.dp,
        animationSpec = tween(
            durationMillis = if (activeSearchBar) 300 else 250,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )
    val searchBarSizeHeight = animateDpAsState(
        if (!activeSearchBar)
            56.dp
        else
            (LocalConfiguration.current.screenHeightDp.dp
                    + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                    + WindowInsets.systemGestures.getBottom(LocalDensity.current).dp),
        animationSpec = tween(
            durationMillis = if (activeSearchBar) 300 else 250,
            easing = LinearOutSlowInEasing
        ),
        label = ""
    )
    val animateSearchPadding = animateDpAsState(
        if (activeSearchBar) 0.dp else 12.dp,
        label = ""
    )
    val backgroundSearchColor by animateColorAsState(
        if (activeSearchBar) MaterialTheme.colorScheme.surfaceContainerHigh else MaterialTheme.colorScheme.surfaceContainerHigh.copy(
            alpha = 0f
        ),
        animationSpec = tween(
            durationMillis = if (activeSearchBar) 50 else 250,
            easing = LinearOutSlowInEasing,
            delayMillis = if (activeSearchBar) 0 else 100
        ),
        label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            "Packages",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
            ) {
                when (uiState.value) {
                    is PackagesScreenUiStates.Success -> {
                        list.addAll((uiState.value as PackagesScreenUiStates.Success).data)
                        Log.e("LIST1", "${list.toList()}")
                        SuccessListItems(
                            list = list.toList(),
                            onFlagClick = onFlagClick
                        )
                    }

                    is PackagesScreenUiStates.Loading -> {
                        LoadingProgressBar()
                    }

                    is PackagesScreenUiStates.Error -> {
                        ErrorLoadScreen()
                    }
                }
            }
        }
        SearchBar(
            query = textSearchBar,
            onQueryChange = {
                textSearchBar = it
            },
            onSearch = {
                activeSearchBar = false
            },
            active = activeSearchBar,
            onActiveChange = {
                activeSearchBar = it
                if (activeSearchBar) haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            colors = SearchBarDefaults.colors(
                containerColor = backgroundSearchColor,
                dividerColor = MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = animateSearchPadding.value)
                .size(
                    width = searchBarSizeWidth.value,
                    height = searchBarSizeHeight.value
                ),
            shape = MaterialTheme.shapes.extraLarge,
            content = {
                LazyColumn(content = { // for test
                    items(5) {
                        ListItem(headlineContent = { Text(text = "text") },
                            colors = ListItemColors(
                                containerColor = Color.Transparent,
                                headlineColor = MaterialTheme.colorScheme.onBackground,
                                leadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                overlineColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                supportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                trailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                disabledHeadlineColor = MaterialTheme.colorScheme.outline,
                                disabledLeadingIconColor = MaterialTheme.colorScheme.outline,
                                disabledTrailingIconColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                })
            }
        )
    }
}

@Composable
private fun SuccessListItems(
    list: List<String>,
    onFlagClick: (packageName: String) -> Unit
) {

    //todo: This code have problems with recompositions


    LazyColumn {
        itemsIndexed(list) { index, item ->
            LazyItem(
                packageName = item.split("|")[0],
                packagesCount = (item.split("|")[1]).toInt(),
                modifier = Modifier.clickable {
                    onFlagClick(item.split("|")[0])
                }
            )
        }
    }

}

@Composable
fun LoadingProgressBar() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 2.dp,
            modifier = Modifier.wrapContentSize()
        )
    }
}

@Composable
fun ErrorLoadScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Data loading error")
    }
}

@Composable
fun LazyItem(
    packageName: String,
    packagesCount: Int,
    modifier: Modifier = Modifier
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
        }
    )
}

@Composable
fun LazyItem(
    packageName: String,
    packagesCount: Int,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
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
                        contentDescription = "Localized description"
                    )
                } else {
                    Icon(
                        painterResource(id = R.drawable.ic_save_inactive),
                        contentDescription = "Localized description"
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
    HorizontalDivider(Modifier.padding(horizontal = 16.dp))
}