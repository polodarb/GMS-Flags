package ua.polodarb.gmsflags.ui.screens.settingsScreen.about

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.BuildConfig
import ua.polodarb.gmsflags.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBackPressed: () -> Unit
) {

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "About & Support") },
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
        Box(modifier = Modifier.padding(top = it.calculateTopPadding())) {
            AboutSettingsContent(
                onDeveloperClick = {
                    uriHandler.openUri("https://github.com/polodarb")
                },
                onGitHubClick = {
                    uriHandler.openUri("https://github.com/polodarb/GMS-Flags")
                },
                onTelegramClick = {
                    uriHandler.openUri("https://t.me/gmsflags")
                }
            )
        }
    }
}

@Composable
fun AboutSettingsContent(
    onDeveloperClick: () -> Unit,
    onGitHubClick: () -> Unit,
    onTelegramClick: () -> Unit
) {

    val haptic = LocalHapticFeedback.current

    Column {
        Header(haptic)
        SettingsAboutListInfo(onDeveloperClick, onGitHubClick, onTelegramClick, haptic)
    }
}

@Composable
fun Header(
    haptic: HapticFeedback
) {

    var rotationState by remember { mutableStateOf(0f) }

    val rotationDegrees by animateFloatAsState(
        targetValue = rotationState,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    val interactionSource = remember { MutableInteractionSource() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    rotationState += 30f
                }
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.star_background),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondaryContainer),
                    modifier = Modifier
                        .size(240.dp)
                        .graphicsLayer(rotationZ = rotationDegrees)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSecondaryContainer),
                    modifier = Modifier.size(292.dp)
                )
            }
        }
        Text(
            text = "GMS Flags",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.headlineLarge
        )
        SettingsAboutVersionBadge()
    }
}


@Composable
fun SettingsAboutVersionBadge() {
    Box(
        modifier = Modifier
            .padding(top = 20.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = "v${BuildConfig.VERSION_NAME}",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun SettingsAboutListInfo(
    onDeveloperClick: () -> Unit,
    onGitHubClick: () -> Unit,
    onTelegramClick: () -> Unit,
    haptic: HapticFeedback
) {
    Column(
        modifier = Modifier.padding(top = 36.dp, start = 8.dp)
    ) {
        ListItem(
            headlineContent = {
                Text(text = "Danyil Kobzar")
            },
            leadingContent = {
                Image(imageVector = Icons.Rounded.Person, contentDescription = null)
            },
            overlineContent = {
                Text(text = "Developer")
            },
            modifier = Modifier.clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onDeveloperClick()
            }
        )
        ListItem(
            headlineContent = {
                Text(text = "Source code")
            },
            leadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.ic_github),
                    contentDescription = null
                )
            },
            overlineContent = {
                Text(text = "GitHub")
            },
            modifier = Modifier.clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onGitHubClick()
            }
        )
        ListItem(
            headlineContent = {
                Text(text = "Telegram channel")
            },
            leadingContent = {
                Image(
                    painter = painterResource(id = R.drawable.ic_telegram),
                    contentDescription = null
                )
            },
            overlineContent = {
                Text(text = "Support & updates")
            },
            modifier = Modifier.clickable {
                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onTelegramClick()
            }
        )
    }
}