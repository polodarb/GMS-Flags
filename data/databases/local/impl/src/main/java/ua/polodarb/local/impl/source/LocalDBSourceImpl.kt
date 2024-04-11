package ua.polodarb.local.impl.source

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ua.polodarb.local.entities.SavedFlagsEntity
import ua.polodarb.local.entities.SavedPackagesEntity
import ua.polodarb.local.impl.dao.FlagsDAO
import ua.polodarb.local.impl.dao.PackagesDAO
import ua.polodarb.local.source.LocalDBSource

class LocalDBSourceImpl(
    private val packagesDao: PackagesDAO,
    private val flagsDao: FlagsDAO
): LocalDBSource {

    override suspend fun getSavedFlags(): Flow<List<SavedFlagsEntity>> {
        return withContext(Dispatchers.IO) {
            flagsDao.getSavedFlags()
        }
    }

    override suspend fun saveFlag(flagName: SavedFlagsEntity) {
        withContext(Dispatchers.IO) {
            flagsDao.saveFlag(flagName)
        }
    }

    override suspend fun deleteSavedFlag(flagName: String, pkgName: String) {
        withContext(Dispatchers.IO) {
            flagsDao.deleteSavedFlag(flagName, pkgName)
        }
    }

    override suspend fun deleteAllSavedFlags() {
        withContext(Dispatchers.IO) {
            flagsDao.deleteAllSavedFlags()
        }
    }

    override suspend fun getSavedPackages(): Flow<List<String>> {
        return withContext(Dispatchers.IO) {
            packagesDao.getSavedPackages()
        }
    }

    override suspend fun savePackage(pkgName: SavedPackagesEntity) {
        withContext(Dispatchers.IO) {
            packagesDao.savePackage(pkgName)
        }
    }

    override suspend fun deleteSavedPackage(pkgName: String) {
        withContext(Dispatchers.IO) {
            packagesDao.deleteSavedPackage(pkgName)
        }
    }

    override suspend fun deleteAllSavedPackages() {
        withContext(Dispatchers.IO) {
            packagesDao.deleteAllSavedPackages()
        }
    }

}