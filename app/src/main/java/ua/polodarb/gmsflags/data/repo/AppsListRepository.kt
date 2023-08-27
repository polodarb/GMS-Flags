package ua.polodarb.gmsflags.data.repo

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.di.GMSApplication
import ua.polodarb.gmsflags.ui.screens.appsScreen.AppInfo
import ua.polodarb.gmsflags.ui.screens.appsScreen.AppsScreenUiStates
import ua.polodarb.gmsflags.ui.screens.appsScreen.DialogUiStates

class AppsListRepository(
    private val context: Context
) {
    fun getAllInstalledApps() = flow<AppsScreenUiStates> {

        val gmsPackages = (context as GMSApplication).getRootDatabase().gmsPackages

        val pm = context.packageManager
        val appInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(PackageManager.GET_META_DATA.toLong()))
        } else {
            pm.getInstalledApplications(PackageManager.GET_META_DATA)
        }
        val filteredAppInfoList = appInfoList
            .map { it -> AppInfo.create(pm, it, gmsPackages.map { it.key }) }
            .filterNot {
                it.applicationInfo.packageName.contains("com.google.android.youtube")
            }
            .filterNot {
                it.applicationInfo.packageName.contains("com.google.android.apps.youtube.music")
            }
            .filterNot {
                it.applicationInfo.packageName.contains("com.google.android.overlay")
            }
            .filterNot {
                it.applicationInfo.packageName.contains("com.google.android.googlequicksearchbox.nga_resources")
            }
            .filter { it.applicationInfo.packageName.contains("com.google.android") }

        val packageNameCounts = filteredAppInfoList.associate { appInfo ->
            val packageName = appInfo.applicationInfo.packageName
            val matchingGmsPackages = gmsPackages.filter { it.key.contains(packageName) }
            packageName to matchingGmsPackages.size
        }

        val list = filteredAppInfoList.filter { appInfo ->
            val packageName = appInfo.applicationInfo.packageName
            (packageNameCounts[packageName] ?: 0) > 0
        }.filterNot {
            if(it.applicationInfo.packageName == "com.google.android.gm") {
                it.applicationInfo.packageName.contains("gms")
            } else {
                false
            }
        }.sortedBy { it.appName }

        Log.e("db", "5 - ${(context as GMSApplication).isRootDatabaseInitialized}")

        emit(AppsScreenUiStates.Success(list))
    }

    fun getListByPackages(pkgName: String) = flow<DialogUiStates> {
        val context = context as GMSApplication
        val list = context.getRootDatabase().getListByPackages(pkgName).filterNot {
            if(pkgName == "com.google.android.gm") {
                it.contains("gms")
            } else {
                false
            }
        }
        emit(DialogUiStates.Success(list))
    }
}