package ua.polodarb.gmsflags.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.polodarb.gmsflags.data.repo.SettingsRepository

private object LocalConstants {
    const val PLAY_STORE_PKG = "com.android.vending"
    const val STOP_PLAY_STORE_CMD = "am force-stop $PLAY_STORE_PKG"
    const val CLEAN_PLAY_STORE_CMD = "rm -rf /data/data/$PLAY_STORE_PKG/files/experiment*"
    const val PHOTOS_PKG = "com.google.android.apps.photos"
    const val STOP_PHOTOS_CMD = "am force-stop $PHOTOS_PKG"
    const val CLEAN_PHOTOS_CMD = "rm -rf /data/data/$PHOTOS_PKG/shared_prefs/*phenotype*"
}

class SettingsViewModel(
    val settingsRepository: SettingsRepository
) : ViewModel() {
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

    private fun clearCache() {
        viewModelScope.launch(Dispatchers.IO) {
            Shell.cmd(LocalConstants.STOP_PLAY_STORE_CMD).exec()
            Shell.cmd(LocalConstants.CLEAN_PLAY_STORE_CMD).exec()
            Shell.cmd(LocalConstants.STOP_PHOTOS_CMD).exec()
            Shell.cmd(LocalConstants.CLEAN_PHOTOS_CMD).exec()
        }
    }

    fun deleteAllSavedFlags() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.deleteAllSavedFlags()
        }
    }

    fun deleteAllSavedPackages() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.deleteAllSavedPackages()
        }
    }

    fun deleteAllSavedFlagsAndPackages() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsRepository.deleteAllSavedFlags()
            settingsRepository.deleteAllSavedPackages()
        }
    }
}
