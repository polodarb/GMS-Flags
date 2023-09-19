package ua.polodarb.gmsflags.data.repo

import android.util.Log
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.data.databases.local.dao.PackagesDAO
import ua.polodarb.gmsflags.data.databases.local.enities.SavedPackages

class RoomDBRepository(
    private val savedPackagesDao: PackagesDAO
) {

    suspend fun getSavedPackages() = flow<List<String>> {
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

}