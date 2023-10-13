package ua.polodarb.gmsflags.data.repo

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.AppInfo
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.appsScreen.dialog.DialogUiStates

class AppsListRepository(
    private val context: Context
) {

    private val gmsApplication = context as GMSApplication

    fun getAllInstalledApps() = flow {
        emit(UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val gmsPackages = (context as GMSApplication).getRootDatabase().googlePackages
                val pm = context.packageManager

                val finskyPackages = gmsPackages.filter { it.contains("finsky") }

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
                    .map { AppInfo.create(pm, it) }
                    .sortedBy { it.appName }
                    .toList()

                if (filteredAppInfoList.isNotEmpty()) {
                    emit(UiStates.Success(filteredAppInfoList))
                } else {
                    emit(UiStates.Error())
                }
            }
        }
    }

    fun getListByPackages(pkgName: String) = flow<DialogUiStates> {
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

        emit(DialogUiStates.Success(list))
    }

}