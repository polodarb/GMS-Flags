package ua.polodarb.gmsflags.data.repo

import android.content.Context
import ua.polodarb.gmsflags.GMSApplication

class SettingsRepository(
    private val context: Context
) {

    private val gmsApplication = context as GMSApplication

    fun deleteAllOverriddenFlagsFromGMS() {
        gmsApplication.getRootDatabase().deleteAllOverriddenFlagsFromGMS()
    }

    fun deleteAllOverriddenFlagsFromPlayStore() {
        gmsApplication.getRootDatabase().deleteAllOverriddenFlagsFromPlayStore()
    }

}