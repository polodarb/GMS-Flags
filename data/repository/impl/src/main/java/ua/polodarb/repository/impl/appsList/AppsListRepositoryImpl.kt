package ua.polodarb.repository.impl.appsList

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.polodarb.gms.init.InitRootDB
import ua.polodarb.repository.appsList.AppInfo
import ua.polodarb.repository.appsList.AppsListRepository
import ua.polodarb.repository.uiStates.UiStates

class AppsListRepositoryImpl(
    private val context: Context,
    private val rootDB: InitRootDB
): AppsListRepository {
    
    private val rootDatabase by lazy {
        rootDB.getRootDatabase()
    }

    override fun getAllInstalledApps(): Flow<UiStates<List<AppInfo>>> = flow {
        emit(UiStates.Loading())

        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val gmsPackages = rootDatabase.googlePackages
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
                    emit(UiStates.Success(filteredAppInfoList))
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

    override fun getAppVersionCode(packageName: String): Long {
            val packageManager: PackageManager = context.packageManager
            try {
                val packageInfo = packageManager.getPackageInfo(packageName, 0)
                return packageInfo.longVersionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return -1
    }

    override fun getAppLastUpdateTime(packageName: String): Long {
        val packageManager: PackageManager = context.packageManager
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            return packageInfo.lastUpdateTime
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return -1
    }

    override fun getListByPackages(pkgName: String): Flow<UiStates<List<String>>> = flow {
        val list = rootDatabase.getListByPackages(pkgName).filterNot {
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

        emit(UiStates.Success(list))
    }

}