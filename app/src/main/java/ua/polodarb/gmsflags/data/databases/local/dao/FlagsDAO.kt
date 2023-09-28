package ua.polodarb.gmsflags.data.databases.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.data.databases.local.enities.SavedPackages

@Dao
interface FlagsDAO {

    @Query("SELECT * FROM saved_flags")
    fun getSavedFlags(): Flow<List<SavedFlags>>

    @Insert(entity = SavedFlags::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFlag(flagName: SavedFlags)

    @Query("DELETE FROM saved_flags WHERE flag_name = :flagName AND pkg_name = :pkgName")
    suspend fun deleteSavedFlag(flagName: String, pkgName: String)

    @Query("DELETE FROM saved_flags")
    fun deleteAllSavedFlags()

}