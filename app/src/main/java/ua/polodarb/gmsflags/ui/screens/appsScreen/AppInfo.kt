package ua.polodarb.gmsflags.ui.screens.appsScreen

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class AppInfo {
    lateinit var applicationInfo: ApplicationInfo
    lateinit var appName: String
    lateinit var icon: Drawable

    companion object {
        fun create(
            pm: PackageManager,
            applicationInfo: ApplicationInfo
        ): AppInfo {
            val appInfo = AppInfo()
            appInfo.applicationInfo = applicationInfo
            appInfo.appName = applicationInfo.loadLabel(pm) as String
            appInfo.icon = pm.getApplicationIcon(applicationInfo.packageName)

            return appInfo
        }
    }
}
