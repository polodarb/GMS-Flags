package ua.polodarb.gmsflags.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

const val GMS_FLAGS_DATASTORE ="GMS_FLAGS_DATASTORE"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = GMS_FLAGS_DATASTORE)

class DataStoreManager (val context: Context) {

    companion object {
        val FIRST_START = booleanPreferencesKey("FIRST_START")
    }

    suspend fun setFirstStart(firstStart: Boolean) {
        context.dataStore.edit {
            it[FIRST_START] = firstStart
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        it[FIRST_START] ?: true
    }

}