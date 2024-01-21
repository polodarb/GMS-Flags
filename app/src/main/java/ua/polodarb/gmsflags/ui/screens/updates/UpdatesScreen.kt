package ua.polodarb.gmsflags.ui.screens.updates

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.gmsflags.ui.screens.UiStates

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdatesScreen() {

    val viewModel = koinViewModel<UpdatesScreenViewModel>()
    val state = viewModel.uiState.collectAsState()

    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topBarState)

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.nav_bar_updates),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Localized description"
                        )
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.loadArticles() },
                modifier = Modifier.offset(y = 24.dp),
                text = {
                    Text(
                        text = "Refresh",
                    )
                },
                icon = {
                    Icon(imageVector = Icons.Rounded.Refresh, contentDescription = null)
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            when (val result = state.value) {
                is UiStates.Success -> {
                    LazyColumn {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        itemsIndexed(result.data.articles) { index, item ->
                            UpdatesAppItem(
                                appTitle = item.title,
                                appVersion = item.date,
                                appDate = "Ver: " + item.version,
                                onClick = {

                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(it.calculateBottomPadding()))
                        }
                    }
                }

                is UiStates.Loading -> {
                    LoadingProgressBar()
                }

                is UiStates.Error -> {
                    Text(text = "err")
                }
            }
        }
    }
}

@Composable
private fun UpdatesAppItem(
    appTitle: String,
    appVersion: String,
    appDate: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(
                if (!isSystemInDarkTheme())
                    MaterialTheme.colorScheme.surfaceContainerLow
                else
                    MaterialTheme.colorScheme.surfaceContainer
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.weight(0.9f)
        ) {
            Text(
                text = appTitle,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(500)
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = appVersion,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400)
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            UpdatesAppDateLabel(appDate)
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
            )
        }
    }
}


@Composable
private fun UpdatesAppDateLabel(
    date: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(text = date, style = TextStyle(
            fontSize = 15.sp,
            fontWeight = FontWeight(500)
        ))
    }
}

@Preview
@Composable
private fun UpdatesAppItemPreview() {
    UpdatesAppItem(
        appTitle = "Google app",
        appVersion = "1.0.0",
        appDate = "12.12.2022",
        onClick = { /*TODO*/ }
    )
}