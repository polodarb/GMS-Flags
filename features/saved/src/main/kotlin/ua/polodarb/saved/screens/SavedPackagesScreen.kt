package ua.polodarb.saved.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ua.polodarb.saved.R
import ua.polodarb.saved.SavedScreenViewModel
import ua.polodarb.ui.components.inserts.NoFlagsOrPackages
import ua.polodarb.ui.components.inserts.NotFoundContent

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SavedPackagesScreen(
    savedPackagesList: List<String>,
    viewModel: SavedScreenViewModel,
    onPackageClick: (packageName: String) -> Unit
) {
    if (savedPackagesList.isEmpty()) {
        NotFoundContent(NoFlagsOrPackages.PACKAGES)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(savedPackagesList) { index, item ->
                SavedPackagesLazyItem(
                    packageName = item,
                    checked = savedPackagesList.contains(item),
                    onCheckedChange = {
                        if (!it) viewModel.deleteSavedPackage(item)
                    },
                    lastItem = savedPackagesList.size - 1 == index,
                    modifier = Modifier
                        .animateItemPlacement()
                        .clickable {
                            onPackageClick(item)
                        }
                )
            }
        }
    }
}

@Composable
fun SavedPackagesLazyItem(
    modifier: Modifier = Modifier,
    packageName: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    lastItem: Boolean,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
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
                Text(text = packageName, fontSize = 15.sp)
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
