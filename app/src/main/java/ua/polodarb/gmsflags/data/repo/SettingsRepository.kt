package ua.polodarb.gmsflags.data.repo

import android.content.Context
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.databases.local.dao.FlagsDAO
import ua.polodarb.gmsflags.data.databases.local.dao.PackagesDAO

class SettingsRepository(
    private val flagsDao: FlagsDAO,
    private val packagesDAO: PackagesDAO
) {

    // GMS Database


    // Local Database
    suspend fun deleteAllSavedFlags() {
        flagsDao.deleteAllSavedFlags()
    }

    suspend fun deleteAllSavedPackages() {
        packagesDAO.deleteAllSavedPackages()
    }

}