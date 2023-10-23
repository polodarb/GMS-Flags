package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.utils.Extensions.toSortMap
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.data.repo.RoomDBRepository
import ua.polodarb.gmsflags.ui.screens.UiStates
import java.util.Collections

typealias FlagChangeUiStates = UiStates<Map<String, String>>

class FlagChangeScreenViewModel(
    private val pkgName: String,
    private val repository: GmsDBRepository,
    private val roomRepository: RoomDBRepository
) : ViewModel() {

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
        MutableStateFlow<List<SavedFlags>>(emptyList())
    val stateSavedFlags: StateFlow<List<SavedFlags>> = _stateSavedFlags.asStateFlow()

    // ProgressDialog
    val showProgressDialog = mutableStateOf(false)
    fun showFalseProgressDialog(customCount: Int? = null) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (stateBoolean.value) {
                    is UiStates.Success -> {
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

    // Filter
    var filterMethod = mutableStateOf(FilterMethod.ALL)

    fun updateBoolFlagValues(flagNames: List<String>, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentState = _stateBoolean.value
                if (currentState is UiStates.Success) {
                    val updatedData = currentState.data.toMutableMap()

                    for (flagName in flagNames) {
                        updatedData[flagName] = newValue
                        listBoolFiltered[flagName] = newValue
                    }

                    _stateBoolean.value = currentState.copy(data = updatedData.toSortMap())
                }
            }
        }
    }


    fun turnOnAllBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentState = _stateBoolean.value
                if (currentState is UiStates.Success) {
                    val updatedData = currentState.data.mapValues { "1" }.toMutableMap()
                    _stateBoolean.value = currentState.copy(data = updatedData.toSortMap())
                    listBoolFiltered.replaceAll { _, _ -> "1" }
                }
            }
        }
    }

    fun turnOffAllBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentState = _stateBoolean.value
                if (currentState is UiStates.Success) {
                    val updatedData = currentState.data.mapValues { "0" }.toMutableMap()
                    _stateBoolean.value = currentState.copy(data = updatedData.toSortMap())
                    listBoolFiltered.replaceAll { _, _ -> "0" }
                }
            }
        }
    }

    fun updateIntFlagValue(flagName: String, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentState = _stateInteger.value
                if (currentState is UiStates.Success) {
                    val updatedData = currentState.data.toMutableMap()
                    updatedData[flagName] = newValue
                    _stateInteger.value = currentState.copy(data = updatedData.toSortMap())
                    listIntFiltered.replace(flagName, newValue)
                }
            }
        }
    }

    fun updateFloatFlagValue(flagName: String, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentState = _stateFloat.value
                if (currentState is UiStates.Success) {
                    val updatedData = currentState.data.toMutableMap()
                    updatedData[flagName] = newValue
                    _stateFloat.value = currentState.copy(data = updatedData.toSortMap())
                    listFloatFiltered.replace(flagName, newValue)
                }
            }
        }
    }

    fun updateStringFlagValue(flagName: String, newValue: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val currentState = _stateString.value
                if (currentState is UiStates.Success) {
                    val updatedData = currentState.data.toMutableMap()
                    updatedData[flagName] = newValue
                    _stateString.value = currentState.copy(data = updatedData.toSortMap())
                    listStringFiltered.replace(flagName, newValue)
                }
            }
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

    fun getAndroidPackage(pkgName: String): String {
        return repository.androidPackage(pkgName)
    }

    private val changedFilterBoolList = mutableMapOf<String, String>()
    private val usersList = mutableListOf<String>()

    // Search
    var searchQuery = mutableStateOf("")

    private val listBoolFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val listIntFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val listFloatFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())
    private val listStringFiltered = Collections.synchronizedMap(mutableMapOf<String, String>())

    init {
        viewModelScope.launch() {
            withContext(Dispatchers.IO) {
                repository.getUsers().collect {
                    usersList.addAll(it)
                }
            }
        }
        getAllSavedFlags()
        initBoolValues()
        initIntValues()
        initFloatValues()
        initStringValues()
    }

    fun initOverriddenBoolFlags(pkgName: String, delay: Boolean = false) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (val data = repository.getOverriddenBoolFlagsByPackage(pkgName)) {
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

    // Boolean
    fun initBoolValues(delay: Boolean = true) {
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

    fun getBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (filterMethod.value) {
                    FilterMethod.ENABLED -> {
                        _stateBoolean.value = UiStates.Success(
                            (listBoolFiltered.toMap().filterByEnabled()).filter {
                                it.key.contains(searchQuery.value, ignoreCase = true)
                            }.toSortMap()
                        )
                    }

                    FilterMethod.DISABLED -> {
                        _stateBoolean.value = UiStates.Success(
                            (listBoolFiltered.toMap().filterByDisabled()).filter {
                                it.key.contains(searchQuery.value, ignoreCase = true)
                            }.toSortMap()
                        )
                    }

                    FilterMethod.CHANGED -> {
                        _stateBoolean.value = UiStates.Success(
                            changedFilterBoolList.filter {
                                it.key.contains(searchQuery.value, ignoreCase = true)
                            }.toSortMap()
                        )
                    }

                    else -> {
                        _stateBoolean.value = UiStates.Success(
                            listBoolFiltered.filter {
                                it.key.contains(searchQuery.value, ignoreCase = true)
                            }.toSortMap()
                        )
                    }
                }
            }
        }
    }

    // Integer
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

    fun getIntFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateInteger.value = UiStates.Success(
                    listIntFiltered.filter {
                        it.key.contains(searchQuery.value, ignoreCase = true)
                    }.toSortMap()
                )
            }
        }
    }

    // Float
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

    fun getFloatFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateFloat.value = UiStates.Success(
                    listFloatFiltered.filter {
                        it.key.contains(searchQuery.value, ignoreCase = true)
                    }.toSortMap()
                )
            }
        }
    }

    // String
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

    fun getStringFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _stateString.value = UiStates.Success(
                    listStringFiltered.filter {
                        it.key.contains(searchQuery.value, ignoreCase = true)
                    }.toSortMap()
                )
            }
        }
    }

    fun clearPhenotypeCache(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val androidPkgName = repository.androidPackage(pkgName)
                Shell.cmd("am force-stop $androidPkgName").exec()
                Shell.cmd("rm -rf /data/data/$androidPkgName/files/phenotype").exec()
                if (pkgName.contains("finsky") || pkgName.contains("vending")) {
                    Shell.cmd("rm -rf /data/data/com.android.vending/files/experiment*").exec()
                    Shell.cmd("am force-stop com.android.vending").exec()
                }
                if (pkgName.contains("com.google.android.apps.photos")) {
                    Shell.cmd("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/phenotype*")
                        .exec()
                    Shell.cmd("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/com.google.android.apps.photos.phenotype.xml")
                        .exec()
                    Shell.cmd("am force-stop com.google.android.apps.photos").exec()
                }
                repeat(3) {
                    Shell.cmd("am start -a android.intent.action.MAIN -n $androidPkgName &").exec()
                    Shell.cmd("am force-stop $androidPkgName").exec()
                }
            }
        }
    }

    // Override Flag
    suspend fun overrideFlag(
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
        if (clearData) clearPhenotypeCache(pkgName)
    }

    fun overrideAllFlag() { // todo
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                listBoolFiltered.forEach {
                    if (it.value == "0") {
                        overrideFlag(
                            packageName = pkgName,
                            it.key,
                            boolVal = "1"
                        )
                    }
                }
                turnOnAllBoolFlags()
            }
        }
    }

    // Delete overridden flags
    fun deleteOverriddenFlagByPackage(packageName: String) {
//        viewModelScope.launch {
//            withContext(Dispatchers.IO) {
        repository.deleteOverriddenFlagByPackage(packageName)
//            }
//        }
    }

    // Saved flags

    private fun getAllSavedFlags() {
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

    // Select
    val selectedItems = mutableStateListOf<String>()

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

    fun selectAllItems() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (stateBoolean.value) {
                    is UiStates.Success -> {
                        selectedItems.clear()
                        selectedItems.addAll((stateBoolean.value as UiStates.Success<Map<String, String>>).data.keys)
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

    fun enableSelectedFlag() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (stateBoolean.value) {
                    is UiStates.Success -> {
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

    fun disableSelectedFlag() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (stateBoolean.value) {
                    is UiStates.Success -> {
                        val data =
                            (stateBoolean.value as UiStates.Success<Map<String, String>>).data
                        data.forEach { (key, value) ->
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
                        if (data.keys.size == selectedItems.size) {
                            turnOffAllBoolFlags()
                        } else {
                            updateBoolFlagValues(selectedItems, "0")
                        }
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