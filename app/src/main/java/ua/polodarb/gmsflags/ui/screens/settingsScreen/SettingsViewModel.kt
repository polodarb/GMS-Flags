package ua.polodarb.gmsflags.ui.screens.settingsScreen

import androidx.lifecycle.ViewModel
import ua.polodarb.gmsflags.data.repo.SettingsRepository

class SettingsViewModel(
    val settingsRepository: SettingsRepository
): ViewModel() {

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

}