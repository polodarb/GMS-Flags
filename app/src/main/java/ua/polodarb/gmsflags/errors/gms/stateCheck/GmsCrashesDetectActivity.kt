package ua.polodarb.gmsflags.errors.gms.stateCheck

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.errors.gms.sharedUI.HeaderDescription
import ua.polodarb.gmsflags.errors.gms.sharedUI.HeaderIcon
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme

class GmsCrashesDetectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDecorFitsSystemWindows(window, false)

        setContent {

            val context = LocalContext.current
            val uriHandler = LocalUriHandler.current
            val haptic = LocalHapticFeedback.current

            val gmsFlagsChatLink = "https://t.me/gmsflags_chat"

            GMSFlagsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    GmsCrashesDetectScreen(
                        onAskClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            uriHandler.openUri(gmsFlagsChatLink)
                        },
                        onFixClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            val intent = Intent().apply {
                                setClassName("com.google.android.gms", "co.g.Space")
                            }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun GmsCrashesDetectScreen(
    onFixClick: () -> Unit,
    onAskClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            GmsCrashesDetectBottomBar(
                onFixClick = onFixClick,
                onAskClick = onAskClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            HeaderIcon(
                icon = R.drawable.ic_error
            )
            HeaderDescription(
                text = stringResource(R.string.gms_error_title),
                descriptions = arrayOf(
                    stringResource(R.string.gms_error_description_1),
                    stringResource(R.string.gms_error_description_2),
                    stringResource(R.string.gms_error_description_3),
                )
            )
        }
    }
}

@Composable
fun GmsCrashesDetectBottomBar(
    onFixClick: () -> Unit,
    onAskClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onAskClick,
            modifier = Modifier.height(48.dp)
        ) {
            Text(
                text = "Ask in chat"
            )
        }
        Button(
            onClick = onFixClick,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(
                text = "Fix it"
            )
        }
    }
}