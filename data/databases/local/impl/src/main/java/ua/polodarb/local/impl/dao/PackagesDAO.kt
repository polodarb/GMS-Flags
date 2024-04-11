package ua.polodarb.local.impl.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.polodarb.local.entities.SavedPackagesEntity

@Dao
interface PackagesDAO {

    @Query("SELECT * FROM saved_packages")
    fun getSavedPackages(): Flow<List<String>>

    @Insert(entity = SavedPackagesEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePackage(pkgName: SavedPackagesEntity)

    @Query("DELETE FROM saved_packages WHERE pkg_name = :pkgName")
    suspend fun deleteSavedPackage(pkgName: String)

    @Query("DELETE FROM saved_packages")
    fun deleteAllSavedPackages()

}