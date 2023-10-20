package ua.polodarb.gmsflags.ui.screens.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.SettingsRepository

class SettingsViewModel(
    val settingsRepository: SettingsRepository
) : ViewModel() {

    // GMS DATABASE
    fun deleteAllOverriddenFlagsFromGMS() {
        settingsRepository.deleteAllOverriddenFlagsFromGMS()
        clearCache()
    }

    fun deleteAllOverriddenFlagsFromPlayStore() {
        settingsRepository.deleteAllOverriddenFlagsFromPlayStore()
        clearCache()
    }

    fun deleteAllOverriddenFlags() {
        settingsRepository.deleteAllOverriddenFlagsFromGMS()
        settingsRepository.deleteAllOverriddenFlagsFromPlayStore()
        clearCache()
    }

    // Clear Google Photos and Play Store phenotype cache
    private fun clearCache() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Shell.cmd("rm -rf /data/data/com.android.vending/files/experiment*").exec()
                Shell.cmd("am force-stop com.android.vending").exec()
                Shell.cmd("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/phenotype*").exec()
                Shell.cmd("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/com.google.android.apps.photos.phenotype.xml").exec()
                Shell.cmd("am force-stop com.google.android.apps.photos").exec()
            }
        }
    }

    // Local Room DATABASE
    fun deleteAllSavedFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                settingsRepository.deleteAllSavedFlags()
            }
        }
    }

    fun deleteAllSavedPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                settingsRepository.deleteAllSavedPackages()
            }
        }
    }

    fun deleteAllSavedFlagsAndPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                settingsRepository.deleteAllSavedFlags()
                settingsRepository.deleteAllSavedPackages()
            }
        }
    }

}