package ua.polodarb.gmsflags.ui.screens.suggestions

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
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
import ua.polodarb.gmsflags.data.remote.flags.dto.FlagType
import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlagTypes
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.data.repo.interactors.GmsDBInteractor
import ua.polodarb.gmsflags.data.repo.mappers.MergeFlagsMapper
import ua.polodarb.gmsflags.data.repo.mappers.MergedAllTypesFlags
import ua.polodarb.gmsflags.data.repo.mappers.MergedAllTypesOverriddenFlags
import ua.polodarb.gmsflags.ui.screens.UiStates
import java.io.File
import java.util.Collections

typealias SuggestionsScreenUiState = UiStates<SuggestedFlag>

class SuggestionScreenViewModel(
    private val application: Application,
    private val repository: GmsDBRepository,
    private val appsRepository: AppsListRepository,
    private val flagsApiService: FlagsApiService,
    private val mapper: MergeFlagsMapper,
    private val interactor: GmsDBInteractor
) : ViewModel() {

    private val gmsApplication = application as GMSApplication

    private val _stateSuggestionsFlags =
        MutableStateFlow<SuggestionsScreenUiState>(UiStates.Loading())
    val stateSuggestionsFlags: StateFlow<SuggestionsScreenUiState> =
        _stateSuggestionsFlags.asStateFlow()

    private val usersList = Collections.synchronizedList(mutableListOf<String>())

    private var rawSuggestedFlag: SuggestedFlagTypes = SuggestedFlagTypes(
        primary = emptyList(),
        secondary = emptyList()
    )

    init {
        initUsers()
        getSuggestedFlags()
        initGmsPackages()
    }

    private fun initGmsPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appsRepository.getAllInstalledApps().collectLatest { }
            }
        }
    }

    fun updatePrimaryFlagValue(newValue: Boolean, index: Int) {
        val currentState = _stateSuggestionsFlags.value
        if (currentState is UiStates.Success) {
            val updatedData = currentState.data.primary.toMutableList()
            if (index != -1) {
                updatedData[index] = updatedData[index].copy(enabled = newValue)
                _stateSuggestionsFlags.value =
                    currentState.copy(data = SuggestedFlag(updatedData, currentState.data.secondary))
            }
        }
    }

    fun updateSecondaryFlagValue(newValue: Boolean, index: Int) {
        val currentState = _stateSuggestionsFlags.value
        if (currentState is UiStates.Success) {
            val updatedData = currentState.data.secondary.toMutableList()
            if (index != -1) {
                updatedData[index] = updatedData[index].copy(enabled = newValue)
                _stateSuggestionsFlags.value =
                    currentState.copy(data = SuggestedFlag(currentState.data.primary, updatedData))
            }
        }
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

    private var overriddenFlags = mutableMapOf<String, MergedAllTypesOverriddenFlags>()

    fun getSuggestedFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val primaryList: MutableSet<PrimarySuggestedFlag> = mutableSetOf()
                val secondaryList: MutableSet<SecondarySuggestedFlag> = mutableSetOf()

                if (rawSuggestedFlag.primary.isEmpty() || rawSuggestedFlag.secondary.isEmpty()) {
                    rawSuggestedFlag = loadSuggestedFlags()
                }

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

                        if (primaryList.isEmpty() || secondaryList.isEmpty()) {
                            rawSuggestedFlag.primary.mapIndexed { index, flag ->

                                val minAndroidSdkCode = flag.minAndroidSdkCode
                                val minVersionCode = flag.minVersionCode
                                val appVersionCode = appsRepository.getAppVersionCode(flag.appPackage)

                                if (flag.enabled && appVersionCode != -1L && (minAndroidSdkCode == null || Build.VERSION.SDK_INT >= minAndroidSdkCode) &&
                                    (minVersionCode == null || minVersionCode <= appVersionCode)
                                ) {
                                    primaryList.add(
                                        PrimarySuggestedFlag(
                                            flag = flag,
                                            enabled = flag.flags.firstOrNull {
                                                overriddenFlags[flag.flagPackage]?.boolFlag?.get(it.tag) != it.value &&
                                                        overriddenFlags[flag.flagPackage]?.intFlag?.get(
                                                            it.tag
                                                        ) != it.value &&
                                                        overriddenFlags[flag.flagPackage]?.floatFlag?.get(
                                                            it.tag
                                                        ) != it.value &&
                                                        overriddenFlags[flag.flagPackage]?.stringFlag?.get(
                                                            it.tag
                                                        ) != it.value
                                            } == null
                                        )
                                    )
                                }
                            }
                            rawSuggestedFlag.secondary.mapIndexed { index, flag ->

                                val minAndroidSdkCode = flag.minAndroidSdkCode
                                val minVersionCode = flag.minVersionCode
                                val appVersionCode = appsRepository.getAppVersionCode(flag.appPackage)

                                if (flag.enabled &&  appVersionCode != -1L && (minAndroidSdkCode == null || Build.VERSION.SDK_INT >= minAndroidSdkCode) &&
                                    (minVersionCode == null || minVersionCode <= appVersionCode)
                                ) {
                                    secondaryList.add(
                                        SecondarySuggestedFlag(
                                            flag = flag,
                                            enabled = flag.flags.firstOrNull {
                                                overriddenFlags[flag.flagPackage]?.boolFlag?.get(it.tag) != it.value &&
                                                        overriddenFlags[flag.flagPackage]?.intFlag?.get(it.tag) != it.value &&
                                                        overriddenFlags[flag.flagPackage]?.floatFlag?.get(it.tag) != it.value &&
                                                        overriddenFlags[flag.flagPackage]?.stringFlag?.get(it.tag) != it.value
                                            } == null
                                        )
                                    )
                                }

                            }
                        }

                        val data = SuggestedFlag(
                            primary = primaryList.toSet().toImmutableList(),
                            secondary = secondaryList.toSet().toImmutableList()
                        )
                        _stateSuggestionsFlags.value = UiStates.Success(
                            data = data
                        )

                    }
                }
            }
        }
    }

    private suspend fun loadSuggestedFlags(): SuggestedFlagTypes {
        try {
            val localFlags =
                File(gmsApplication.filesDir.absolutePath + File.separator + "suggestedFlags_2.0.json")

            val flags = flagsApiService.getSuggestedFlags()
            if (flags is Resource.Success && flags.data != null) {
                localFlags.writeText(Json.encodeToString(flags.data))
                return flags.data
            }

            try {
                if (localFlags.exists())
                    return Json.decodeFromString(localFlags.readText())
            } catch (_: Exception) {
            }

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

    fun overrideSuggestedFlags(
        flags: List<FlagInfo>,
        packageName: String,
        index: Int,
        newBoolValue: Boolean,
        flagType: SuggestedUIFlagTypes
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                when (flagType) {
                    SuggestedUIFlagTypes.PRIMARY -> {
                        updatePrimaryFlagValue(newBoolValue, index)
                    }
                    SuggestedUIFlagTypes.SECONDARY -> {
                        updateSecondaryFlagValue(newBoolValue, index)
                    }
                }
                flags.forEach { flag ->
                    when (flag.type) {
                        FlagType.BOOL -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.tag,
                                boolVal = if (newBoolValue) flag.value else "0"
                            )
                        }

                        FlagType.INTEGER -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.tag,
                                intVal = if (newBoolValue) flag.value else "0"
                            )
                        }

                        FlagType.FLOAT -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.tag,
                                floatVal = if (newBoolValue) flag.value else "0"
                            )
                        }

                        FlagType.STRING -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.tag,
                                stringVal = if (newBoolValue) flag.value else ""
                            )
                        }
                    }
                }
            }
        }
    }

    fun resetSuggestedFlagValue(packageName: String, flags: List<FlagInfo>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                flags.forEach {
                    repository.deleteRowByFlagName(packageName, it.tag)
                }
            }
            getSuggestedFlags()
        }
    }

}

enum class SuggestedUIFlagTypes {
    PRIMARY,
    SECONDARY
}
