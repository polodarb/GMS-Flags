package ua.polodarb.repository.appsList

import kotlinx.coroutines.flow.Flow
import ua.polodarb.repository.uiStates.UiStates

interface AppsListRepository {

    suspend fun getAllInstalledApps(): Flow<UiStates<List<AppInfo>>>

    suspend fun getAppVersionCode(packageName: String): Long

    suspend fun getAppLastUpdateTime(packageName: String): Long

    suspend fun getListByPackages(pkgName: String): Flow<UiStates<List<String>>>

}