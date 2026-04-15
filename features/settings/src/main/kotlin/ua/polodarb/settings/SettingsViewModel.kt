package ua.polodarb.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.polodarb.repository.settings.SettingsRepository

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _xposedTargetStatuses = MutableStateFlow(defaultXposedTargetStatuses())
    val xposedTargetStatuses: StateFlow<List<XposedTargetStatus>> = _xposedTargetStatuses.asStateFlow()

    init {
        refreshXposedTargetStatuses()
    }

    fun refreshXposedTargetStatuses() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val versions = settingsRepository.getPhenotypeVersions()
                val hookStates = settingsRepository.getXposedHookStates()
                _xposedTargetStatuses.value = listOf(
                    xposedTargetStatus(
                        packageName = XposedModuleStatus.GMS_PACKAGE_NAME,
                        label = "GMS",
                        versions = versions,
                        hookStates = hookStates
                    ),
                    xposedTargetStatus(
                        packageName = XposedModuleStatus.VENDING_PACKAGE_NAME,
                        label = "Vending",
                        versions = versions,
                        hookStates = hookStates
                    )
                )
            }.onFailure {
                Log.e("SettingsViewModel", "Failed to load Xposed target statuses", it)
            }
        }
    }

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

    private fun xposedTargetStatus(
        packageName: String,
        label: String,
        versions: Map<String, String>,
        hookStates: Map<String, String>
    ): XposedTargetStatus {
        val version = versions[packageName]?.toIntOrNull()
        return XposedTargetStatus(
            packageName = packageName,
            label = label,
            version = version,
            state = XposedModuleStatus.resolveState(version, hookStates[packageName])
        )
    }

    private fun defaultXposedTargetStatuses(): List<XposedTargetStatus> {
        return listOf(
            XposedTargetStatus(
                packageName = XposedModuleStatus.GMS_PACKAGE_NAME,
                label = "GMS",
                version = null,
                state = XposedTargetState.UNKNOWN
            ),
            XposedTargetStatus(
                packageName = XposedModuleStatus.VENDING_PACKAGE_NAME,
                label = "Vending",
                version = null,
                state = XposedTargetState.UNKNOWN
            )
        )
    }
}
