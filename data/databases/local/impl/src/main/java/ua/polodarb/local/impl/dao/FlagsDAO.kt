package ua.polodarb.local.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.polodarb.local.entities.SavedFlagsEntity

@Dao
interface FlagsDAO {
    @Query("SELECT * FROM saved_flags")
    fun getSavedFlags(): Flow<List<SavedFlagsEntity>>

    @Insert(entity = SavedFlagsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFlag(flagName: SavedFlagsEntity)

    @Query("DELETE FROM saved_flags WHERE flag_name = :flagName AND pkg_name = :pkgName")
    suspend fun deleteSavedFlag(flagName: String, pkgName: String)

    @Query("DELETE FROM saved_flags")
    fun deleteAllSavedFlags()
}

