package ua.polodarb.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackPressed: () -> Unit,
    onResetFlagsClick: () -> Unit,
    onResetSavedClick: () -> Unit,
    onChangeNavigationClick: () -> Unit,
    onAboutClick: () -> Unit,
    onXposedStatusClick: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val xposedTargetStatuses by viewModel.xposedTargetStatuses.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                LargeTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.settings_title),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.displaySmall
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackPressed) {
                            Icon(
                                imageVector = Icons.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    },
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column {
                SettingsItem(
                    icon = R.drawable.ic_reset_flags,
                    headline = R.string.settings_item_reset_flags_headline,
                    description = R.string.settings_item_reset_flags_description,
                    onResetFlagsClick
                )
                SettingsItem(
                    R.drawable.ic_reset_saved,
                    headline = R.string.settings_item_reset_saved_headline,
                    description = R.string.settings_item_reset_saved_description,
                    onResetSavedClick
                )
                SettingsItem(
                    R.drawable.ic_home,
                    headline = R.string.settings_item_start_route_headline,
                    description = R.string.settings_item_start_route_description,
                    onChangeNavigationClick
                )
                SettingsItem(
                    R.drawable.ic_info,
                    headline = R.string.settings_item_about_headline,
                    description = R.string.settings_item_about_description,
                    onAboutClick
                )
                SettingsItem(
                    icon = R.drawable.ic_phishing,
                    headline = R.string.settings_item_xposed_headline,
                    onItemClick = onXposedStatusClick,
                ) {
                    Spacer(modifier = Modifier.height(2.dp))
                    xposedTargetStatuses.forEach { status ->
                        Text(
                            text = "${status.label}: ${stringResource(id = status.state.stringRes())}" +
                                    status.version?.let { " ($it)" }.orEmpty(),
                            color = status.state.color(),
                            fontSize = 15.sp,
                            lineHeight = 17.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@StringRes
private fun XposedTargetState.stringRes(): Int = when (this) {
    XposedTargetState.UNKNOWN -> R.string.settings_item_xposed_unknown
    XposedTargetState.LEGACY_SCHEMA -> R.string.settings_item_xposed_legacy_schema
    XposedTargetState.PHIXIT_RUNNING -> R.string.settings_item_xposed_phixit_running
    XposedTargetState.PHIXIT_STOPPED -> R.string.settings_item_xposed_phixit_stopped
    XposedTargetState.PHIXIT_NOT_RUNNING -> R.string.settings_item_xposed_phixit_not_running
}

@Composable
private fun XposedTargetState.color() = when (this) {
    XposedTargetState.PHIXIT_RUNNING -> MaterialTheme.colorScheme.primary
    XposedTargetState.PHIXIT_STOPPED -> MaterialTheme.colorScheme.tertiary
    XposedTargetState.LEGACY_SCHEMA -> MaterialTheme.colorScheme.outline
    else -> MaterialTheme.colorScheme.error
}

@Composable
fun SettingsItem(
    @DrawableRes icon: Int,
    @StringRes headline: Int,
    @StringRes description: Int,
    onItemClick: () -> Unit
) {
    SettingsItem(
        icon = icon,
        headline = headline,
        onItemClick = onItemClick
    ) {
        Text(
            text = stringResource(id = description),
            color = MaterialTheme.colorScheme.outline,
            fontSize = 15.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SettingsItem(
    @DrawableRes icon: Int,
    @StringRes headline: Int,
    onItemClick: (() -> Unit)? = null,
    descriptionContent: @Composable ColumnScope.() -> Unit
) {
    val itemModifier = if (onItemClick == null) {
        Modifier.fillMaxWidth()
    } else {
        Modifier
            .fillMaxWidth()
            .clickable { onItemClick() }
    }

    Row(
        modifier = itemModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 28.dp)
                .size(26.dp)
        )
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(id = headline),
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            descriptionContent()
        }
    }
}
