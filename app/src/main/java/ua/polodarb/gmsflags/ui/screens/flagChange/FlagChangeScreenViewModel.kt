package ua.polodarb.gmsflags.ui.screens.flagChange

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.data.repo.RoomDBRepository
import ua.polodarb.gmsflags.data.repo.interactors.GmsDBInteractor
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.utils.Extensions.filterByDisabled
import ua.polodarb.gmsflags.utils.Extensions.filterByEnabled
import ua.polodarb.gmsflags.utils.Extensions.toSortMap
import java.util.Collections

typealias FlagChangeUiStates = UiStates<Map<String, String>>
typealias SavedFlagsFlow = List<SavedFlags>

class FlagChangeScreenViewModel(
    private val pkgName: String,
    private val repository: GmsDBRepository,
    private val roomRepository: RoomDBRepository,
    private val gmsDBInteractor: GmsDBInteractor
) : ViewModel() {

    init {
        initUsers()
        getAndroidPackage(pkgName)
    }

    private val _stateBoolean =
        MutableStateFlow<FlagChangeUiStates>(UiStates.Loading())
    val stateBoolean: StateFlow<FlagChangeUiStates> = _stateBoolean.asStateFlow()

    private val _stateInteger =
        MutableStateFlow<FlagChangeUiStates>(UiStates.Loading())
    val stateInteger: StateFlow<FlagChangeUiStates> = _stateInteger.asStateFlow()

    private val _stateFloat =
        MutableStateFlow<FlagChangeUiStates>(UiStates.Loading())
    val stateFloat: StateFlow<FlagChangeUiStates> = _stateFloat.asStateFlow()

    private val _stateString =
        MutableStateFlow<FlagChangeUiStates>(UiStates.Loading())
    val stateString: StateFlow<FlagChangeUiStates> = _stateString.asStateFlow()

    private val _stateSavedFlags =
        MutableStateFlow<SavedFlagsFlow>(emptyList())
    val stateSavedFlags: StateFlow<SavedFlagsFlow> = _stateSavedFlags.asStateFlow()

    val androidPackage: MutableStateFlow<String> = MutableStateFlow(pkgName)


    // Filter
    var filterMethod = mutableStateOf(FilterMethod.ALL)

    // Search
    var searchQuery = mutableStateOf("")

    // Select
    val selectedItems: MutableList<String> =
        Collections.synchronizedList(mutableStateListOf<String>())

    private val changedFilterBoolList = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val changedFilterIntList = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val changedFilterFloatList = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val changedFilterStringList = Collections.synchronizedMap(mutableMapOf<String, String>())

    private val listBoolFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val listIntFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val listFloatFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val listStringFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())

    private val usersList = Collections.synchronizedList(mutableListOf<String>())

    // Initialization of flags of all types
    private fun initBoolValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getBoolFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is UiStates.Success -> {
                            listBoolFiltered.putAll(uiStates.data)
                            getBoolFlags()
                        }

                        is UiStates.Loading -> {
                            _stateBoolean.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateBoolean.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initIntValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getIntFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is UiStates.Success -> {
                            listIntFiltered.putAll(uiStates.data)
                            getIntFlags()
                        }

                        is UiStates.Loading -> {
                            _stateInteger.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateInteger.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initFloatValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getFloatFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is UiStates.Success -> {
                            listFloatFiltered.putAll(uiStates.data)
                            getFloatFlags()
                        }

                        is UiStates.Loading -> {
                            _stateFloat.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateInteger.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initStringValues(delay: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getStringFlags(pkgName, delay).collect { uiStates ->
                    when (uiStates) {
                        is UiStates.Success -> {
                            listStringFiltered.putAll(uiStates.data)
                            getStringFlags()
                        }

                        is UiStates.Loading -> {
                            _stateString.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateInteger.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initAllFlags() {
        initBoolValues()
        initIntValues()
        initFloatValues()
        initStringValues()
    }


    // Getting initialized flags
    fun getBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (filterMethod.value) {
                    FilterMethod.ENABLED -> {
                        _stateBoolean.update {
                            UiStates.Success(
                                synchronized(listBoolFiltered) {
                                    (listBoolFiltered.toMap().filterByEnabled()).filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }

                    FilterMethod.DISABLED -> {
                        _stateBoolean.update {
                            UiStates.Success(
                                synchronized(listBoolFiltered) {
                                    (listBoolFiltered.toMap().filterByDisabled()).filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }

                    FilterMethod.CHANGED -> {
                        _stateBoolean.update {
                            UiStates.Success(
                                synchronized(changedFilterBoolList) {
                                    changedFilterBoolList.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }

                    else -> {
                        _stateBoolean.update {
                            UiStates.Success(
                                synchronized(listBoolFiltered) {
                                    listBoolFiltered.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun getIntFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (filterMethod.value) {
                    FilterMethod.CHANGED -> {
                        _stateInteger.update {
                            UiStates.Success(
                                synchronized(changedFilterIntList) {
                                    changedFilterIntList.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }

                    else -> {
                        _stateInteger.update {
                            UiStates.Success(
                                synchronized(listIntFiltered) {
                                    listIntFiltered.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun getFloatFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (filterMethod.value) {
                    FilterMethod.CHANGED -> {
                        _stateFloat.update {
                            UiStates.Success(
                                synchronized(changedFilterFloatList) {
                                    changedFilterFloatList.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }

                    else -> {
                        _stateFloat.update {
                            UiStates.Success(
                                synchronized(listFloatFiltered) {
                                    listFloatFiltered.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun getStringFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (filterMethod.value) {
                    FilterMethod.CHANGED -> {
                        _stateString.update {
                            UiStates.Success(
                                synchronized(changedFilterStringList) {
                                    changedFilterStringList.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }

                    else -> {
                        _stateString.update {
                            UiStates.Success(
                                synchronized(listStringFiltered) {
                                    listStringFiltered.filter {
                                        it.key.contains(searchQuery.value, ignoreCase = true)
                                    }.toSortMap()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    fun getAllFlags() {
        getBoolFlags()
        getIntFlags()
        getFloatFlags()
        getStringFlags()
    }

    // Updating flags values
    fun updateBoolFlagValues(flagNames: List<String>, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateBoolean.update {
                    val currentState = it
                    if (currentState is UiStates.Success) {
                        val updatedData = currentState.data.toMutableMap()

                        for (flagName in flagNames) {
                            updatedData[flagName] = newValue
                            listBoolFiltered[flagName] = newValue
                        }

                        currentState.copy(data = updatedData.toSortMap())
                    } else {
                        currentState
                    }
                }
            }
        }
    }

    fun updateIntFlagValue(flagName: String, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateInteger.update {
                    val currentState = it
                    if (currentState is UiStates.Success) {
                        val updatedData = currentState.data.toMutableMap()
                        updatedData[flagName] = newValue
                        currentState.copy(data = updatedData.toSortMap())
                    } else {
                        currentState
                    }
                }

                listIntFiltered.replace(flagName, newValue)
            }
        }
    }

    fun updateFloatFlagValue(flagName: String, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateFloat.update {
                    val currentState = it
                    if (currentState is UiStates.Success) {
                        val updatedData = currentState.data.toMutableMap()
                        updatedData[flagName] = newValue
                        currentState.copy(data = updatedData.toSortMap())
                    } else {
                        currentState
                    }
                }

                listFloatFiltered.replace(flagName, newValue)
            }
        }
    }

    fun updateStringFlagValue(flagName: String, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateString.update {
                    val currentState = it
                    if (currentState is UiStates.Success) {
                        val updatedData = currentState.data.toMutableMap()
                        updatedData[flagName] = newValue
                        currentState.copy(data = updatedData.toSortMap())
                    } else {
                        currentState
                    }
                }

                listStringFiltered.replace(flagName, newValue)
            }
        }
    }


    // Get overridden flags
    fun initOverriddenBoolFlags(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getOverriddenBoolFlagsByPackage(pkgName).collect {
                    when (val data = it) {
                        is UiStates.Success -> {
                            changedFilterBoolList.clear()
                            changedFilterBoolList.putAll(data.data)
                            listBoolFiltered.putAll(data.data)
                        }

                        is UiStates.Loading -> {
                            _stateBoolean.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateBoolean.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initOverriddenIntFlags(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getOverriddenIntFlagsByPackage(pkgName).collect {
                    when (val data = it) {
                        is UiStates.Success -> {
                            changedFilterIntList.clear()
                            changedFilterIntList.putAll(data.data)
                            listIntFiltered.putAll(data.data)
                        }

                        is UiStates.Loading -> {
                            _stateInteger.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateInteger.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initOverriddenFloatFlags(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getOverriddenFloatFlagsByPackage(pkgName).collect {
                    when (val data = it) {
                        is UiStates.Success -> {
                            changedFilterFloatList.clear()
                            changedFilterFloatList.putAll(data.data)
                            listFloatFiltered.putAll(data.data)
                        }

                        is UiStates.Loading -> {
                            _stateFloat.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateFloat.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initOverriddenStringFlags(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getOverriddenStringFlagsByPackage(pkgName).collect {
                    when (val data = it) {
                        is UiStates.Success -> {
                            changedFilterStringList.clear()
                            changedFilterStringList.putAll(data.data)
                            listStringFiltered.putAll(data.data)
                        }

                        is UiStates.Loading -> {
                            _stateString.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _stateString.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun initAllOverriddenFlagsByPackage(pkgName: String) {
        initOverriddenBoolFlags(pkgName)
        initOverriddenIntFlags(pkgName)
        initOverriddenFloatFlags(pkgName)
        initOverriddenStringFlags(pkgName)
    }

    // Manually add bool flag
    fun addManuallyBoolFlag(flagName: String, flagValue: String) {
        viewModelScope.launch {
            val currentState = _stateBoolean.value
            if (currentState is UiStates.Success) {
                val updatedData = currentState.data.toMutableMap()
                updatedData[flagName] = flagValue
                _stateBoolean.value = currentState.copy(data = updatedData.toSortMap())
            }
        }
    }

    fun addManuallyIntFlag(flagName: String, flagValue: String) {
        viewModelScope.launch {
            val currentState = _stateInteger.value
            if (currentState is UiStates.Success) {
                val updatedData = currentState.data.toMutableMap()
                updatedData[flagName] = flagValue
                _stateInteger.value = currentState.copy(data = updatedData.toSortMap())
            }
        }
    }

    fun addManuallyFloatFlag(flagName: String, flagValue: String) {
        viewModelScope.launch {
            val currentState = _stateFloat.value
            if (currentState is UiStates.Success) {
                val updatedData = currentState.data.toMutableMap()
                updatedData[flagName] = flagValue
                _stateFloat.value = currentState.copy(data = updatedData.toSortMap())
            }
        }
    }

    fun addManuallyStringFlag(flagName: String, flagValue: String) {
        viewModelScope.launch {
            val currentState = _stateString.value
            if (currentState is UiStates.Success) {
                val updatedData = currentState.data.toMutableMap()
                updatedData[flagName] = flagValue
                _stateString.value = currentState.copy(data = updatedData.toSortMap())
            }
        }
    }

    // Enable/disable all Bool flags
    private fun turnOnAllBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateBoolean.update {
                    val currentState = it
                    if (currentState is UiStates.Success) {
                        val updatedData = currentState.data.mapValues { "1" }.toMutableMap()
                        currentState.copy(data = updatedData.toSortMap())
                    } else {
                        currentState
                    }
                }

                listBoolFiltered.replaceAll { _, _ -> "1" }
            }
        }
    }

    private fun turnOffAllBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateBoolean.update {
                    val currentState = it
                    if (currentState is UiStates.Success) {
                        val updatedData = currentState.data.mapValues { "0" }.toMutableMap()
                        currentState.copy(data = updatedData.toSortMap())
                    } else {
                        currentState
                    }
                }

                listBoolFiltered.replaceAll { _, _ -> "0" }
            }
        }
    }


    // Enable/disable selected Bool flags
    fun enableSelectedFlag() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                listBoolFiltered.forEach { (key, value) ->
                    if (selectedItems.contains(key) && value == "0") {
                        overrideFlag(
                            packageName = pkgName,
                            key,
                            boolVal = "1",
                            clearData = false
                        )
                    }
                }
                clearPhenotypeCache(pkgName)
                if ((stateBoolean.value as UiStates.Success<Map<String, String>>).data.keys.size == selectedItems.size) {
                    turnOnAllBoolFlags()
                } else {
                    updateBoolFlagValues(selectedItems, "1")
                }
            }
        }
    }

    fun disableSelectedFlag() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                listBoolFiltered.forEach { (key, value) ->
                    if (selectedItems.contains(key) && value == "1") {
                        overrideFlag(
                            packageName = pkgName,
                            key,
                            boolVal = "0",
                            clearData = false
                        )
                    }
                }
                clearPhenotypeCache(pkgName)
                if ((stateBoolean.value as UiStates.Success<Map<String, String>>).data.keys.size == selectedItems.size) {
                    turnOffAllBoolFlags()
                } else {
                    updateBoolFlagValues(selectedItems, "0")
                }
            }
        }
    }

    // Init users
    private fun initUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getUsers().collect {
                    usersList.addAll(it)
                }
            }
        }
    }


    // Get original Android package
    fun getAndroidPackage(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.androidPackage(pkgName).collect {
                    androidPackage.value = it
                }
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
        committed: Int = 0,
        clearData: Boolean = true
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gmsDBInteractor.overrideFlag(
                    packageName = packageName,
                    name = name,
                    flagType = flagType,
                    intVal = intVal,
                    boolVal = boolVal,
                    floatVal = floatVal,
                    stringVal = stringVal,
                    extensionVal = extensionVal,
                    committed = committed,
                    clearData = clearData,
                    usersList = usersList
                )
            }
            changedFilterBoolList[name] = boolVal
            changedFilterIntList[name] = intVal
            changedFilterFloatList[name] = floatVal
            changedFilterStringList[name] = stringVal
        }
    }

    fun clearPhenotypeCache(pkgName: String) {
        viewModelScope.launch {
            gmsDBInteractor.clearPhenotypeCache(pkgName)
        }
    }


    // Delete overridden flags
    fun deleteOverriddenFlagByPackage(packageName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteOverriddenFlagByPackage(packageName)
            }
        }
    }

    // Reset int/float/string flags to default value
    fun resetOtherTypesFlagsToDefault(flag: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteRowByFlagName(
                    packageName = pkgName,
                    name = flag
                )
            }
        }
    }

    // Saved flags in local DB
    fun getAllSavedFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.getSavedFlags().collect {
                    _stateSavedFlags.value = it
                }
            }
        }
    }

    fun saveFlag(flagName: String, pkgName: String, flagType: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.saveFlag(flagName, pkgName, flagType)
            }
        }
    }

    fun deleteSavedFlag(flagName: String, pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.deleteSavedFlag(flagName, pkgName)
            }
        }
    }

    fun saveSelectedFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                selectedItems.forEach {
                    saveFlag(
                        it,
                        pkgName,
                        SelectFlagsType.BOOLEAN.name
                    )
                }
            }
        }
    }


    // Select all items by one click
    fun selectAllItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val result = stateBoolean.value) {
                    is UiStates.Success -> {
                        selectedItems.clear()
                        selectedItems.addAll(result.data.keys)
                    }

                    is UiStates.Loading -> {
                        _stateBoolean.value = UiStates.Loading()
                    }

                    is UiStates.Error -> {
                        _stateBoolean.value = UiStates.Error()
                    }
                }
            }
        }
    }


    // ProgressDialog
    val showProgressDialog = mutableStateOf(false)
    fun showFalseProgressDialog(customCount: Int? = null) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (stateBoolean.value) {
                    is UiStates.Success -> {
                        showProgressDialog.value = true
                        val flagsCount = customCount
                            ?: (stateBoolean.value as UiStates.Success<Map<String, String>>).data.keys.size
                        delay(
                            when {
                                flagsCount <= 50 -> 0
                                flagsCount in 51..150 -> 3000
                                flagsCount in 151..500 -> 5000
                                flagsCount in 501..1000 -> 7000
                                flagsCount in 1001..1500 -> 9000
                                flagsCount > 1501 -> 10000
                                else -> 0
                            }
                        )
                        showProgressDialog.value = false
                    }

                    is UiStates.Loading -> {
                        _stateBoolean.value = UiStates.Loading()
                    }

                    is UiStates.Error -> {
                        _stateBoolean.value = UiStates.Error()
                    }
                }
            }
        }
    }

    fun resetFilterLists() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                listBoolFiltered.clear()
                listIntFiltered.clear()
                listFloatFiltered.clear()
                listStringFiltered.clear()
                changedFilterBoolList.clear()
                changedFilterIntList.clear()
                changedFilterFloatList.clear()
                changedFilterStringList.clear()
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
