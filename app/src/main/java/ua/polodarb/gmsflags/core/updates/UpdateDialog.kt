package ua.polodarb.gmsflags.core.updates

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.polodarb.gmsflags.BuildConfig
import ua.polodarb.gmsflags.R
import ua.polodarb.common.Extensions.toFormattedInt
import ua.polodarb.network.Resource
import ua.polodarb.network.appUpdates.AppUpdatesApiService

@Composable
fun UpdateDialog( // TODO: Refactor and move to :core:ui
    appUpdatesApiService: AppUpdatesApiService,
    isFirstStart: Boolean
) {
    val uriHandler = LocalUriHandler.current

    var showDialog by rememberSaveable { mutableStateOf(false) }
    val release = produceState(
        initialValue = BuildConfig.VERSION_NAME,
        producer = {
            CoroutineScope(Dispatchers.IO).launch {
                val res = appUpdatesApiService.getLatestRelease()
                if (res is Resource.Success) {
                    Log.e("UpdateDialog", res.data?.tagName.toString())
                    this@produceState.value = res.data?.tagName ?: BuildConfig.VERSION_NAME
                }
            }
        }
    ).value

    var appUpdateState by remember { mutableStateOf(false) }
    appUpdateState = BuildConfig.VERSION_NAME.toFormattedInt() < release.toFormattedInt()

    if (!isFirstStart) {
        LaunchedEffect(appUpdateState) {
            if (appUpdateState) {
                showDialog = true
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Row {
                    OutlinedButton(onClick = { showDialog = false }) {
                        Text(text = stringResource(R.string.update_dialog_close))
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            uriHandler.openUri("https://github.com/polodarb/GMS-Flags/releases/latest")
                            showDialog = false
                        }) {
                        Text(text = stringResource(R.string.update_dialog_confirm))
                    }
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_update_app),
                    contentDescription = null
                )
            },
            text = {
                Text(text = stringResource(R.string.update_dialog_info))
            },
            title = {
                Text(
                    text = stringResource(R.string.update_dialog_title, release),
                    fontWeight = FontWeight.Medium
                )
            },
        )
    }
}
