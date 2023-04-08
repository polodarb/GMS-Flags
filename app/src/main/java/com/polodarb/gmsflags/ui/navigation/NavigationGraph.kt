package com.polodarb.gmsflags.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.polodarb.gmsflags.ui.screens.appsScreen.AppsScreen
import com.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreen
import com.polodarb.gmsflags.ui.screens.historyScreen.HistoryScreen
import com.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreen
import com.polodarb.gmsflags.ui.screens.savedScreen.SavedScreen

//@Composable
//fun NavigationGraph(navController: NavHostController) {
//    NavHost(navController, startDestination = ScreenItem.Main.screenRoute) {
//        composable(ScreenItem.FlagChange.screenRoute) {
//            FlagChangeScreen()
//        }
//
//        navigation(NavBarItem.Packages.screenRoute, route = "main") {
//
//            composable(NavBarItem.Packages.screenRoute) {
//                PackagesScreen()
//            }
//            composable(NavBarItem.Apps.screenRoute) {
//                AppsScreen()
//            }
//            composable(NavBarItem.Saved.screenRoute) {
//                SavedScreen()
//            }
//            composable(NavBarItem.History.screenRoute) {
//                HistoryScreen()
//            }
//        }
//
//    }
//}

