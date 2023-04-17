package com.polodarb.gmsflags.ui.screens

import BottomBarNavigation
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.polodarb.gmsflags.ui.navigation.BottomBarUI

@Composable
fun RootScreen(
    parentNavController: NavController,
    childNavController: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = { BottomBarUI(navController = childNavController) }
    ) { paddingValues ->
        BottomBarNavigation(
            parentNavController = parentNavController,
            navController = childNavController,
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        )
    }
}