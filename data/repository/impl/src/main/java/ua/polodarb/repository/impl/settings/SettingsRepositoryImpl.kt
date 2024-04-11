package ua.polodarb.repository.impl.settings

import android.content.Context
import ua.polodarb.gms.init.InitRootDB
import ua.polodarb.local.source.LocalDBSource
import ua.polodarb.repository.settings.SettingsRepository

class SettingsRepositoryImpl(
    context: Context,
    private val localDB: LocalDBSource,
    private val rootDB: InitRootDB
): SettingsRepository {

    private val rootDatabase get() = rootDB.getRootDatabase()

    override fun deleteAllOverriddenFlagsFromGMS() {
        rootDatabase.deleteAllOverriddenFlagsFromGMS()
    }

    override fun deleteAllOverriddenFlagsFromPlayStore() {
        rootDatabase.deleteAllOverriddenFlagsFromPlayStore()
    }

    override suspend fun deleteAllSavedFlags() {
        localDB.deleteAllSavedFlags()
    }

    override suspend fun deleteAllSavedPackages() {
        localDB.deleteAllSavedPackages()
    }
}
