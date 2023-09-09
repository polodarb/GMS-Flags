package ua.polodarb.gmsflags.ui.screens.appsScreen.dialog

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
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreenDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onPackageClick: (packageName: String) -> Unit,
    pkgName: String,
    list: List<String>
) {

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Choose a package") },
            text = {
                Column(
                    modifier = Modifier.width(screenWidth - 78.dp) //todo: change dialog width
                ) {
                    DockedSearchBar(
                        query = "",
                        onQueryChange = {},
                        onSearch = {},
                        placeholder = {
                            Text(text = "Search a package")
                        },
                        trailingIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        },
                        colors = SearchBarDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.surfaceBright
                        ),
                        active = false,
                        onActiveChange = {},
                        enabled = false
                    ) {}
                    Spacer(modifier = Modifier.height(6.dp))
                    DialogPackagesList(list, pkgName, onPackageClick)
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
                    Text(text = "Exit")
                }
            }
        )
    }
}

@Composable
fun DialogPackagesList(
    allPackages: List<String>,
    pkgName: String,
    onPackageClick: (packageName: String) -> Unit
) {
    if (checkAppsListSeparation(allPackages, pkgName) && allPackages.size != 1) {
        DialogListWithSeparator(allPackages, pkgName, onPackageClick)
    } else {
        DialogListWithoutSeparator(allPackages, pkgName, onPackageClick)
    }
}

@Composable
fun DialogListWithSeparator(
    packagesList: List<String>,
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
                        it.contains("user#$pkgName")
        }

    val filteredSecondaryList = packagesList
        .filterNot {
            it == pkgName ||
                    it == "$pkgName#$pkgName" ||
                    it.contains("device#$pkgName") ||
                    it.contains("user#$pkgName")
        }

    LazyColumn(
        modifier = Modifier.clip(RoundedCornerShape(16.dp))
    ) {
        item {
            SeparatorText("Primary")
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
            SeparatorText("Secondary")
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
    packagesList: List<String>,
    pkgName: String,
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
            .padding(
                bottom =
                if (listStart) {
                    2.dp
                } else {
                    2.dp
                },
                top =
                if (listEnd) {
                    2.dp
                } else {
                    2.dp
                }
            )
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
                } else if (listEnd && listStart) {
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
            allPackages.contains("$pkgName#$pkgName")
}