package ua.polodarb.gmsflags.ui.screens.apps.dialog

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import ua.polodarb.gmsflags.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreenDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onPackageClick: (packageName: String) -> Unit,
    pkgName: String,
    list: PersistentList<String>
) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    var searchQuery by rememberSaveable { mutableStateOf(value = "") }

    val filteredList = list.filter { it.contains(searchQuery) }.toPersistentList()

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
                searchQuery = ""
            },
            title = { Text(text = stringResource(R.string.apps_dialog_choose_package)) },
            text = {
                Column(
                    modifier = Modifier.width(screenWidth - 78.dp) //todo: change dialog width
                ) {
                    DockedSearchBar(
                        query = searchQuery,
                        onQueryChange = { query ->
                            searchQuery = query
                        },
                        onSearch = {},
                        placeholder = {
                            Text(text = stringResource(id = R.string.packages_search_advice))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Search,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            AnimatedVisibility(
                                visible = searchQuery.isNotEmpty(),
                                enter = fadeIn(),
                                exit = fadeOut()
                            ) {
                                IconButton(onClick = {
                                    searchQuery = ""
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        colors = SearchBarDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceBright
                        ),
                        active = false,
                        onActiveChange = {},
                        enabled = true
                    ) {}
                    Spacer(modifier = Modifier.height(6.dp))
                    DialogPackagesList(filteredList, pkgName) {
                        onPackageClick(it)
                        searchQuery = ""
                    }
                }
            },
            properties = DialogProperties().let {
                DialogProperties(
                    dismissOnBackPress = it.dismissOnBackPress,
                    dismissOnClickOutside = it.dismissOnClickOutside,
                    securePolicy = it.securePolicy,
                    usePlatformDefaultWidth = false
                )
            },
            confirmButton = {
                OutlinedButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.close))
                }
            }
        )
    }
}

@Composable
fun DialogPackagesList(
    allPackages: PersistentList<String>,
    pkgName: String,
    onPackageClick: (packageName: String) -> Unit
) {
    if (checkAppsListSeparation(allPackages, pkgName) && allPackages.size != 1) {
        DialogListWithSeparator(allPackages, pkgName, onPackageClick)
    } else {
        DialogListWithoutSeparator(allPackages, onPackageClick)
    }
}

@Composable
fun DialogListWithSeparator(
    packagesList: PersistentList<String>,
    pkgName: String,
    onPackageClick: (packageName: String) -> Unit,
) {
    val filteredPrimaryList = packagesList
        .filter {
            if (pkgName == "com.google.android.googlequicksearchbox")
                it == pkgName ||
                        it == "$pkgName#$pkgName" ||
                        it.contains("com.google.android.libraries.search.googleapp.device#$pkgName") ||
                        it.contains("com.google.android.libraries.search.googleapp.user#$pkgName")
            else
                it == pkgName ||
                        it == "$pkgName#$pkgName" ||
                        it.contains("device#$pkgName") ||
                        it.contains("user#$pkgName") ||
                        it.contains("finsky")
        }

    val filteredSecondaryList = packagesList
        .filterNot {
            it == pkgName ||
                    it == "$pkgName#$pkgName" ||
                    it.contains("device#$pkgName") ||
                    it.contains("user#$pkgName") ||
                    it.contains("finsky")
        }

    LazyColumn(
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
    ) {
        item {
            SeparatorText(stringResource(R.string.primary))
        }
        itemsIndexed(filteredPrimaryList.toList()) { index: Int, item: String ->
            DialogListItem(
                itemText = item,
                listStart = index == 0,
                listEnd = index == filteredPrimaryList.size - 1,
                onPackageClick = onPackageClick
            )
        }
        item {
            SeparatorText(stringResource(R.string.secondary))
        }
        itemsIndexed(filteredSecondaryList.toList()) { index: Int, item: String ->
            DialogListItem(
                itemText = item,
                listStart = index == 0,
                listEnd = index == filteredSecondaryList.size - 1,
                onPackageClick = onPackageClick
            )
        }
    }
}

@Composable
fun SeparatorText(text: String) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 8.dp, top = 16.dp, bottom = 6.dp)
    )
}

@Composable
fun DialogListWithoutSeparator(
    packagesList: PersistentList<String>,
    onPackageClick: (packageName: String) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .padding(top = 16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        itemsIndexed(packagesList.toList()) { index: Int, item: String ->
            DialogListItem(
                itemText = item,
                listStart = index == 0,
                listEnd = index == packagesList.size - 1,
                onPackageClick = onPackageClick
            )
        }
    }
}

@Composable
fun DialogListItem(
    itemText: String,
    listStart: Boolean,
    listEnd: Boolean,
    onPackageClick: (packageName: String) -> Unit,
) {
    Box(
        modifier = Modifier
            .padding(vertical = 2.dp)
            .clip(
                if (listStart && !listEnd) {
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = 4.dp,
                        bottomEnd = 4.dp
                    )
                } else if (listEnd && !listStart) {
                    RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp,
                        topStart = 4.dp,
                        topEnd = 4.dp
                    )
                } else if (listEnd) {
                    RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp,
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )
                } else {
                    RoundedCornerShape(4.dp)
                }
            )
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .clickable { onPackageClick(itemText) }
    ) {
        Text(
            text = itemText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )
    }
}

fun checkAppsListSeparation(allPackages: List<String>, pkgName: String): Boolean {
    return allPackages.contains(pkgName) ||
            allPackages.contains("device#$pkgName") ||
            allPackages.contains("user#$pkgName") ||
            allPackages.contains("$pkgName#$pkgName") ||
            allPackages.any { it.contains("finsky") }
}
