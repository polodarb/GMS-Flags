package ua.polodarb.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.polodarb.repository.flagsFile.FlagsFromFileRepository
import ua.polodarb.repository.flagsFile.model.LoadedFlagData
import ua.polodarb.repository.flagsFile.model.LoadedFlags
import ua.polodarb.repository.settings.SettingsRepository

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
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
