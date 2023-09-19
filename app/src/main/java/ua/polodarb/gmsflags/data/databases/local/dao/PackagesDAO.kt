package ua.polodarb.gmsflags.data.databases.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.polodarb.gmsflags.data.databases.local.enities.SavedPackages

@Dao
interface PackagesDAO {

    @Query("SELECT * FROM saved_packages")
    fun getSavedPackages(): List<String>

    @Insert(entity = SavedPackages::class, onConflict = OnConflictStrategy.REPLACE)
    fun savePackage(pkgName: String)

    @Query("DELETE FROM saved_packages WHERE pkg_name = :pkgName")
    fun deleteSavedPackage(pkgName: String)

    @Query("DELETE FROM saved_packages")
    fun deleteAllSavedPackages()

}