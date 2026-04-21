package ua.polodarb.settings.screens.xposedStatus

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.polodarb.repository.settings.SettingsRepository
import ua.polodarb.settings.XposedModuleStatus
import ua.polodarb.settings.XposedTargetState
import ua.polodarb.settings.XposedTargetStatus
import ua.polodarb.xposed.info.HookInfo

class XposedStatusViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val lastKnownHookInfo = mutableMapOf<String, HookInfo>()

    private val _targets = MutableStateFlow(defaultTargets())
    val targets: StateFlow<List<XposedTargetStatus>> = _targets.asStateFlow()

    private val _fixState = MutableStateFlow<FixState>(FixState.Idle)
    val fixState: StateFlow<FixState> = _fixState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                val versions = settingsRepository.getPhenotypeVersions()
                val hookStates = settingsRepository.getXposedHookStates()
                _targets.value = listOf(
                    buildTarget(GMS_PACKAGE, "Google Play Services", versions, hookStates),
                    buildTarget(VENDING_PACKAGE, "Play Store", versions, hookStates),
                )
            }.onFailure {
                Log.e(TAG, "Failed to load xposed status", it)
            }
        }
    }

    fun fixWalletAttestation() {
        viewModelScope.launch(Dispatchers.IO) {
            _fixState.value = FixState.Loading
            try {
                val updated = settingsRepository.fixWalletAttestation()
                _fixState.value = FixState.Success(updated)
            } catch (e: Exception) {
                _fixState.value = FixState.Error(e.message ?: "Failed to execute fix")
            }
        }
    }

    private fun buildTarget(
        packageName: String,
        label: String,
        versions: Map<String, String>,
        hookStates: Map<String, String>,
    ): XposedTargetStatus {
        val version = versions[packageName]?.toIntOrNull()
        val hookInfo = hookStates[packageName]
            ?.takeIf { it.isNotEmpty() }
            ?.let { HookInfo.deserialize(it) }

        if (hookInfo != null) {
            lastKnownHookInfo[packageName] = hookInfo
        }
        val lastInfo = if (hookInfo == null) lastKnownHookInfo[packageName] else null

        return XposedTargetStatus(
            packageName = packageName,
            label = label,
            version = version,
            state = XposedModuleStatus.resolveState(version, hookInfo, lastInfo),
            hookInfo = hookInfo ?: lastInfo,
        )
    }

    private fun defaultTargets() = listOf(
        XposedTargetStatus(GMS_PACKAGE, "Google Play Services", null, XposedTargetState.UNKNOWN),
        XposedTargetStatus(VENDING_PACKAGE, "Play Store", null, XposedTargetState.UNKNOWN),
    )

    companion object {
        private const val TAG = "XposedStatusVM"
        private const val GMS_PACKAGE = XposedModuleStatus.GMS_PACKAGE_NAME
        private const val VENDING_PACKAGE = XposedModuleStatus.VENDING_PACKAGE_NAME
    }
}

sealed class FixState {
    data object Idle : FixState()
    data object Loading : FixState()
    data class Success(val updatedRows: Int) : FixState()
    data class Error(val message: String) : FixState()
}
