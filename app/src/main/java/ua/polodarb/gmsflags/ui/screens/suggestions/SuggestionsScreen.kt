package ua.polodarb.gmsflags.ui.screens.suggestions

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import coil.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import ua.polodarb.gmsflags.BuildConfig
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.data.remote.flags.dto.FlagInfo
import ua.polodarb.gmsflags.ui.OSUtils
import ua.polodarb.gmsflags.ui.components.inserts.ErrorLoadScreen
import ua.polodarb.gmsflags.ui.components.inserts.LoadingProgressBar
import ua.polodarb.repository.uiStates.UiStates
import ua.polodarb.gmsflags.ui.screens.flagChange.dialogs.ReportFlagsDialog
import ua.polodarb.gmsflags.ui.screens.suggestions.dialog.ResetFlagToDefaultDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuggestionsScreen(
    isFirstStart: Boolean,
    onSettingsClick: () -> Unit
) {
    val viewModel = koinViewModel<SuggestionScreenViewModel>()

    val networkFlags = viewModel.stateSuggestionsFlags.collectAsState()

    Log.e("TAG", "SuggestionsScreen: ${networkFlags.value}")

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val packageManager = context.packageManager

    LaunchedEffect(Unit) {
        viewModel.getSuggestedFlags()
    }

    // Report dialog
    var showReportDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var reportFlagDesc by rememberSaveable {
        mutableStateOf("")
    }
    var reportFlagName by rememberSaveable {
        mutableStateOf("")
    }

    // Reset dialog
    var showResetDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val resetFlagsList: MutableList<FlagInfo> = mutableListOf()
    var resetFlagPackage = ""

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.nav_bar_suggestions),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                actions = {
//                    IconButton(onClick = {
//                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
//                        Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
//                    }) {
//                        Icon(
//                            imageVector = Icons.Filled.Search,
//                            contentDescription = "Localized description"
//                        )
//                    }
                    IconButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onSettingsClick()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = it.calculateTopPadding())
        ) {
            when (val result = networkFlags.value) {
                is ua.polodarb.repository.uiStates.UiStates.Success -> {
                    val data = result.data
                    Log.e("compose", "data: $data")
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        item {
                            WarningBanner(isFirstStart)
                        }
                        itemsIndexed(data.primary) { index, item ->
                            SuggestedFlagItem(
                                flagTitle = item.flag.name,
                                noteText = item.flag.note,
                                source = item.flag.source ?: null,
                                appInfoPackageName = item.flag.appPackage,
                                flagValue = item.enabled,
                                flagDetails = item.flag.details,
                                listStart = index == 0,
                                listEnd = index == data.primary.size - 1,
                                flagOnCheckedChange = { bool ->
                                    viewModel.overrideSuggestedFlags(
                                        flags = item.flag.flags,
                                        packageName = item.flag.flagPackage,
                                        newBoolValue = bool,
                                        index = index,
                                        flagType = SuggestedUIFlagTypes.PRIMARY
                                    )
                                },
                                onOpenAppClick = {
                                    val intent =
                                        packageManager.getLaunchIntentForPackage(item.flag.appPackage)
                                    if (intent != null) {
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Couldn't open app",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onOpenSettingsClick = {
                                    val intent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", item.flag.appPackage, null)
                                    )
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(context, intent, null)
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onViewDetailsClick = {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(item.flag.details)
                                    )
                                    startActivity(context, intent, null)
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onReportClick = {
                                    showReportDialog = true
                                    reportFlagName = item.flag.name
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onResetClick = {
                                    resetFlagPackage = item.flag.flagPackage
                                    resetFlagsList.addAll(item.flag.flags)
                                    showResetDialog = true
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                packageManager = packageManager
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        itemsIndexed(data.secondary) { index, item ->
                            SuggestedFlagItem(
                                flagTitle = item.flag.name,
                                noteText = item.flag.note,
                                source = item.flag.source,
                                appInfoPackageName = item.flag.appPackage,
                                flagValue = item.enabled,
                                flagDetails = item.flag.details,
                                listStart = index == 0,
                                listEnd = index == data.secondary.size - 1,
                                flagOnCheckedChange = { bool ->
                                    viewModel.overrideSuggestedFlags(
                                        flags = item.flag.flags,
                                        packageName = item.flag.flagPackage,
                                        newBoolValue = bool,
                                        index = index,
                                        flagType = SuggestedUIFlagTypes.SECONDARY
                                    )
                                },
                                onOpenAppClick = {
                                    val intent =
                                        packageManager.getLaunchIntentForPackage(item.flag.appPackage)
                                    if (intent != null) {
                                        context.startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Couldn't open app",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onOpenSettingsClick = {
                                    val intent = Intent(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", item.flag.appPackage, null)
                                    )
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(context, intent, null)
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onViewDetailsClick = {
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(item.flag.details)
                                    )
                                    startActivity(context, intent, null)
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onReportClick = {
                                    showReportDialog = true
                                    reportFlagName = item.flag.name
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                onResetClick = {
                                    resetFlagPackage = item.flag.flagPackage
                                    resetFlagsList.addAll(item.flag.flags)
                                    showResetDialog = true
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                },
                                packageManager = packageManager
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.padding(44.dp))
                        }
                    }
                    ResetFlagToDefaultDialog(
                        showDialog = showResetDialog,
                        onDismiss = { showResetDialog = false }
                    ) {
                        showResetDialog = false
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        viewModel.resetSuggestedFlagValue(resetFlagPackage, resetFlagsList)
                        viewModel.getSuggestedFlags()
                    }
                    ReportFlagsDialog(
                        showDialog = showReportDialog,
                        flagDesc = reportFlagDesc,
                        onFlagDescChange = { newValue ->
                            reportFlagDesc = newValue
                        },
                        onSend = {
                            showReportDialog = false
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                this.data = Uri.parse("mailto:")
                                putExtra(Intent.EXTRA_EMAIL, arrayOf("gmsflags@gmail.com"))
                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    "Report on suggested flag"
                                )
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Model: ${Build.DEVICE} (${Build.BOARD})\n" +
                                            "Manufacturer: ${Build.MANUFACTURER}\n" +
                                            "Android: ${Build.VERSION.RELEASE}\n" +
                                            "Manufacturer OS: ${OSUtils.sName} (${OSUtils.sVersion})\n" +
                                            "GMS flag: ${BuildConfig.VERSION_CODE} (${BuildConfig.VERSION_NAME})\n\n" +

                                            "Flag name: $reportFlagName\n\n" +
                                            "Description: $reportFlagDesc"
                                )
                            }
                            startActivity(context, intent, null)
                            reportFlagDesc = ""
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        },
                        onDismiss = {
                            showReportDialog = false
                            reportFlagDesc = ""
                        }
                    )
                }

                is ua.polodarb.repository.uiStates.UiStates.Loading -> {
                    LoadingProgressBar()
                }

                is ua.polodarb.repository.uiStates.UiStates.Error -> {
                    ErrorLoadScreen()
                }
            }
        }
    }
}

@Composable
fun SuggestedFlagItem(
    flagTitle: String,
    flagValue: Boolean,
    noteText: String?,
    source: String?,
    appInfoPackageName: String,
    flagDetails: String?,
    listStart: Boolean,
    listEnd: Boolean,
    flagOnCheckedChange: (Boolean) -> Unit,
    onOpenSettingsClick: () -> Unit,
    onOpenAppClick: () -> Unit,
    onViewDetailsClick: () -> Unit,
    onResetClick: () -> Unit,
    onReportClick: () -> Unit,
    packageManager: PackageManager,
) {

    val appInfoName by remember {
        mutableStateOf(
            packageManager.getApplicationInfo(appInfoPackageName, 0).loadLabel(packageManager)
                .toString()
        )
    }
    val appIcon by remember {
        mutableStateOf(packageManager.getApplicationIcon(appInfoPackageName))
    }

    NewSuggestedFlagItem(
        titleText = flagTitle,
        switchValue = flagValue,
        onSwitchChanged = flagOnCheckedChange,
        noteText = noteText,
        sourceText = source,
        appInfoIcon = appIcon,
        appInfoName = appInfoName,
        appInfoPackage = appInfoPackageName,
        onOpenSettingsClick = onOpenSettingsClick,
        onOpenAppClick = onOpenAppClick,
        flagDetails = flagDetails,
        listStart = listStart,
        listEnd = listEnd,
        onViewDetailsClick = onViewDetailsClick,
        onResetClick = onResetClick,
        onReportClick = onReportClick
    )
}

@Composable
private fun NewSuggestedFlagItem(
    titleText: String,
    switchValue: Boolean,
    onSwitchChanged: (Boolean) -> Unit,
    noteText: String?,
    sourceText: String?,
    appInfoIcon: Drawable,
    appInfoName: String,
    appInfoPackage: String,
    flagDetails: String?,
    listStart: Boolean,
    listEnd: Boolean,
    onOpenSettingsClick: () -> Unit,
    onOpenAppClick: () -> Unit,
    onViewDetailsClick: () -> Unit,
    onResetClick: () -> Unit,
    onReportClick: () -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 6.dp)
            .clip(
                if (listStart && !listEnd) {
                    RoundedCornerShape(
                        topStart = 24.dp,
                        topEnd = 24.dp,
                        bottomStart = 6.dp,
                        bottomEnd = 6.dp
                    )
                } else if (listEnd && !listStart) {
                    RoundedCornerShape(
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp,
                        topStart = 6.dp,
                        topEnd = 6.dp
                    )
                } else if (listEnd) {
                    RoundedCornerShape(24.dp)
                } else {
                    RoundedCornerShape(6.dp)
                }
            )
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = titleText,
                    fontSize = 17.sp,
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp, top = 20.dp, bottom = 20.dp)
                        .weight(1f)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SuggestedFlagItemArrow(
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = !expanded
                        }
                    )
                    Switch(
                        checked = switchValue,
                        onCheckedChange = onSwitchChanged,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            AnimatedVisibility(visible = expanded) {
                Column {
                    if (noteText != null) {
                        Text(
                            text = noteText,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.tertiaryContainer)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    if (sourceText != null) {
                        Text(
                            text = "" + stringResource(R.string.finder) + " " + sourceText,
                            fontSize = 13.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomEnd = 4.dp,
                                        bottomStart = 4.dp
                                    )
                                )
                                .background(
                                    if (!isSystemInDarkTheme())
                                        MaterialTheme.colorScheme.surfaceContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceContainerHigh
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp, top = 6.dp)
                            .fillMaxWidth()
                            .clip(
                                if (sourceText != null) {
                                    RoundedCornerShape(
                                        topStart = 4.dp,
                                        topEnd = 4.dp,
                                        bottomEnd = 16.dp,
                                        bottomStart = 16.dp
                                    )
                                } else {
                                    RoundedCornerShape(16.dp)
                                }
                            )
                            .background(
                                if (!isSystemInDarkTheme())
                                    MaterialTheme.colorScheme.surfaceContainer
                                else
                                    MaterialTheme.colorScheme.surfaceContainerHigh
                            )
                    ) {
                        AppContent(
                            appName = appInfoName,
                            pkg = appInfoPackage,
                            appIcon = appInfoIcon
                        )
                        OutlinedButton(
                            onClick = onOpenAppClick,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(text = stringResource(R.string.open, appInfoName))
                        }
                        OutlinedButton(
                            onClick = onOpenSettingsClick,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                                .fillMaxWidth(),
                        ) {
                            Text(text = stringResource(id = R.string.open_app_details_settings))
                        }
                    }
                    if (flagDetails != null) {
                        Button(
                            onClick = onViewDetailsClick,
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                                .fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            ),
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_telegram),
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = stringResource(R.string.view_more_information)
                            )
                        }
                    }
                    Row(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = if (flagDetails != null) 8.dp else 16.dp,
                            bottom = 16.dp
                        ),
                    ) {
                        Button(
                            onClick = onResetClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ),
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_flag_reset_new),
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = stringResource(R.string.reset)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = onReportClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                            modifier = Modifier.weight(0.5f)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_report_fill),
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                text = stringResource(R.string.report)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Stable
@Composable
fun AppContent(
    appName: String,
    pkg: String,
    appIcon: Drawable
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = if (!isSystemInDarkTheme())
                MaterialTheme.colorScheme.surfaceContainer
            else
                MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        headlineContent = { Text(text = appName, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(text = pkg, fontSize = 13.sp) },
        leadingContent = {
            AsyncImage(
                model = appIcon,
                contentDescription = null,
                modifier = Modifier.size(52.dp)
            )
        }
    )
}

@Composable
fun SuggestedFlagItemArrow(
    expanded: Boolean,
    onExpandedChange: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    val rotationState by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = LinearEasing),
        label = "Rotation"
    )

    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Rounded.KeyboardArrowDown,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer(
                    rotationX = rotationState
                )
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    onExpandedChange()
                }
        )
    }
}


@Composable
fun WarningBanner(
    isFirstStart: Boolean
) {
    var visibility by rememberSaveable {
        mutableStateOf(isFirstStart)
    }

    AnimatedVisibility(
        visible = visibility,
        enter = fadeIn(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 0.dp, 16.dp, 16.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_force_stop),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .padding(8.dp)
                        .size(28.dp)
                )
                Text(
                    text = stringResource(R.string.suggestion_banner),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 4.dp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = { visibility = false },
                        modifier = Modifier.padding(8.dp),
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError,
                            disabledContainerColor = MaterialTheme.colorScheme.outline,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    ) {
                        Text(text = stringResource(R.string.close))
                    }
                }
            }
        }
    }
}
