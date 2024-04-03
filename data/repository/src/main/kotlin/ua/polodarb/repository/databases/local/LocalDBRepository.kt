package ua.polodarb.repository.databases.local

import kotlinx.coroutines.flow.Flow
import ua.polodarb.common.fagsTypes.FlagsTypes
import ua.polodarb.repository.databases.local.model.SavedFlags

interface LocalDBRepository {

    suspend fun getSavedPackages(): Flow<List<String>>

    suspend fun deleteSavedPackage(pkgName: String)

    suspend fun savePackage(pkgName: String)

    suspend fun getSavedFlags():  Flow<List<SavedFlags>>

    suspend fun deleteSavedFlag(flagName: String, pkgName: String)

    suspend fun saveFlag(flagName: String, pkgName: String, flagType: FlagsTypes)
}
