package ua.polodarb.gmsflags.errors.gms.missingDB

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.errors.gms.sharedUI.HeaderDescription
import ua.polodarb.gmsflags.errors.gms.sharedUI.HeaderIcon
import ua.polodarb.gmsflags.ui.MainActivity

@Composable
fun MissingDbScreen() {

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomBar(
                onExitClick = {
                    (context as MainActivity).finish()
                },
                isVisible = true
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
                icon = R.drawable.ic_dissatisfied
            )
            HeaderDescription(
                text = stringResource(R.string.error_missing_db_title),
                descriptions = arrayOf(
                    stringResource(R.string.error_missing_db_desc_1),
                    stringResource(R.string.error_missing_db_desc_2)
                )
            )
        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    onExitClick: () -> Unit,
    isVisible: Boolean,
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp)
        ) {
            OutlinedButton(
                onClick = onExitClick,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.phixit_button_title))
            }
        }
    }
}