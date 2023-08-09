package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.util.Log
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

    private val changedFilterBoolList = mutableMapOf<String, Boolean>()

    // Search
    var searchQuery = mutableStateOf("")

    private val listBoolFiltered: MutableMap<String, Boolean> = mutableMapOf()
    private val listIntFiltered: MutableMap<String, String> = mutableMapOf()
    private val listFloatFiltered: MutableMap<String, String> = mutableMapOf()
    private val listStringFiltered: MutableMap<String, String> = mutableMapOf()

    init {
        initBoolValues()
        initIntValues()
        initFloatValues()
        initStringValues()
    }

    // Boolean
    private fun initBoolValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initBoolValues()")
                repository.getBoolFlags(pkgName).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeBooleanUiStates.Success -> {
                            Log.e("bool", "Success INIT")
                            listBoolFiltered.putAll(uiStates.data)
                            getBoolFlags()
                        }

                        is FlagChangeBooleanUiStates.Loading -> {
                            Log.e("bool", "Loading INIT")
                            _stateBoolean.value = FlagChangeBooleanUiStates.Loading
                        }

                        is FlagChangeBooleanUiStates.Error -> {
                            _stateBoolean.value = FlagChangeBooleanUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getBoolFlags() {
        if (listBoolFiltered.isNotEmpty()) {
            when (filterMethod.value) {
                FilterMethod.ENABLED -> {
                    _stateBoolean.value = FlagChangeBooleanUiStates.Success(
                        (listBoolFiltered.toMap().filterByEnabled()).filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeBooleanUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeBooleanUiStates.Error()
                }

                FilterMethod.DISABLED -> {
                    _stateBoolean.value = FlagChangeBooleanUiStates.Success(
                        (listBoolFiltered.toMap().filterByDisabled()).filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeBooleanUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeBooleanUiStates.Error()
                }

                FilterMethod.CHANGED -> {
                    _stateBoolean.value = FlagChangeBooleanUiStates.Success(
                        (changedFilterBoolList.toMap().filterByEnabled()).filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeBooleanUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeBooleanUiStates.Error()
                }

                else -> {
                    _stateBoolean.value = FlagChangeBooleanUiStates.Success(
                        listBoolFiltered.filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeBooleanUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeBooleanUiStates.Error()
                }
            }
        }
    }

    // Integer
    private fun initIntValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initIntValues()")
                repository.getIntFlags(pkgName).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeOtherTypesUiStates.Success -> {
                            listIntFiltered.putAll(uiStates.data)
                            getIntFlags()
                        }

                        is FlagChangeOtherTypesUiStates.Loading -> {
                            _stateInteger.value = FlagChangeOtherTypesUiStates.Loading
                        }

                        is FlagChangeOtherTypesUiStates.Error -> {
                            _stateInteger.value = FlagChangeOtherTypesUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getIntFlags() {
        _stateInteger.value = FlagChangeOtherTypesUiStates.Success(
            listIntFiltered.filter {
                it.key.contains(searchQuery.value, ignoreCase = true)
            }
        )
        if ((_stateInteger.value as FlagChangeOtherTypesUiStates.Success).data.isEmpty()) _stateInteger.value =
            FlagChangeOtherTypesUiStates.Error()
    }

    // Float
    private fun initFloatValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initFloatValues()")
                repository.getFloatFlags(pkgName).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeOtherTypesUiStates.Success -> {
                            listFloatFiltered.putAll(uiStates.data)
                            getFloatFlags()
                        }

                        is FlagChangeOtherTypesUiStates.Loading -> {
                            _stateFloat.value = FlagChangeOtherTypesUiStates.Loading
                        }

                        is FlagChangeOtherTypesUiStates.Error -> {
                            _stateInteger.value = FlagChangeOtherTypesUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getFloatFlags() {
        _stateFloat.value = FlagChangeOtherTypesUiStates.Success(
            listFloatFiltered.filter {
                it.key.contains(searchQuery.value, ignoreCase = true)
            }
        )
        if ((_stateFloat.value as FlagChangeOtherTypesUiStates.Success).data.isEmpty()) _stateFloat.value =
            FlagChangeOtherTypesUiStates.Error()
    }

    // String
    private fun initStringValues() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initStringValues()")
                repository.getStringFlags(pkgName).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeOtherTypesUiStates.Success -> {
                            listStringFiltered.putAll(uiStates.data)
                            getStringFlags()
                        }

                        is FlagChangeOtherTypesUiStates.Loading -> {
                            _stateString.value = FlagChangeOtherTypesUiStates.Loading
                        }

                        is FlagChangeOtherTypesUiStates.Error -> {
                            _stateInteger.value = FlagChangeOtherTypesUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getStringFlags() {
        _stateString.value = FlagChangeOtherTypesUiStates.Success(
            listStringFiltered.filter {
                it.key.contains(searchQuery.value, ignoreCase = true)
            }
        )
        if ((_stateString.value as FlagChangeOtherTypesUiStates.Success).data.isEmpty()) _stateString.value =
            FlagChangeOtherTypesUiStates.Error()
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