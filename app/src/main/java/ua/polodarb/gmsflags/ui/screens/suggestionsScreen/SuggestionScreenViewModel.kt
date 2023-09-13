package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.repo.DatabaseRepository
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates

class SuggestionScreenViewModel(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _stateSuggestionsFlags =
        MutableStateFlow<SuggestionsScreenUiStates>(SuggestionsScreenUiStates.Loading)
    val stateSuggestionsFlags: StateFlow<SuggestionsScreenUiStates> = _stateSuggestionsFlags.asStateFlow()

    private val usersList = mutableListOf<String>()

    init {
        Log.e("sugg", "init VM")
        getAllOverriddenBoolFlags()
    }

    fun updateFlagValue(phenotypeFlagName: String, newValue: Boolean) {
        val currentState = _stateSuggestionsFlags.value
        if (currentState is SuggestionsScreenUiStates.Success) {
            val updatedData = currentState.data.toMutableList()
            val flagToUpdateIndex = updatedData.indexOfFirst { it.phenotypeFlagName == phenotypeFlagName }
            if (flagToUpdateIndex != -1) {
                updatedData[flagToUpdateIndex] = updatedData[flagToUpdateIndex].copy(flagValue = newValue)
                _stateSuggestionsFlags.value = currentState.copy(data = updatedData)
            }
        }
    }




    fun initUsers() {
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

    fun updateFlagValues(suggestedFlags: List<SuggestedFlag>, flagValuesMap: Map<String, String>): List<SuggestedFlag> {
        val list =  suggestedFlags.map { suggestedFlag ->
            val newFlagValue = flagValuesMap[suggestedFlag.phenotypeFlagName]?.toIntOrNull() == 1
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

    fun clearPhenotypeCache(pkgName: String) {
        val androidPkgName = repository.androidPackage(pkgName)
        CoroutineScope(Dispatchers.IO).launch {
            Shell.cmd("am force-stop $androidPkgName").exec()
            Shell.cmd("rm -rf /data/data/$androidPkgName/files/phenotype").exec()
            repeat(3) {
                Shell.cmd("am start -a android.intent.action.MAIN -n $androidPkgName &").exec()
                Shell.cmd("am force-stop $androidPkgName").exec()
            }
        }
    }

    fun overrideFlag(
        packageName: String,
        name: String,
        flagType: Int = 0,
        intVal: String? = null,
        boolVal: String? = null,
        floatVal: String? = null,
        stringVal: String? = null,
        extensionVal: String? = null,
        committed: Int = 0
    ) {
        initUsers()
        repository.deleteRowByFlagName(packageName, name)
        repository.overrideFlag(
            packageName = packageName,
            user = "",
            name = name,
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
                name = name,
                flagType = flagType,
                intVal = intVal,
                boolVal = boolVal,
                floatVal = floatVal,
                stringVal = stringVal,
                extensionVal = extensionVal,
                committed = committed
            )
        }
        clearPhenotypeCache(packageName)
    }

}

