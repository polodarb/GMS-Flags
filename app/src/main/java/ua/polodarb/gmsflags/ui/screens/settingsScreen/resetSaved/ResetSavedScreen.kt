package ua.polodarb.gmsflags.ui.screens.settingsScreen.resetSaved

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
fun ResetSavedScreen(
    onBackPressed: () -> Unit
) {

    val viewModel = koinViewModel<SettingsViewModel>()
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var resetFlagsType by rememberSaveable {
        mutableStateOf(SettingsResetSavedType.NONE)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Reset saved") },
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
                onPackagesClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    resetFlagsType = SettingsResetSavedType.PACKAGES
                    showDialog = true
                },
                onFlagsClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    resetFlagsType = SettingsResetSavedType.FLAGS
                    showDialog = true
                },
                onResetAllClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    resetFlagsType = SettingsResetSavedType.ALL
                    showDialog = true
                }
            )
        }
        ConfirmResetFlagsDialog(showDialog = showDialog, onDismiss = { showDialog = false }) {
            showDialog = false
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

            when (resetFlagsType) {
                SettingsResetSavedType.PACKAGES -> viewModel.deleteAllSavedPackages()
                SettingsResetSavedType.FLAGS -> viewModel.deleteAllSavedFlags()
                SettingsResetSavedType.ALL -> viewModel.deleteAllSavedFlagsAndPackages()
                SettingsResetSavedType.NONE -> {}
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
                painter = painterResource(id = R.drawable.ic_reset_saved),
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
            text = "In this section you can reset all packages and flags on the \"Saved\" screen that you have saved before.",
            style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun SettingsResetFlagsButtons(
    onPackagesClick: () -> Unit,
    onFlagsClick: () -> Unit,
    onResetAllClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        OutlinedButton(
            onClick = onPackagesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reset saved packages")
        }
        OutlinedButton(
            onClick = onFlagsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(text = "Reset saved flags")
        }
        Button(
            onClick = onResetAllClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(text = "Reset all saved flags and packages")
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
                    text = "All flags that you saved will be reset!",
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

enum class SettingsResetSavedType {
    NONE, FLAGS, PACKAGES, ALL
}