package ua.polodarb.gmsflags.data.databases.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.polodarb.gmsflags.data.databases.local.enities.SavedPackages

@Dao
interface PackagesDAO {

    @Query("SELECT * FROM saved_packages")
    fun getSavedPackages(): Flow<List<String>>

    @Insert(entity = SavedPackages::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePackage(pkgName: SavedPackages)

    @Query("DELETE FROM saved_packages WHERE pkg_name = :pkgName")
    suspend fun deleteSavedPackage(pkgName: String)

    @Query("DELETE FROM saved_packages")
    fun deleteAllSavedPackages()

}