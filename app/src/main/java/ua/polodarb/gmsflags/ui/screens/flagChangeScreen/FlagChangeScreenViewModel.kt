package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.DatabaseRepository
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates

class FlagChangeScreenViewModel(
    private val pkgName: String,
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FlagChangeUiStates>(FlagChangeUiStates.Loading)
    val state: StateFlow<FlagChangeUiStates> = _state.asStateFlow()

    private val _stateBoolean =
        MutableStateFlow<FlagChangeBooleanUiStates>(FlagChangeBooleanUiStates.Loading)
    val stateBoolean: StateFlow<FlagChangeBooleanUiStates> = _stateBoolean.asStateFlow()

    private val _stateInteger =
        MutableStateFlow<FlagChangeOtherTypesUiStates>(FlagChangeOtherTypesUiStates.Loading)
    val stateInteger: StateFlow<FlagChangeOtherTypesUiStates> = _stateInteger.asStateFlow()



    // Filter
    var filterMethod = mutableStateOf(FilterMethod.ALL)

    fun Map<String, Boolean>.filterByEnabled(): Map<String, Boolean> {
        val filteredMap = mutableMapOf<String, Boolean>()
        for ((key, value) in this) {
            if (value) {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    fun Map<String, Boolean>.filterByDisabled(): Map<String, Boolean> {
        val filteredMap = mutableMapOf<String, Boolean>()
        for ((key, value) in this) {
            if (!value) {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    val changedFilterBoolList = mutableMapOf<String, Boolean>()


    fun getFlagsData(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getFlagsData(pkgName).collect { uiState ->
                    when (uiState) {
                        is FlagChangeUiStates.Success -> {
                            _state.value = FlagChangeUiStates.Success(uiState.data)
                        }

                        is FlagChangeUiStates.Error -> {
                            _state.value = FlagChangeUiStates.Error(Throwable("LIST_ERROR"))
                        }

                        is FlagChangeUiStates.Loading -> {}
                    }
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getBoolFlags(pkgName).collect { uiState ->
                    when (uiState) {
                        is FlagChangeBooleanUiStates.Success -> {
                            _stateBoolean.value = when (filterMethod) {
                                FilterMethod.ENABLED -> FlagChangeBooleanUiStates.Success(uiState.data.filterByEnabled())
                                FilterMethod.DISABLED -> FlagChangeBooleanUiStates.Success(uiState.data.filterByDisabled())
                                FilterMethod.CHANGED -> FlagChangeBooleanUiStates.Success(changedFilterBoolList)
                                else -> FlagChangeBooleanUiStates.Success(uiState.data)
                            }
                        }

                        is FlagChangeBooleanUiStates.Error -> {
                            _stateBoolean.value =
                                FlagChangeBooleanUiStates.Error(Throwable("BOOLEAN_LOAD_ERROR"))
                        }

                        is FlagChangeBooleanUiStates.Loading -> {}
                    }
                }
            }
        }
    }

    fun getIntFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getIntFlags(pkgName).collect { uiState ->
                    when (uiState) {
                        is FlagChangeOtherTypesUiStates.Success -> {
                            _stateInteger.value = FlagChangeOtherTypesUiStates.Success(uiState.data)
                        }

                        is FlagChangeOtherTypesUiStates.Error -> {
                            _stateInteger.value =
                                FlagChangeOtherTypesUiStates.Error(Throwable("BOOLEAN_LOAD_ERROR"))
                        }

                        is FlagChangeOtherTypesUiStates.Loading -> {}
                    }
                }
            }
        }
    }

}

enum class FilterMethod : MutableState<FilterMethod> {
    ALL, ENABLED, DISABLED, CHANGED;

    override var value: FilterMethod
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun component1(): FilterMethod {
        TODO("Not yet implemented")
    }

    override fun component2(): (FilterMethod) -> Unit {
        TODO("Not yet implemented")
    }
}