package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
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
import ua.polodarb.gmsflags.data.repo.DatabaseRepository

class FlagChangeScreenViewModel(
    private val pkgName: String,
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _stateBoolean =
        MutableStateFlow<FlagChangeUiStates>(FlagChangeUiStates.Loading)
    val stateBoolean: StateFlow<FlagChangeUiStates> = _stateBoolean.asStateFlow()

    private val _stateInteger =
        MutableStateFlow<FlagChangeUiStates>(FlagChangeUiStates.Loading)
    val stateInteger: StateFlow<FlagChangeUiStates> = _stateInteger.asStateFlow()

    private val _stateFloat =
        MutableStateFlow<FlagChangeUiStates>(FlagChangeUiStates.Loading)
    val stateFloat: StateFlow<FlagChangeUiStates> = _stateFloat.asStateFlow()

    private val _stateString =
        MutableStateFlow<FlagChangeUiStates>(FlagChangeUiStates.Loading)
    val stateString: StateFlow<FlagChangeUiStates> = _stateString.asStateFlow()

    // Filter
    var filterMethod = mutableStateOf(FilterMethod.ALL)

    fun updateBoolFlagValue(flagName: String, newValue: String) {
        val currentState = _stateBoolean.value
        if (currentState is FlagChangeUiStates.Success) {
            val updatedData = currentState.data.toMutableMap()
            updatedData[flagName] = newValue
            _stateBoolean.value = currentState.copy(data = updatedData)
            listBoolFiltered.replace(flagName, newValue)
        }
    }

    fun updateIntFlagValue(flagName: String, newValue: String) {
        val currentState = _stateInteger.value
        if (currentState is FlagChangeUiStates.Success) {
            val updatedData = currentState.data.toMutableMap()
            updatedData[flagName] = newValue
            _stateInteger.value = currentState.copy(data = updatedData)
            listIntFiltered.replace(flagName, newValue)
        }
    }

    fun updateFloatFlagValue(flagName: String, newValue: String) {
        val currentState = _stateFloat.value
        if (currentState is FlagChangeUiStates.Success) {
            val updatedData = currentState.data.toMutableMap()
            updatedData[flagName] = newValue
            _stateFloat.value = currentState.copy(data = updatedData)
            listFloatFiltered.replace(flagName, newValue)
        }
    }

    fun updateStringFlagValue(flagName: String, newValue: String) {
        val currentState = _stateString.value
        if (currentState is FlagChangeUiStates.Success) {
            val updatedData = currentState.data.toMutableMap()
            updatedData[flagName] = newValue
            _stateString.value = currentState.copy(data = updatedData)
            listStringFiltered.replace(flagName, newValue)
        }
    }

    fun Map<String, String>.filterByEnabled(): Map<String, String> {
        val filteredMap = mutableMapOf<String, String>()
        for ((key, value) in this) {
            if (value == "1") {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    fun Map<String, String>.filterByDisabled(): Map<String, String> {
        val filteredMap = mutableMapOf<String, String>()
        for ((key, value) in this) {
            if (value == "0") {
                filteredMap[key] = value
            }
        }
        return filteredMap
    }

    private val changedFilterBoolList = mutableMapOf<String, String>()
    private val usersList = mutableListOf<String>()

    // Search
    var searchQuery = mutableStateOf("")

    private val listBoolFiltered: MutableMap<String, String> = mutableMapOf()
    private val listIntFiltered: MutableMap<String, String> = mutableMapOf()
    private val listFloatFiltered: MutableMap<String, String> = mutableMapOf()
    private val listStringFiltered: MutableMap<String, String> = mutableMapOf()

    init {
        usersList.addAll(repository.getUsers())
        initBoolValues()
        initIntValues()
        initFloatValues()
        initStringValues()
    }

    // Boolean
    fun initBoolValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initBoolValues()")
                repository.getBoolFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeUiStates.Success -> {
                            Log.e("bool", "Success INIT")
                            listBoolFiltered.putAll(uiStates.data)
                            getBoolFlags()
                        }

                        is FlagChangeUiStates.Loading -> {
                            Log.e("bool", "Loading INIT")
                            _stateBoolean.value = FlagChangeUiStates.Loading
                        }

                        is FlagChangeUiStates.Error -> {
                            _stateBoolean.value = FlagChangeUiStates.Error()
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
                    _stateBoolean.value = FlagChangeUiStates.Success(
                        (listBoolFiltered.toMap().filterByEnabled()).filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeUiStates.Error()
                }

                FilterMethod.DISABLED -> {
                    _stateBoolean.value = FlagChangeUiStates.Success(
                        (listBoolFiltered.toMap().filterByDisabled()).filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeUiStates.Error()
                }

                FilterMethod.CHANGED -> {
                    _stateBoolean.value = FlagChangeUiStates.Success(
                        (changedFilterBoolList.toMap().filterByEnabled()).filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeUiStates.Error()
                }

                else -> {
                    _stateBoolean.value = FlagChangeUiStates.Success(
                        listBoolFiltered.filter {
                            it.key.contains(searchQuery.value, ignoreCase = true)
                        }
                    )
                    if ((_stateBoolean.value as FlagChangeUiStates.Success).data.isEmpty()) _stateBoolean.value =
                        FlagChangeUiStates.Error()
                }
            }
        }
    }

    // Integer
    fun initIntValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initIntValues()")
                repository.getIntFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeUiStates.Success -> {
                            listIntFiltered.putAll(uiStates.data)
                            getIntFlags()
                        }

                        is FlagChangeUiStates.Loading -> {
                            _stateInteger.value = FlagChangeUiStates.Loading
                        }

                        is FlagChangeUiStates.Error -> {
                            _stateInteger.value = FlagChangeUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getIntFlags() {
        _stateInteger.value = FlagChangeUiStates.Success(
            listIntFiltered.filter {
                it.key.contains(searchQuery.value, ignoreCase = true)
            }
        )
        if ((_stateInteger.value as FlagChangeUiStates.Success).data.isEmpty()) _stateInteger.value =
            FlagChangeUiStates.Error()
    }

    // Float
    fun initFloatValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initFloatValues()")
                repository.getFloatFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeUiStates.Success -> {
                            listFloatFiltered.putAll(uiStates.data)
                            getFloatFlags()
                        }

                        is FlagChangeUiStates.Loading -> {
                            _stateFloat.value = FlagChangeUiStates.Loading
                        }

                        is FlagChangeUiStates.Error -> {
                            _stateInteger.value = FlagChangeUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getFloatFlags() {
        _stateFloat.value = FlagChangeUiStates.Success(
            listFloatFiltered.filter {
                it.key.contains(searchQuery.value, ignoreCase = true)
            }
        )
        if ((_stateFloat.value as FlagChangeUiStates.Success).data.isEmpty()) _stateFloat.value =
            FlagChangeUiStates.Error()
    }

    // String
    fun initStringValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.e("bool", "initStringValues()")
                repository.getStringFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is FlagChangeUiStates.Success -> {
                            listStringFiltered.putAll(uiStates.data)
                            getStringFlags()
                        }

                        is FlagChangeUiStates.Loading -> {
                            _stateString.value = FlagChangeUiStates.Loading
                        }

                        is FlagChangeUiStates.Error -> {
                            _stateInteger.value = FlagChangeUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getStringFlags() {
        _stateString.value = FlagChangeUiStates.Success(
            listStringFiltered.filter {
                it.key.contains(searchQuery.value, ignoreCase = true)
            }
        )
        if ((_stateString.value as FlagChangeUiStates.Success).data.isEmpty()) _stateString.value =
            FlagChangeUiStates.Error()
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

    // Override Flag
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
        clearPhenotypeCache(pkgName)
    }

    // Delete overridden flags
    fun deleteOverriddenFlagByPackage(packageName: String) {
        repository.deleteOverriddenFlagByPackage(packageName)
    }

    fun updateListAfterDelete() {

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