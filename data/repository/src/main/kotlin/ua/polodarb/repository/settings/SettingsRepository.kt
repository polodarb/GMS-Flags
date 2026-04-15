package ua.polodarb.repository.settings

interface SettingsRepository {

    fun deleteAllOverriddenFlagsFromGMS()

    fun deleteAllOverriddenFlagsFromPlayStore()

    fun getPhenotypeVersions(): Map<String, String>

    fun getXposedHookStates(): Map<String, String>

    suspend fun deleteAllSavedFlags()

    suspend fun deleteAllSavedPackages()

}
