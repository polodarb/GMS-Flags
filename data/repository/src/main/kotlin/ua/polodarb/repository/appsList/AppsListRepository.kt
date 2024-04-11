package ua.polodarb.repository.appsList

import kotlinx.coroutines.flow.Flow
import ua.polodarb.repository.uiStates.UiStates

interface AppsListRepository {

    fun getAllInstalledApps(): Flow<UiStates<List<AppInfo>>>

    fun getAppVersionCode(packageName: String): Long

    fun getAppLastUpdateTime(packageName: String): Long

    fun getListByPackages(pkgName: String): Flow<UiStates<List<String>>>

}