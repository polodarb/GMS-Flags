package ua.polodarb.gmsflags.ui.screens.settingsScreen.changeNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.polodarb.gmsflags.R
import ua.polodarb.gmsflags.data.prefs.shared.PreferencesManager
import ua.polodarb.gmsflags.ui.navigation.NavBarItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNavigationScreen(
    onBackPressed: () -> Unit
) {

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    val preferencesManager = remember { PreferencesManager(context) }
    val data = remember {
        mutableStateOf(
            preferencesManager.getData(
                "settings_navigation",
                NavBarItem.Suggestions.screenRoute
            )
        )
    }

    val radioOptions = listOf("Suggestions screen", "Apps screen", "Saved screen")
    val selectedOption = remember {
        mutableStateOf(
            when (data.value) {
                NavBarItem.Suggestions.screenRoute -> radioOptions[0]
                NavBarItem.Apps.screenRoute -> radioOptions[1]
                NavBarItem.Saved.screenRoute -> radioOptions[2]
                else -> radioOptions[0]
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Change the start screen") },
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
                .fillMaxSize()
        ) {
            SettingsNavigationFlagsHeader()
            Spacer(modifier = Modifier.height(24.dp))
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->

                    val selectData = when (text) {
                        "Suggestions screen" -> NavBarItem.Suggestions.screenRoute
                        "Apps screen" -> NavBarItem.Apps.screenRoute
                        "Saved screen" -> NavBarItem.Saved.screenRoute
                        else -> NavBarItem.Suggestions.screenRoute
                    }

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .selectable(
                                selected = (text == selectedOption.value),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    preferencesManager.saveData("settings_navigation", selectData)
                                    data.value = selectData
                                    selectedOption.value = text // Обновляем выбранную опцию
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption.value),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SettingsNavigationFlagsHeader() {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_home),
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
            modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
        )
        Text(
            text = "Select the screen that will be displayed when the application is launched. Default screen - Suggestions.",
            style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}