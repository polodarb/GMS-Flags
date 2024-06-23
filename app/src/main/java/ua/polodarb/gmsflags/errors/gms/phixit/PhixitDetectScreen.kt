package ua.polodarb.gmsflags.errors.gms.phixit

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ua.polodarb.flagsfile.LoadFileScreenViewModel
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.errors.gms.sharedUI.HeaderDescription
import ua.polodarb.gmsflags.errors.gms.sharedUI.HeaderIcon
import ua.polodarb.gmsflags.ui.MainActivity
import ua.polodarb.gmsflags.ui.theme.GMSFlagsTheme
import ua.polodarb.suggestions.SuggestedFlagItemArrow

@Composable
fun PhixitDetectScreen() {

    val viewModel = koinViewModel<PhixitDetectViewModel>()

    var isExpandedAction1 by rememberSaveable { mutableStateOf(false) }
    var isExpandedAction2 by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomBar(
                onExitClick = {
                    (context as MainActivity).finish()
                },
                isVisible = !isExpandedAction1 && !isExpandedAction2
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
                text = stringResource(R.string.phixit_title),
                descriptions = arrayOf(
                    stringResource(R.string.phixit_description)
                )
            )
            ActionsQAContainer(
                title = stringResource(R.string.phixit_fix_title),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                    bottomEnd = 8.dp,
                    bottomStart = 8.dp
                ),
                expanded = isExpandedAction1,
                onExpandedChange = {
                    isExpandedAction1 = !isExpandedAction1
                },
                modifier = Modifier
                    .padding(top = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.phixit_fix_desc_1),
                    fontSize = 16.sp
                )
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.phixit_fix_desc_2).trimIndent()
                    )
                }
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.phixit_fix_desc_3)
                    )
                }
                Button(
                    onClick = {
                        viewModel.setOpenGmsSettingsBtnClicked(true)
                        WorkManager.getInstance(context).apply {
                            enqueueUniquePeriodicWork(
                                PhixitDetectWorker.TAG,
                                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                                PhixitDetectWorker.initWorker(),
                            )
                        }
                        val intent = Intent().apply {
                            setClassName("com.google.android.gms", "co.g.Space")
                        }
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Text(stringResource(R.string.phixit_button_open_settings))
                }
            }
            ActionsQAContainer(
                title = stringResource(R.string.phixit_plan_title),
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomEnd = 24.dp,
                    bottomStart = 24.dp
                ),
                modifier = Modifier.padding(top = 8.dp),
                expanded = isExpandedAction2,
                onExpandedChange = {
                    isExpandedAction2 = !isExpandedAction2
                }
            ) {
                Text(
                    text = stringResource(R.string.phixit_plan_desc),
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ActionsQAContainer(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandedChange: () -> Unit,
    title: String,
    shape: RoundedCornerShape,
    content: @Composable ColumnScope.() -> Unit
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .clip(shape = shape)
            .background(
                if (!isSystemInDarkTheme())
                    MaterialTheme.colorScheme.surfaceContainerLow
                else
                    MaterialTheme.colorScheme.surfaceContainer
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 20.dp)

            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuggestedFlagItemArrow(
                        badge = false,
                        expanded = expanded,
                        onExpandedChange = onExpandedChange
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                ) {
                    content()
                }
            }
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