package ua.polodarb.gmsflags.data.repo

import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.data.databases.local.dao.FlagsDAO
import ua.polodarb.gmsflags.data.databases.local.dao.PackagesDAO
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.data.databases.local.enities.SavedPackages

class RoomDBRepository(
    private val savedPackagesDao: PackagesDAO,
    private val savedFlagsDao: FlagsDAO
) {

    suspend fun getSavedPackages() = flow {
        savedPackagesDao.getSavedPackages().collect {
            emit(it)
        }
    }

    suspend fun deleteSavedPackage(pkgName: String) {
        savedPackagesDao.deleteSavedPackage(pkgName)
    }

    suspend fun savePackage(pkgName: String) {
        savedPackagesDao.savePackage(SavedPackages(pkgName))
    }

    suspend fun getSavedFlags() = flow {
        savedFlagsDao.getSavedFlags().collect {
            emit(it)
        }
    }

    suspend fun deleteSavedFlag(flagName: String, pkgName: String) {
        savedFlagsDao.deleteSavedFlag(flagName, pkgName)
    }

    suspend fun saveFlag(flagName: String, pkgName: String, flagType: String) {
        savedFlagsDao.saveFlag(SavedFlags(pkgName, flagName, flagType))
    }
}
