package ua.polodarb.settings.screens.startRoute

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.koinInject
import ua.polodarb.preferences.sharedPrefs.PreferenceConstants
import ua.polodarb.preferences.sharedPrefs.PreferencesManager
import ua.polodarb.settings.R
import ua.polodarb.settings.components.HeaderWithIcon

private data class NavBarItems(
    val title: String,
    val screenRoute: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangeNavigationScreen(
    screenRoute: String,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    val sharedPrefs = koinInject<PreferencesManager>()

    val preferencesManager = remember { sharedPrefs }
    var startScreen by remember {
        mutableStateOf(
            preferencesManager.getData(
                PreferenceConstants.START_SCREEN_KEY,
                screenRoute
            )
        )
    }

    val navBarItems = persistentListOf(
        NavBarItems(
            title = "Suggestions",
            screenRoute = "suggestions"
        ),
        NavBarItems(
            title = "Search",
            screenRoute = "search"
        ),
        NavBarItems(
            title = "Saved",
            screenRoute = "saved"
        ),
        NavBarItems(
            title = "Updates",
            screenRoute = "updates"
        )
    ) // TODO: make it dynamic

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.settings_start_route_title)) },
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
                .fillMaxSize()
        ) {
            HeaderWithIcon(
                icon = R.drawable.ic_home,
                description = R.string.settings_start_route_description
            )
            Spacer(modifier = Modifier.height(24.dp))
            Column(Modifier.selectableGroup()) {
                navBarItems.forEach { item ->
                    val selected = startScreen == item.screenRoute
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .selectable(
                                selected = selected,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    preferencesManager.saveData(
                                        key = PreferenceConstants.START_SCREEN_KEY,
                                        value = item.screenRoute
                                    )
                                    startScreen = item.screenRoute
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selected,
                            onClick = null
                        )
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }
        }
    }
}
