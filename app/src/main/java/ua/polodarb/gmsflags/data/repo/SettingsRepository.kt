package ua.polodarb.gmsflags.data.repo

import android.content.Context
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.local.impl.dao.FlagsDAO
import ua.polodarb.local.impl.dao.PackagesDAO

class SettingsRepository(
    context: Context,
    private val flagsDao: FlagsDAO,
    private val packagesDAO: PackagesDAO
) {
    private val gmsApplication = context as GMSApplication

    fun deleteAllOverriddenFlagsFromGMS() {
        gmsApplication.getRootDatabase().deleteAllOverriddenFlagsFromGMS()
    }

    fun deleteAllOverriddenFlagsFromPlayStore() {
        gmsApplication.getRootDatabase().deleteAllOverriddenFlagsFromPlayStore()
    }

    fun deleteAllSavedFlags() {
        flagsDao.deleteAllSavedFlags()
    }

    fun deleteAllSavedPackages() {
        packagesDAO.deleteAllSavedPackages()
    }
}
