package ua.polodarb.search.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.ui.theme.Typography
import ua.polodarb.repository.uiStates.UiStates
import ua.polodarb.search.PackagesScreenUiStates
import ua.polodarb.search.R
import ua.polodarb.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.ui.components.inserts.LoadingProgressBar
import ua.polodarb.ui.components.scrollbars.ListScrollbar

@Composable
fun SearchPackagesScreen(
    uiState: State<PackagesScreenUiStates>,
    savedPackagesList: List<String>,
    lazyListState: LazyListState,
    onPackageClick: (packageName: String) -> Unit,
    onSavePackageClick: (value: Boolean, item: Pair<String, String>) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState.value) {
            is UiStates.Success -> {
                SuccessListItems(
                    list = (uiState.value as UiStates.Success).data,
                    listState = lazyListState,
                    savedPackagesList = savedPackagesList,
                    onPackageClick = { packageName ->
                        onPackageClick(packageName)
                    },
                    onSavePackageClick = { it, item ->
                        onSavePackageClick(it, item)
                    }
                )
            }

            is UiStates.Loading -> LoadingProgressBar()
            is UiStates.Error -> ErrorLoadScreen()
        }
    }
}

@Composable
private fun SuccessListItems(
    list: Map<String, String>,
    listState: LazyListState,
    savedPackagesList: List<String>,
    onPackageClick: (packageName: String) -> Unit,
    onSavePackageClick: (value: Boolean, item: Pair<String, String>) -> Unit
) {

    ListScrollbar(
        listState = listState
    ) {
        LazyColumn(
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }
            itemsIndexed(list.toList()) { index, item ->
                PackagesLazyItem(
                    packageName = item.first,
                    packagesCount = item.second.toInt(),
                    checked = savedPackagesList.contains(item.first),
                    onCheckedChange = {
                        onSavePackageClick(it, item)
                    },
                    lastItem = index == list.size - 1,
                    modifier = Modifier.clickable {
                        onPackageClick(item.first)
                    })
            }
            item {
                Spacer(modifier = Modifier.padding(12.dp))
            }
        }
    }

}

@Composable
fun PackagesLazyItem(
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null
            )
        }
    }
    if (!lastItem) HorizontalDivider(Modifier.padding(horizontal = 16.dp), color = MaterialTheme.colorScheme.surfaceContainerHighest)
}