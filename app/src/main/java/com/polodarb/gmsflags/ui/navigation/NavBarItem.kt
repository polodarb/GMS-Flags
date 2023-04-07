package com.polodarb.gmsflags.ui.navigation

import com.polodarb.gmsflags.R

sealed class NavBarItem(var title: String, var icon: Int, var screenRoute: String) {

    object Packages : NavBarItem("Packages", R.drawable.ic_navbar_packages, "packages")
    object Apps : NavBarItem("Apps", R.drawable.ic_navbar_apps, "Apps")
    object Saved : NavBarItem("Saved", R.drawable.ic_save_inactive, "saved")
    object History : NavBarItem("History", R.drawable.ic_navbar_history, "history")

}