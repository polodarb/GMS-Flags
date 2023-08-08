package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.DatabaseRepository

class FlagChangeScreenViewModel(
    private val pkgName: String,
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _stateBoolean =
        MutableStateFlow<FlagChangeBooleanUiStates>(FlagChangeBooleanUiStates.Loading)
    val stateBoolean: StateFlow<FlagChangeBooleanUiStates> = _stateBoolean.asStateFlow()

    private val _stateInteger =
        MutableStateFlow<FlagChangeOtherTypesUiStates>(FlagChangeOtherTypesUiStates.Loading)
    val stateInteger: StateFlow<FlagChangeOtherTypesUiStates> = _stateInteger.asStateFlow()

    private val _stateFloat =
        MutableStateFlow<FlagChangeOtherTypesUiStates>(FlagChangeOtherTypesUiStates.Loading)
    val stateFloat: StateFlow<FlagChangeOtherTypesUiStates> = _stateFloat.asStateFlow()

    private val _stateString =
        MutableStateFlow<FlagChangeOtherTypesUiStates>(FlagChangeOtherTypesUiStates.Loading)
    val stateString: StateFlow<FlagChangeOtherTypesUiStates> = _stateString.asStateFlow()

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

    val changedFilterBoolList = mutableMapOf<String, Boolean>() // todo

    // Search
    var searchBoolQuery = mutableStateOf("")


    fun getBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getBoolFlags(pkgName).collect { uiState ->
                    when (uiState) {
                        is FlagChangeBooleanUiStates.Success -> {
                            _stateBoolean.value = when (filterMethod.value) {
                                FilterMethod.ENABLED -> FlagChangeBooleanUiStates.Success(
                                    (uiState.data.filterByEnabled()).filter {
                                        it.key.contains(searchBoolQuery.value, ignoreCase = true)
                                    }
                                )
                                FilterMethod.DISABLED -> FlagChangeBooleanUiStates.Success(
                                    (uiState.data.filterByDisabled()).filter {
                                        it.key.contains(searchBoolQuery.value, ignoreCase = true)
                                    }
                                )
                                FilterMethod.CHANGED -> FlagChangeBooleanUiStates.Success(
                                    changedFilterBoolList.filter {
                                        it.key.contains(searchBoolQuery.value, ignoreCase = true)
                                    }
                                )
                                else -> FlagChangeBooleanUiStates.Success(
                                    uiState.data.filter {
                                        it.key.contains(searchBoolQuery.value, ignoreCase = true)
                                    }
                                )
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
                            _stateInteger.value = FlagChangeOtherTypesUiStates.Success(
                                uiState.data.filter {
                                    it.key.contains(searchBoolQuery.value, ignoreCase = true)
                                }
                            )
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

    fun getFloatFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getFloatFlags(pkgName).collect { uiState ->
                    when (uiState) {
                        is FlagChangeOtherTypesUiStates.Success -> {
                            _stateFloat.value = FlagChangeOtherTypesUiStates.Success(
                                uiState.data.filter {
                                    it.key.contains(searchBoolQuery.value, ignoreCase = true)
                                }
                            )
                        }

                        is FlagChangeOtherTypesUiStates.Error -> {
                            _stateFloat.value =
                                FlagChangeOtherTypesUiStates.Error(Throwable("BOOLEAN_LOAD_ERROR"))
                        }

                        is FlagChangeOtherTypesUiStates.Loading -> {}
                    }
                }
            }
        }
    }

    fun getStringFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getStringFlags(pkgName).collect { uiState ->
                    when (uiState) {
                        is FlagChangeOtherTypesUiStates.Success -> {
                            _stateString.value = FlagChangeOtherTypesUiStates.Success(
                                uiState.data.filter {
                                    it.key.contains(searchBoolQuery.value, ignoreCase = true)
                                }
                            )
                        }

                        is FlagChangeOtherTypesUiStates.Error -> {
                            _stateString.value =
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