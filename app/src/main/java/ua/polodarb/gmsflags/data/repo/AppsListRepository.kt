package ua.polodarb.gmsflags.data.repo

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.AppInfo
import ua.polodarb.repository.uiStates.UiStates

class AppsListRepository(
    private val context: Context
) {

    private val gmsApplication = context as GMSApplication

    fun getAllInstalledApps() = flow {
        emit(ua.polodarb.repository.uiStates.UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val gmsPackages = (context as GMSApplication).getRootDatabase().googlePackages
                val pm = context.packageManager

                val appInfoList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    pm.getInstalledApplications(
                        PackageManager.ApplicationInfoFlags.of(
                            PackageManager.GET_META_DATA.toLong()
                        )
                    )
                } else {
                    pm.getInstalledApplications(PackageManager.GET_META_DATA)
                }

                val filteredAppInfoList = appInfoList.asSequence()
                    .filter {
                        gmsPackages.contains(it.packageName)
                                && it.packageName.contains("com.google")
                                || it.packageName.contains("com.android.vending")
                    }
                    .map { createAppInfo(pm, it) }
                    .sortedBy { it.appName }
                    .toList()

                if (filteredAppInfoList.isNotEmpty()) {
                    emit(ua.polodarb.repository.uiStates.UiStates.Success(filteredAppInfoList))
                }
            }
        }
    }

    private fun createAppInfo(pm: PackageManager, appInfo: ApplicationInfo): AppInfo {
        val packageInfo = getPackageInfo(appInfo.packageName)
        return AppInfo(pm = pm, applicationInfo = appInfo, packageInfo = packageInfo)
    }

    private fun getPackageInfo(packageName: String): PackageInfo? {
        return try {
            context.packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    fun getAppVersionCode(packageName: String): Long {
            val packageManager: PackageManager = context.packageManager
            try {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                return packageInfo.longVersionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return -1
    }

    fun getAppLastUpdateTime(packageName: String): Long {
        val packageManager: PackageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return packageInfo.lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    fun getListByPackages(pkgName: String) = flow<ua.polodarb.repository.uiStates.UiStates<List<String>>> {
        val context = context as GMSApplication
        val list = context.getRootDatabase().getListByPackages(pkgName).filterNot {
            if (pkgName == "com.google.android.gm") {
                it.contains("gms")
            } else {
                false
            }
        }.toMutableList()

        if (pkgName == "com.android.vending") list.addAll(0,
            listOf(
                "com.google.android.finsky.instantapps",
                "com.google.android.finsky.regular",
                "com.google.android.finsky.stable"
            )
        )

        emit(ua.polodarb.repository.uiStates.UiStates.Success(list))
    }

}