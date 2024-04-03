package ua.polodarb.repository.impl.databases.local

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.polodarb.common.fagsTypes.FlagsTypes
import ua.polodarb.local.entities.SavedPackagesEntity
import ua.polodarb.local.source.LocalDBSource
import ua.polodarb.repository.databases.local.LocalDBRepository
import ua.polodarb.repository.databases.local.mapper.toSavedFlags
import ua.polodarb.repository.databases.local.mapper.toSavedFlagsEntity
import ua.polodarb.repository.databases.local.model.SavedFlags

class LocalDBRepositoryImpl(
    private val localDBSource: LocalDBSource
): LocalDBRepository {

    override suspend fun getSavedPackages(): Flow<List<String>> = flow {
        localDBSource.getSavedPackages().collect {
            emit(it)
        }
    }

    override suspend fun deleteSavedPackage(pkgName: String) {
        localDBSource.deleteSavedPackage(pkgName)
    }

    override suspend fun savePackage(pkgName: String) {
        localDBSource.savePackage(SavedPackagesEntity(pkgName))
    }

    override suspend fun getSavedFlags(): Flow<List<SavedFlags>> = flow {
        localDBSource.getSavedFlags().collect {
            emit(it.toSavedFlags())
        }
    }

    override suspend fun deleteSavedFlag(flagName: String, pkgName: String) {
        localDBSource.deleteSavedFlag(flagName, pkgName)
    }

    override suspend fun saveFlag(flagName: String, pkgName: String, flagType: FlagsTypes) {
        localDBSource.saveFlag(SavedFlags(pkgName, flagName, flagType).toSavedFlagsEntity())
    }

}