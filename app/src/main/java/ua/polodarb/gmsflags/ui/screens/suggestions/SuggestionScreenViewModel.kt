package ua.polodarb.gmsflags.ui.screens.suggestions

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.remote.Resource
import ua.polodarb.gmsflags.data.remote.flags.FlagsApiService
import ua.polodarb.gmsflags.data.remote.flags.dto.FlagInfo
import ua.polodarb.gmsflags.data.remote.flags.dto.NewFlagInfo
import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlagTypes
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.data.repo.interactors.GmsDBInteractor
import ua.polodarb.gmsflags.data.repo.mappers.MergeOverriddenFlagsInteractor
import ua.polodarb.gmsflags.data.repo.mappers.MergedOverriddenFlag
import ua.polodarb.gmsflags.ui.screens.UiStates
import java.io.File
import java.util.Collections

typealias SuggestionsScreenUiState = UiStates<NewSuggestedFlag>

class SuggestionScreenViewModel(
    private val application: Application,
    private val repository: GmsDBRepository,
    private val appsRepository: AppsListRepository,
    private val flagsApiService: FlagsApiService,
    private val mapper: MergeOverriddenFlagsInteractor,
    private val interactor: GmsDBInteractor
) : ViewModel() {
    private val gmsApplication = application as GMSApplication

    private val _stateSuggestionsFlags =
        MutableStateFlow<SuggestionsScreenUiState>(UiStates.Loading())
    val stateSuggestionsFlags: StateFlow<SuggestionsScreenUiState> = _stateSuggestionsFlags.asStateFlow()

    private val usersList  = Collections.synchronizedList(mutableListOf<String>())

    private var rawSuggestedFlag = SuggestedFlagTypes(
        primary = emptyList(),
        secondary = emptyList(),
    )

    init {
        initUsers()
        getPrimaryAllOverriddenBoolFlags()
        getSecondaryAllOverriddenBoolFlags()
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
//        val currentState = _stateSuggestionsFlags.value
//        if (currentState is UiStates.Success) {
//            val updatedData = currentState.data.flag.secondary.toMutableList()
//            if (index != -1) {
//                updatedData[index] = updatedData[index].copy(enabled = newValue)
//                _stateSuggestionsFlags.value = currentState.copy(data = updatedData)
//            }
//        }
    }

    private fun initUsers() {
        usersList.clear()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getUsers().collect {
                    usersList.addAll(it)
                }
            }
        }
    }

    private var overriddenFlags = mutableMapOf<String, MergedOverriddenFlag>()

    fun getPrimaryAllOverriddenBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                    if (rawSuggestedFlag.primary.isEmpty() || rawSuggestedFlag.secondary.isEmpty())
                        rawSuggestedFlag = loadSuggestedFlags()

                    gmsApplication.databaseInitializationStateFlow.collect { status ->
                        if (status.isInitialized) {
                            overriddenFlags = mutableMapOf()
                            rawSuggestedFlag.primary.map { it.flagPackage }.forEach { pkg ->
                                if (overriddenFlags[pkg] == null) {
                                    overriddenFlags[pkg] = mapper.getMergedOverriddenFlagsByPackage(pkg)
                                }
                            }
                            rawSuggestedFlag.secondary.map { it.flagPackage }.forEach { pkg ->
                                if (overriddenFlags[pkg] == null) {
                                    overriddenFlags[pkg] = mapper.getMergedOverriddenFlagsByPackage(pkg)
                                }
                            }
                            _stateSuggestionsFlags.value = UiStates.Success(
                                data = NewSuggestedFlag(
                                    flag = rawSuggestedFlag,
                                    enabled = false
                                )
//                                rawSuggestedFlag.primary.map { flag ->
//                                NewSuggestedFlag(
//                                    flag = flag,
//                                    enabled = flag.flags.firstOrNull {
//                                        overriddenFlags[flag.packageName]?.boolFlag?.get(it.tag) != it.value &&
//                                                overriddenFlags[flag.packageName]?.intFlag?.get(it.tag) != it.value &&
//                                                overriddenFlags[flag.packageName]?.floatFlag?.get(it.tag) != it.value &&
//                                                overriddenFlags[flag.packageName]?.stringFlag?.get(it.tag) != it.value
//                                    } == null
//                                )
//                            }
                            )
                        }
                    }
            }
        }
    }

    fun getSecondaryAllOverriddenBoolFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                if (rawSuggestedFlag.secondary.isEmpty())
                    rawSuggestedFlag = loadSuggestedFlags()

                gmsApplication.databaseInitializationStateFlow.collect { status ->
                    if (status.isInitialized) {
                        overriddenFlags = mutableMapOf()
                        rawSuggestedFlag.secondary.map { it.flagPackage }.forEach { pkg ->
                            if (overriddenFlags[pkg] == null) {
                                overriddenFlags[pkg] = mapper.getMergedOverriddenFlagsByPackage(pkg)
                            }
                        }
                        _stateSuggestionsFlags.value = UiStates.Success(
                            data = NewSuggestedFlag(
                                flag = rawSuggestedFlag,
                                enabled = false
                            )
                        )
                    }
                }
            }
        }
    }

    private suspend fun loadSuggestedFlags(): SuggestedFlagTypes {
        try {
            val localFlags = File(gmsApplication.filesDir.absolutePath + File.separator + "suggestedFlags_2.0.json")

            val flags = flagsApiService.getSuggestedFlags()
            if (flags is Resource.Success && flags.data != null) {
                localFlags.writeText(Json.encodeToString(flags.data))
                return flags.data
            }

            try {
                if (localFlags.exists())
                    return Json.decodeFromString(localFlags.readText())
            } catch (_: Exception) { }

            val pkgFlags = application.assets.open("suggestedFlags_2.0.json")
            val pkgContent = pkgFlags.bufferedReader().use { it.readText() }
            localFlags.writeText(pkgContent)
            return Json.decodeFromString(pkgContent)
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
            _stateSuggestionsFlags.value = UiStates.Error(e)
            return SuggestedFlagTypes(emptyList(), emptyList())
        }

    }

    private fun clearPhenotypeCache(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                interactor.clearPhenotypeCache(pkgName)
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
        committed: Int = 0,
        clearData: Boolean = true
    ) {
        interactor.overrideFlag(
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

    fun resetSuggestedFlagValue(packageName: String, flags: List<NewFlagInfo>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                flags.forEach {
                    repository.deleteRowByFlagName(packageName, it.tag)
                }
            }
        }
    }

}

