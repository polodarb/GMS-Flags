package ua.polodarb.repository.appsList

import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class AppInfo(
    pm: PackageManager,
    val applicationInfo: ApplicationInfo,
    val packageInfo: PackageInfo?
) {
    val appName: String = applicationInfo.loadLabel(pm) as String
    val icon: Drawable = pm.getApplicationIcon(applicationInfo.packageName)
}
