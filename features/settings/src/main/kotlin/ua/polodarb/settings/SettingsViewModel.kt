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
    private val flagsFromFileRepository: FlagsFromFileRepository
) : ViewModel() {

    suspend fun write() {
        val abc = flagsFromFileRepository.write(
            flags = LoadedFlags(
                packageName = "com.package.test",
                flags = LoadedFlagData(
                    bool = mapOf("Test_bool_1" to true, "Test_bool_2" to false),
                    int = mapOf("Test_int_1" to 10),
                    float = mapOf("Test_float_1" to 1.55f),
                    string = mapOf("Test_str_1" to "abc123"),
                    extVal = mapOf("Test_extVal_1" to "0x256")
                )
            ),
            fileName = "test"
        )
        Log.e("file", abc.path.toString())
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
}
