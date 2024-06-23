package ua.polodarb.suggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.domain.suggestedFlags.SuggestedFlagsUseCase
import ua.polodarb.domain.suggestedFlags.models.SuggestedFlagsModel
import ua.polodarb.repository.appsList.AppsListRepository
import ua.polodarb.repository.databases.gms.GmsDBInteractor
import ua.polodarb.repository.databases.gms.GmsDBRepository
import ua.polodarb.repository.suggestedFlags.models.FlagInfoRepoModel
import ua.polodarb.repository.suggestedFlags.models.FlagTypeRepoModel
import ua.polodarb.repository.uiStates.UiStates
import java.util.Collections

typealias SuggestionsScreenUiState = UiStates<List<SuggestedFlagsModel>>

class SuggestionScreenViewModel(
    private val repository: GmsDBRepository,
    private val appsRepository: AppsListRepository,
    private val interactor: GmsDBInteractor,
    private val flagsUseCase: SuggestedFlagsUseCase,
) : ViewModel() {

    private val _stateSuggestionsFlags =
        MutableStateFlow<SuggestionsScreenUiState>(UiStates.Loading())
    val stateSuggestionsFlags: StateFlow<SuggestionsScreenUiState> =
        _stateSuggestionsFlags.asStateFlow()

    private val usersList = Collections.synchronizedList(mutableListOf<String>())

    init {
        initUsers()
        initGmsPackages()
    }

    private fun initGmsPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                appsRepository.getAllInstalledApps().collectLatest { }
            }
        }
    }

    private fun updateFlagValue(newValue: Boolean, index: Int) {
        val currentState = _stateSuggestionsFlags.value
        if (currentState is UiStates.Success) {
            val updatedData = currentState.data.toMutableList()
            if (index != -1) {
                updatedData[index] = updatedData[index].copy(enabled = newValue)
                _stateSuggestionsFlags.value =
                    currentState.copy(
                        data = updatedData.map {
                            SuggestedFlagsModel(
                                it.flag,
                                it.enabled
                            )
                        }
                    )
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

    fun getSuggestedFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    flagsUseCase.invoke().collect { flags ->
                        if (!flags.isNullOrEmpty()) {
                            _stateSuggestionsFlags.value = UiStates.Success(flags)
                        } else {
                            _stateSuggestionsFlags.value = UiStates.Error()
                        }
                    }
                } catch (e: Exception) {
                    _stateSuggestionsFlags.value = UiStates.Error()
                }
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
        flags: List<FlagInfoRepoModel>,
        packageName: String,
        index: Int,
        newBoolValue: Boolean
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                updateFlagValue(newBoolValue, index)
                flags.forEach { flag ->
                    when (flag.type) {
                        FlagTypeRepoModel.BOOL -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.name,
                                boolVal = if (newBoolValue) flag.value else "0"
                            )
                        }

                        FlagTypeRepoModel.INTEGER -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.name,
                                intVal = if (newBoolValue) flag.value else "0"
                            )
                        }

                        FlagTypeRepoModel.FLOAT -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.name,
                                floatVal = if (newBoolValue) flag.value else "0"
                            )
                        }

                        FlagTypeRepoModel.STRING -> {
                            overrideFlag(
                                packageName = packageName,
                                name = flag.name,
                                stringVal = if (newBoolValue) flag.value else ""
                            )
                        }

                        FlagTypeRepoModel.EXTVAL -> {
                            // TODO
                        }
                    }
                }
            }
        }
    }

    fun resetSuggestedFlagValue(packageName: String, flags: List<FlagInfoRepoModel>) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                flags.forEach {
                    repository.deleteRowByFlagName(packageName, it.name)
                }
            }
            getSuggestedFlags()
        }
    }

}
