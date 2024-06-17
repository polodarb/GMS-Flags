package ua.polodarb.preferences.impl.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.polodarb.preferences.datastore.DatastoreManager

val Context.dataStore by preferencesDataStore(name = "gms_flags_datastore")

class DatastoreManagerImpl(
    private val context: Context
) : DatastoreManager {

    private val prefKeyOpenGmsSettings =
        booleanPreferencesKey(name = "is_open_gms_settings_btn_clicked")
    private val prefKeyExecutionCount =
        intPreferencesKey("phixit_worker_execution_count")

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

}