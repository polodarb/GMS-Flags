package ua.polodarb.preferences.impl.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.polodarb.preferences.datastore.DatastoreManager
import ua.polodarb.preferences.datastore.models.LastUpdatesAppModel
import ua.polodarb.preferences.datastore.models.SyncTimePrefsModel
import java.util.concurrent.TimeUnit

val Context.dataStore by preferencesDataStore(name = "gms_flags_datastore")

class DatastoreManagerImpl(
    private val context: Context
) : DatastoreManager {

    private val prefKeyOpenGmsSettings =
        booleanPreferencesKey(name = "is_open_gms_settings_btn_clicked")
    private val prefKeyExecutionCount =
        intPreferencesKey("phixit_worker_execution_count")
    private val prefKeyLastUpdatedGoogleApp =
        stringPreferencesKey("last_updated_google_app")
    private val prefKeyFilteredGoogleApps =
        stringPreferencesKey("filtered_google_apps")
    private val prefKeySyncTimeValue =
        longPreferencesKey("sync_time_value")
    private val prefKeySyncTimeUnit =
        stringPreferencesKey("sync_time_unit")

    override val isOpenGmsSettingsBtnClicked: Flow<Boolean> =
        context.dataStore.data.map { prefs -> prefs[prefKeyOpenGmsSettings] ?: false }

    override val phixitWorkerExecutionCount: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[prefKeyExecutionCount] ?: 0
    }

    override fun setOpenGmsSettingsBtnClicked(value: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs[prefKeyOpenGmsSettings] = value
            }
        }
    }

    override suspend fun phixitWorkerIncrementExecutionCount() {
        context.dataStore.edit { prefs ->
            val currentCount = prefs[prefKeyExecutionCount] ?: 0
            prefs[prefKeyExecutionCount] = currentCount + 1
        }
    }

    override suspend fun setLastUpdatedGoogleApp(data: LastUpdatesAppModel) {
        context.dataStore.edit { prefs ->
            prefs[prefKeyLastUpdatedGoogleApp] = "${data.appName}|${data.appVersion}"
        }
    }

    override suspend fun getLastUpdatedGoogleApp(): LastUpdatesAppModel {
        val prefs = context.dataStore.data.first()
        val savedData = prefs[prefKeyLastUpdatedGoogleApp] ?: ""
        val (appName, date) = savedData.split("|")
        return LastUpdatesAppModel(appName, date)
    }

    override suspend fun getFilteredGoogleApps(): String {
        val prefs = context.dataStore.data.first()
        val savedData = prefs[prefKeyFilteredGoogleApps] ?: "Wear OS, Android TV, Trichrome"
        return savedData
    }

    override suspend fun setFilteredGoogleApps(data: String) {
        CoroutineScope(Dispatchers.IO).launch {
            context.dataStore.edit { prefs ->
                prefs[prefKeyFilteredGoogleApps] = data
            }
        }
    }

    override suspend fun setWorkerSyncTime(syncTime: SyncTimePrefsModel) {
        context.dataStore.edit { prefs ->
            prefs[prefKeySyncTimeValue] = syncTime.value
            prefs[prefKeySyncTimeUnit] = syncTime.unit.name
        }
    }

    override suspend fun getWorkerSyncTime(): SyncTimePrefsModel {
        val prefs = context.dataStore.data.first()
        val value = prefs[prefKeySyncTimeValue] ?: 15L
        val unit = prefs[prefKeySyncTimeUnit]?.let { TimeUnit.valueOf(it) } ?: TimeUnit.MINUTES
        return SyncTimePrefsModel(value, unit)
    }
}