package ua.polodarb.gmsflags.ui.screens.appsScreen

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class AppInfo {
    lateinit var applicationInfo: ApplicationInfo
    lateinit var packagePath: String
    lateinit var appName: String
    var packageNameCounts: Int = 0
    lateinit var icon: Drawable

    companion object {
        fun create(
            pm: PackageManager,
            applicationInfo: ApplicationInfo,
            gmsPackages: List<String>
        ): AppInfo {
            val appInfo = AppInfo()
            appInfo.applicationInfo = applicationInfo
            appInfo.appName = applicationInfo.loadLabel(pm) as String
            appInfo.packagePath = applicationInfo.packageName
            appInfo.icon = pm.getApplicationIcon(applicationInfo.packageName)

            val packageName = applicationInfo.packageName
            val matchingGmsPackages = gmsPackages.filter { it.contains(packageName) }
                .filterNot { if (packageName == "com.google.android.gm") it.contains("gms") else false }
            appInfo.packageNameCounts = matchingGmsPackages.size

            return appInfo
        }
    }
}
