package ua.polodarb.gmsflags.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ua.polodarb.gmsflags.IRootDatabase
import ua.polodarb.gmsflags.ui.navigation.BottomBarNavigation
import ua.polodarb.gmsflags.ui.navigation.BottomBarUI

@Composable
fun RootScreen(
    rootDatabase: IRootDatabase,
    isFirstStart: Boolean,
    parentNavController: NavController,
    childNavController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomBarUI(navController = childNavController) }
    ) { paddingValues ->
        BottomBarNavigation(
            rootDatabase = rootDatabase,
            isFirstStart = isFirstStart,
            parentNavController = parentNavController,
            navController = childNavController,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        )
    }
}