package ua.polodarb.preferences.datastore

import kotlinx.coroutines.flow.Flow
import ua.polodarb.preferences.datastore.models.LastUpdatesAppModel
import ua.polodarb.preferences.datastore.models.SyncTimePrefsModel

interface DatastoreManager {

   val isOpenGmsSettingsBtnClicked: Flow<Boolean>

   val phixitWorkerExecutionCount: Flow<Int>

   fun setOpenGmsSettingsBtnClicked(value: Boolean)

   suspend fun phixitWorkerIncrementExecutionCount()

   suspend fun setLastUpdatedGoogleApp(data: LastUpdatesAppModel)

   suspend fun getLastUpdatedGoogleApp(): LastUpdatesAppModel

   suspend fun getFilteredGoogleApps(): String

   suspend fun setFilteredGoogleApps(data: String)

   suspend fun getWorkerSyncTime(): SyncTimePrefsModel

   suspend fun setWorkerSyncTime(syncTime: SyncTimePrefsModel)

}