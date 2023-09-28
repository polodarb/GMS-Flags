package ua.polodarb.gmsflags.data.repo

import android.content.Context
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.databases.local.dao.FlagsDAO
import ua.polodarb.gmsflags.data.databases.local.dao.PackagesDAO

class SettingsRepository(
    private val context: Context,
    private val flagsDao: FlagsDAO,
    private val packagesDAO: PackagesDAO
) {

    private val gmsApplication = context as GMSApplication

    // GMS Database
    fun deleteAllOverriddenFlagsFromGMS() {
        gmsApplication.getRootDatabase().deleteAllOverriddenFlagsFromGMS()
    }

    fun deleteAllOverriddenFlagsFromPlayStore() {
        gmsApplication.getRootDatabase().deleteAllOverriddenFlagsFromPlayStore()
    }

    // Local Database
    suspend fun deleteAllSavedFlags() {
        flagsDao.deleteAllSavedFlags()
    }

    suspend fun deleteAllSavedPackages() {
        packagesDAO.deleteAllSavedPackages()
    }

}