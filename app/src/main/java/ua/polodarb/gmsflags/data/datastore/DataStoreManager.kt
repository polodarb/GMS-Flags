package ua.polodarb.gmsflags.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

const val GMS_FLAGS_DATASTORE ="gms_flags_datastore"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = GMS_FLAGS_DATASTORE)

class DataStoreManager (val context: Context) {

    companion object {
        val SUGGEST_FLAG_1 = booleanPreferencesKey("suggest_flag_1")
        val SUGGEST_FLAG_2 = booleanPreferencesKey("suggest_flag_2")
        val SUGGEST_FLAG_3 = booleanPreferencesKey("suggest_flag_3")
    }

    suspend fun saveFlag1(value: Boolean) {
        context.dataStore.edit {
            it[SUGGEST_FLAG_1] = value
        }
    }

    suspend fun saveFlag2(value: Boolean) {
        context.dataStore.edit {
            it[SUGGEST_FLAG_2] = value
        }
    }

    suspend fun saveFlag3(value: Boolean) {
        context.dataStore.edit {
            it[SUGGEST_FLAG_3] = value
        }
    }

    fun getFlag1(): Flow<Boolean> = context.dataStore.data.map {
        it[SUGGEST_FLAG_1] ?: false
    }

    fun getFlag2(): Flow<Boolean> = context.dataStore.data.map {
        it[SUGGEST_FLAG_2] ?: false
    }

    fun getFlag3(): Flow<Boolean> = context.dataStore.data.map {
        it[SUGGEST_FLAG_3] ?: false
    }

}
