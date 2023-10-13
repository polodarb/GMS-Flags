package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates

class SuggestionScreenViewModel(
    private val repository: GmsDBRepository,
    private val appsRepository: AppsListRepository
) : ViewModel() {

    private val _stateSuggestionsFlags =
        MutableStateFlow<SuggestionsScreenUiStates>(SuggestionsScreenUiStates.Loading)
    val stateSuggestionsFlags: StateFlow<SuggestionsScreenUiStates> = _stateSuggestionsFlags.asStateFlow()

    private val usersList = mutableListOf<String>()

    init {
        getAllOverriddenBoolFlags()
        initGmsPackages()
    }

    private fun initGmsPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appsRepository.getAllInstalledApps().collectLatest {  }
            }
        }
    }

    fun updateFlagValue(newValue: Boolean, index: Int) {
        val currentState = _stateSuggestionsFlags.value
        if (currentState is SuggestionsScreenUiStates.Success) {
            val updatedData = currentState.data.toMutableList()
            if (index != -1) {
                updatedData[index] = updatedData[index].copy(flagValue = newValue)
                _stateSuggestionsFlags.value = currentState.copy(data = updatedData)
            }
        }
    }

    private fun initUsers() {
        usersList.clear()
        usersList.addAll(repository.getUsers())
    }

    fun getAllOverriddenBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getAllOverriddenBoolFlags().collect { uiState ->
                    when (uiState) {
                        is FlagChangeUiStates.Success -> {
                            val suggestedFlagsList = updateFlagValues(SuggestedFlagsList.suggestedFlagsList.toList(), uiState.data)
                            _stateSuggestionsFlags.value = SuggestionsScreenUiStates.Success(suggestedFlagsList)
                        }
                        is FlagChangeUiStates.Loading -> {
                            _stateSuggestionsFlags.value = SuggestionsScreenUiStates.Loading
                        }
                        is FlagChangeUiStates.Error -> {
                            _stateSuggestionsFlags.value = SuggestionsScreenUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    private fun updateFlagValues(suggestedFlags: List<SuggestedFlag>, flagValuesMap: Map<String, String>): List<SuggestedFlag> {
        val list =  suggestedFlags.map { suggestedFlag ->
            val newFlagValue = flagValuesMap[suggestedFlag.phenotypeFlagName[0]]?.toIntOrNull() == 1
            SuggestedFlag(
                suggestedFlag.flagName,
                suggestedFlag.flagSender,
                suggestedFlag.phenotypeFlagName,
                suggestedFlag.phenotypePackageName,
                newFlagValue
            )
        }
        return list
    }

    private fun clearPhenotypeCache(pkgName: String) {
        val androidPkgName = repository.androidPackage(pkgName)
        CoroutineScope(Dispatchers.IO).launch {
            Shell.cmd("am force-stop $androidPkgName").exec()
            Shell.cmd("rm -rf /data/data/$androidPkgName/files/phenotype").exec()
        }
    }

    fun overrideFlag(
        packageName: String,
        name: List<String>,
        flagType: Int = 0,
        intVal: String? = null,
        boolVal: String? = null,
        floatVal: String? = null,
        stringVal: String? = null,
        extensionVal: String? = null,
        committed: Int = 0
    ) {
        initUsers()
        name.forEach {
            repository.deleteRowByFlagName(packageName, it)
            repository.overrideFlag(
                packageName = packageName,
                user = "",
                name = it,
                flagType = flagType,
                intVal = intVal,
                boolVal = boolVal,
                floatVal = floatVal,
                stringVal = stringVal,
                extensionVal = extensionVal,
                committed = committed
            )
            for (i in usersList) {
                repository.overrideFlag(
                    packageName = packageName,
                    user = i,
                    name = it,
                    flagType = flagType,
                    intVal = intVal,
                    boolVal = boolVal,
                    floatVal = floatVal,
                    stringVal = stringVal,
                    extensionVal = extensionVal,
                    committed = committed
                )
            }
        }
        clearPhenotypeCache(packageName)
    }

}

