package ua.polodarb.settings.screens.xposedStatus

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.settings.R
import ua.polodarb.settings.XposedTargetState
import ua.polodarb.settings.XposedTargetStatus
import ua.polodarb.xposed.info.HookInfo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun XposedStatusScreen(
    onBackPressed: () -> Unit,
    viewModel: XposedStatusViewModel = koinViewModel()
) {
    val targets by viewModel.targets.collectAsState()
    val fixState by viewModel.fixState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.xposed_status_title),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.xposed_status_back)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = stringResource(R.string.xposed_status_refresh)
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            targets.forEach { target ->
                item(key = target.packageName) {
                    HookStatusCard(target)
                }
            }

            item(key = "wallet_fix") {
                WalletFixCard(
                    fixState = fixState,
                    onFix = { viewModel.fixWalletAttestation() },
                )
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun HookStatusCard(target: XposedTargetStatus) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = target.label,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                StateBadge(target.state)
            }

            target.version?.let { version ->
                Spacer(Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.xposed_status_phenotype_version, version),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val hookInfo = target.hookInfo
            if (hookInfo != null) {
                Spacer(Modifier.height(12.dp))
                if (target.state == XposedTargetState.PHIXIT_STOPPED) {
                    Text(
                        text = stringResource(R.string.xposed_status_last_known_stats),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.height(4.dp))
                }
                StatsGrid(hookInfo)
            }
        }
    }
}

@Composable
private fun StateBadge(state: XposedTargetState) {
    val (text, containerColor, contentColor) = when (state) {
        XposedTargetState.PHIXIT_RUNNING -> Triple(
            stringResource(R.string.xposed_status_badge_running),
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
        )
        XposedTargetState.PHIXIT_STOPPED -> Triple(
            stringResource(R.string.xposed_status_badge_stopped),
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
        )
        XposedTargetState.PHIXIT_NOT_RUNNING -> Triple(
            stringResource(R.string.xposed_status_badge_not_running),
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
        )
        XposedTargetState.LEGACY_SCHEMA -> Triple(
            stringResource(R.string.xposed_status_badge_legacy),
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
        )
        XposedTargetState.UNKNOWN -> Triple(
            stringResource(R.string.xposed_status_badge_unknown),
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun StatsGrid(hookInfo: HookInfo) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        StatRow(stringResource(R.string.xposed_status_stat_pid), hookInfo.pid.toString())
        StatRow(stringResource(R.string.xposed_status_stat_started), formatTimestamp(hookInfo.startedAt))
        StatRow(stringResource(R.string.xposed_status_stat_connections), hookInfo.connections.toString())
        StatRow(stringResource(R.string.xposed_status_stat_trigger_calls), hookInfo.triggerCalls.toString())
        StatRow(stringResource(R.string.xposed_status_stat_trigger_merges), hookInfo.triggerMerges.toString())
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun WalletFixCard(
    fixState: FixState,
    onFix: () -> Unit,
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .animateContentSize()
        ) {
            Text(
                text = stringResource(R.string.xposed_status_wallet_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.xposed_status_wallet_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(Modifier.height(16.dp))

            val isLoading = fixState is FixState.Loading
            Button(
                onClick = { showConfirmDialog = true },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    stringResource(
                        if (isLoading) R.string.xposed_status_wallet_button_fixing
                        else R.string.xposed_status_wallet_button_fix
                    )
                )
            }

            when (fixState) {
                is FixState.Success -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.xposed_status_wallet_success),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                is FixState.Error -> {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = fixState.message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
                else -> {}
            }
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text(stringResource(R.string.xposed_status_wallet_dialog_title)) },
            text = { Text(stringResource(R.string.xposed_status_wallet_dialog_text)) },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmDialog = false
                        onFix()
                    }
                ) {
                    Text(stringResource(R.string.xposed_status_wallet_dialog_confirm))
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) {
                    Text(stringResource(R.string.xposed_status_wallet_dialog_dismiss))
                }
            },
        )
    }
}

private fun formatTimestamp(epochSeconds: Long): String {
    if (epochSeconds == 0L) return "N/A"
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        sdf.timeZone = TimeZone.getDefault()
        sdf.format(Date(epochSeconds * 1000))
    } catch (_: Exception) {
        epochSeconds.toString()
    }
}
