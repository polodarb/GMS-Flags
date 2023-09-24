package ua.polodarb.gmsflags.data.databases.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.polodarb.gmsflags.data.databases.local.enities.SavedPackages

@Dao
interface FlagsDAO {

    @Query("SELECT * FROM saved_packages")
    fun getSavedFlags(): Flow<List<String>>

    @Insert(entity = SavedPackages::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFlag(flagName: SavedPackages)

    @Query("DELETE FROM saved_packages WHERE pkg_name = :flagName")
    suspend fun deleteSavedFlag(flagName: String)

    @Query("DELETE FROM saved_packages")
    fun deleteAllSavedFlags()

}