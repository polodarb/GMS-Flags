package ua.polodarb.gmsflags.ui.screens.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.SettingsRepository

class SettingsViewModel(
    val settingsRepository: SettingsRepository
): ViewModel() {

    // GMS DATABASE
    fun deleteAllOverriddenFlagsFromGMS() {
        settingsRepository.deleteAllOverriddenFlagsFromGMS()
    }

    fun deleteAllOverriddenFlagsFromPlayStore() {
        settingsRepository.deleteAllOverriddenFlagsFromPlayStore()
    }

    fun deleteAllOverriddenFlags() {
        settingsRepository.deleteAllOverriddenFlagsFromGMS()
        settingsRepository.deleteAllOverriddenFlagsFromPlayStore()
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