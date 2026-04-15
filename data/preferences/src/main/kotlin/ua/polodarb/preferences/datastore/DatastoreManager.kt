package ua.polodarb.preferences.datastore

import ua.polodarb.preferences.datastore.models.LastUpdatesAppModel
import ua.polodarb.preferences.datastore.models.SyncTimePrefsModel

interface DatastoreManager {

   suspend fun setLastUpdatedGoogleApp(data: LastUpdatesAppModel)

   suspend fun getLastUpdatedGoogleApp(): LastUpdatesAppModel

   suspend fun getFilteredGoogleApps(): String

   suspend fun setFilteredGoogleApps(data: String)

   suspend fun getWorkerSyncTime(): SyncTimePrefsModel

   suspend fun setWorkerSyncTime(syncTime: SyncTimePrefsModel)

}
