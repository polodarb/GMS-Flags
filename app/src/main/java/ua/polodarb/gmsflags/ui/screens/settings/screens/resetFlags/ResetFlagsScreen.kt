package ua.polodarb.gmsflags.ui.screens.settings.screens.resetFlags

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.ui.screens.settings.SettingsViewModel
import ua.polodarb.gmsflags.ui.screens.settings.common.ConfirmationDialog
import ua.polodarb.gmsflags.ui.screens.settings.common.HeaderWithIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetFlagsScreen(
    onBackPressed: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var resetFlagsType by rememberSaveable { mutableStateOf(SettingsResetFlagsType.NONE) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_item_reset_flags_headline)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = null
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
            HeaderWithIcon(
                icon = R.drawable.ic_reset_flags,
                description = R.string.settings_reset_flags_description
            )
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
        ConfirmationDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false },
            onConfirmClick = {
                showDialog = false
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                when (resetFlagsType) {
                    SettingsResetFlagsType.GMS -> viewModel.deleteAllOverriddenFlagsFromGMS()
                    SettingsResetFlagsType.PLAYSTORE -> viewModel.deleteAllOverriddenFlagsFromPlayStore()
                    SettingsResetFlagsType.ALL -> viewModel.deleteAllOverriddenFlags()
                    SettingsResetFlagsType.NONE -> {}
                }

                Toast.makeText(context, context.getString(R.string.toast_done), Toast.LENGTH_SHORT).show()
            },
            text = R.string.settings_reset_flags_warning
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
            Text(text = stringResource(id = R.string.settings_reset_flags_opt_gms))
        }
        OutlinedButton(
            onClick = onPlayStoreClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {
            Text(text = stringResource(id = R.string.settings_reset_flags_opt_vending))
        }
        Button(
            onClick = onResetAllClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Text(text = stringResource(id = R.string.settings_reset_flags_opt_all))
        }
    }
}

enum class SettingsResetFlagsType {
    NONE, GMS, PLAYSTORE, ALL
}
