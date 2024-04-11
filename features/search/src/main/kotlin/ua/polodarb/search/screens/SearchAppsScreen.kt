package ua.polodarb.search.screens

import android.graphics.drawable.Drawable
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import ua.polodarb.repository.appsList.AppInfo
import ua.polodarb.repository.uiStates.UiStates
import ua.polodarb.search.AppDialogList
import ua.polodarb.search.AppInfoList
import ua.polodarb.search.R
import ua.polodarb.search.dialogs.AppsScreenDialog
import ua.polodarb.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.ui.components.inserts.LoadingProgressBar
import ua.polodarb.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.ui.components.inserts.NotFoundContent
import ua.polodarb.ui.components.scrollbars.ListScrollbar

@Composable
fun SearchAppsScreen(
    appsListUIState: State<AppInfoList>,
    packagesListUIState: State<AppDialogList>,
    listState: LazyListState,
    showPackagesDialog: Boolean,
    dialogPackageText: String,
    onAppClick: (AppInfo) -> Unit,
    onDialogPackageClick: (String) -> Unit,
    onPackagesDialogDismiss: () -> Unit
) {
    Column {
        when (val result = appsListUIState.value) {
            is UiStates.Success -> {

                val appsList = result.data

                if (appsList.isEmpty()) NotFoundContent(NoFlagsOrPackages.APPS)

                ListScrollbar(
                    listState = listState
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        items(appsList) { item ->
                            AppListItem(
                                appName = item.appName,
                                pkg = item.applicationInfo.packageName,
                                appIcon = item.icon,
                                onClick = {
                                    onAppClick(item)
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(12.dp))
                        }
                    }
                }

                when (val dialogResult = packagesListUIState.value) {
                    is UiStates.Success -> {
                        AppsScreenDialog(
                            showDialog = showPackagesDialog,
                            onDismiss = onPackagesDialogDismiss,
                            pkgName = dialogPackageText,
                            list = dialogResult.data,
                            onPackageClick = onDialogPackageClick
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
//            .padding(horizontal = 8.dp)
//            .clip(RoundedCornerShape(24.dp))
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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    contentDescription = null
                )
            }
        }
    )
}
