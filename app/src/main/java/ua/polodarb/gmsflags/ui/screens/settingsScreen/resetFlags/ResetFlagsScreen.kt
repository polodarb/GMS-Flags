package ua.polodarb.gmsflags.ui.screens.settingsScreen.resetFlags

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.screens.settingsScreen.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetFlagsScreen(
    onBackPressed: () -> Unit
) {

    val viewModel = koinViewModel<SettingsViewModel>()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var resetFlagsType by rememberSaveable {
        mutableStateOf(SettingsResetFlagsType.NONE)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Reset flags") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            SettingsResetFlagsHeader()
            Spacer(modifier = Modifier.height(16.dp))
            SettingsResetFlagsButtons(
                onGmsClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    resetFlagsType = SettingsResetFlagsType.GMS
                    showDialog = true
                },
                onPlayStoreClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    resetFlagsType = SettingsResetFlagsType.PLAYSTORE
                    showDialog = true
                },
                onResetAllClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    resetFlagsType = SettingsResetFlagsType.ALL
                    showDialog = true
                }
            )
        }
        ConfirmResetFlagsDialog(showDialog = showDialog, onDismiss = { showDialog = false }) {
            showDialog = false
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

            when (resetFlagsType) {
                SettingsResetFlagsType.GMS -> viewModel.deleteAllOverriddenFlagsFromGMS()
                SettingsResetFlagsType.PLAYSTORE -> viewModel.deleteAllOverriddenFlagsFromPlayStore()
                SettingsResetFlagsType.ALL -> viewModel.deleteAllOverriddenFlags()
                SettingsResetFlagsType.NONE -> {}
            }

            Toast.makeText(context, "Done!", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun SettingsResetFlagsHeader() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_reset_flags),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .padding(36.dp)
                    .size(112.dp)
            )
        }
        Image(
            imageVector = Icons.Outlined.Info,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant),
            modifier = Modifier.padding(start = 24.dp, bottom = 12.dp, top = 24.dp)
        )
        Text(
            text = "Restore default settings and clears any user-customized (overridden) flags or preferences within the application, providing a clean slate for configuration.",
            style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsResetFlagsButtons(
    onGmsClick: () -> Unit,
    onPlayStoreClick: () -> Unit,
    onResetAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        OutlinedButton(
            onClick = onGmsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset flags in GMS Database")
        }
        OutlinedButton(
            onClick = onPlayStoreClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(text = "Reset flags in Play Store Database")
        }
        Button(
            onClick = onResetAllClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(text = "Reset all overridden flags")
        }
    }
}

@Composable
fun ConfirmResetFlagsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirmClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_force_stop),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurface)
                )
            },
            title = {
                Text(text = "Confirm action")
            },
            text = {
                Text(
                    text = "All flags that you changed will be reset!",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = onDismiss
                    ) {
                        Text("Close")
                    }
                    Button(
                        onClick = onConfirmClick
                    ) {
                        Text("Confirm")
                    }
                }
            },
        )
    }
}

enum class SettingsResetFlagsType {
    NONE, GMS, PLAYSTORE, ALL
}