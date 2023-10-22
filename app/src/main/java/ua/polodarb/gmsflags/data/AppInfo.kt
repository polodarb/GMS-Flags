package ua.polodarb.gmsflags.data

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable

class AppInfo(
    pm: PackageManager,
    val applicationInfo: ApplicationInfo
) {
    val appName: String = applicationInfo.loadLabel(pm) as String
    val icon: Drawable = pm.getApplicationIcon(applicationInfo.packageName)
}
