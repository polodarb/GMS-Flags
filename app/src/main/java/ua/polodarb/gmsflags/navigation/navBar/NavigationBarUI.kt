package ua.polodarb.gmsflags.navigation.navBar

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import ua.polodarb.gmsflags.navigation.utils.currentScreenAsState
import ua.polodarb.gmsflags.navigation.navBarItems

@Composable
fun BottomBarUI(
    navController: NavHostController
) {
    val currentSelectedItem by navController.currentScreenAsState()

    NavigationBar {
        navBarItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(
                            if (currentSelectedItem == item || item.iconInactive == null)
                                item.iconActive
                            else
                                item.iconInactive
                        ),
                        tint = if (currentSelectedItem == item || item.iconInactive == null)
                            MaterialTheme.colorScheme.onSecondaryContainer
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        contentDescription = stringResource(id = item.title)
                    )
                },
                label = { Text(text = stringResource(id = item.title), maxLines = 1, overflow = TextOverflow.Ellipsis) },
                selected = currentSelectedItem == item,
                onClick = {
                    navController.navigate(item.screenRoute) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
