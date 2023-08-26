package ua.polodarb.gmsflags.ui.screens.appsScreen

import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.screens.LoadingProgressBar
import ua.polodarb.gmsflags.ui.screens.NotImplementedScreen
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates
import ua.polodarb.gmsflags.ui.theme.Typography

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

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

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
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Localized description"
                            )
                        }
                        IconButton(onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            onPackagesClick()
                        }) {
                            Icon(
                                painterResource(id = R.drawable.ic_packages),
                                contentDescription = "Localized description"
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

            }
        }
    ) { it ->
        Column(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            when (uiState.value) {
                is AppsScreenUiStates.Success -> {

                    val list =
                        (uiState.value as AppsScreenUiStates.Success).data

                    LazyColumn {
                        itemsIndexed(list.toList()) { index: Int, item: AppInfo ->
                            AppListItem(
                                appName = item.appName,
                                pkg = item.applicationInfo.packageName,
                                appIcon = item.icon,
                                flagsCount = item.packageNameCounts.toString(),
                                onClick = {
                                    viewModel.getListByPackages(item.applicationInfo.packageName)
                                    viewModel.setPackageToDialog(item.applicationInfo.packageName)
                                    showDialog.value = true
                                })
                        }
                        item {
                            Spacer(modifier = Modifier.padding(12.dp))
                        }
                    }
                    when (dialogDataState.value) {
                        is DialogUiStates.Success -> {

                            val dialogPackagesList =
                                (dialogDataState.value as DialogUiStates.Success).data

                            AppsScreenDialog(
                                showDialog.value,
                                onDismiss = { showDialog.value = false },
                                pkgName = dialogPackageText.value,
                                list = dialogPackagesList,
                                onPackageClick = {
                                    onPackageItemClick(it)
                                }
                            )
                        }

                        is DialogUiStates.Loading -> {
                            LoadingProgressBar()
                        }

                        is DialogUiStates.Error -> { }
                    }
                }

                is AppsScreenUiStates.Loading -> {
                    LoadingProgressBar()
                }

                is AppsScreenUiStates.Error -> {}
            }
        }
    }
}

@Composable
fun AppListItem(
    appName: String,
    pkg: String,
    appIcon: Drawable,
    flagsCount: String,
    onClick: (String) -> Unit
) {
    ListItem(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
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
                Text(
                    text = flagsCount
                )
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = null
                )
            }
        })
}