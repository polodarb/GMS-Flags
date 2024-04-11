package ua.polodarb.local.source

import kotlinx.coroutines.flow.Flow
import ua.polodarb.local.entities.SavedFlagsEntity
import ua.polodarb.local.entities.SavedPackagesEntity

interface LocalDBSource {

    suspend fun getSavedFlags(): Flow<List<SavedFlagsEntity>>

    suspend fun saveFlag(flagName: SavedFlagsEntity)

    suspend fun deleteSavedFlag(flagName: String, pkgName: String)

    suspend fun deleteAllSavedFlags()

    suspend fun getSavedPackages(): Flow<List<String>>

    suspend fun savePackage(pkgName: SavedPackagesEntity)

    suspend fun deleteSavedPackage(pkgName: String)

    suspend fun deleteAllSavedPackages()

}