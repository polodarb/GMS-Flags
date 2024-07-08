package ua.polodarb.preferences.datastore

import kotlinx.coroutines.flow.Flow
import ua.polodarb.preferences.datastore.models.LastUpdatesAppModel

interface DatastoreManager {

   val isOpenGmsSettingsBtnClicked: Flow<Boolean>

   val phixitWorkerExecutionCount: Flow<Int>

   fun setOpenGmsSettingsBtnClicked(value: Boolean)

   suspend fun phixitWorkerIncrementExecutionCount()

   suspend fun setLastUpdatedGoogleApp(data: LastUpdatesAppModel)

   suspend fun getLastUpdatedGoogleApp(): LastUpdatesAppModel

   suspend fun getFilteredGoogleApps(): String

   suspend fun setFilteredGoogleApps(data: String )

}