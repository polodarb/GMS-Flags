package ua.polodarb.repository.settings

interface SettingsRepository {

    fun deleteAllOverriddenFlagsFromGMS()

    fun deleteAllOverriddenFlagsFromPlayStore()

    suspend fun deleteAllSavedFlags()

    suspend fun deleteAllSavedPackages()

}
