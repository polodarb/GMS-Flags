package ua.polodarb.suggestions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.domain.override.OverrideFlagsUseCase
import ua.polodarb.domain.override.models.OverriddenFlagsContainer
import ua.polodarb.domain.suggestedFlags.SuggestedFlagsUseCase
import ua.polodarb.domain.suggestedFlags.models.SuggestedFlagsModel
import ua.polodarb.repository.appsList.AppsListRepository
import ua.polodarb.repository.databases.gms.GmsDBRepository
import ua.polodarb.repository.suggestedFlags.models.FlagInfoRepoModel
import ua.polodarb.repository.suggestedFlags.models.FlagTypeRepoModel
import ua.polodarb.repository.uiStates.UiStates
import java.util.Collections
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

typealias SuggestionsScreenUiState = UiStates<List<SuggestedFlagsModel>>

class SuggestionScreenViewModel(
    private val repository: GmsDBRepository,
    private val appsRepository: AppsListRepository,
    private val flagsUseCase: SuggestedFlagsUseCase,
    private val overrideFlagsUseCase: OverrideFlagsUseCase,
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

    suspend fun getSuggestedFlags() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val flags = flagsUseCase.invoke()
                withContext(Dispatchers.Main) {
                    if (!flags.isNullOrEmpty()) {
                        _stateSuggestionsFlags.value = UiStates.Success(flags)
                    } else {
                        _stateSuggestionsFlags.value = UiStates.Error()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _stateSuggestionsFlags.value = UiStates.Error()
                }
            }
        }
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
                    val overriddenFlags = when (flag.type) {
                        FlagTypeRepoModel.BOOL -> OverriddenFlagsContainer(
                            boolValues = mapOf(flag.name to if (newBoolValue) flag.value else "0") // or null?
                        )
                        FlagTypeRepoModel.INTEGER -> OverriddenFlagsContainer(
                            intValues = mapOf(flag.name to if (newBoolValue) flag.value else null)
                        )
                        FlagTypeRepoModel.FLOAT -> OverriddenFlagsContainer(
                            floatValues = mapOf(flag.name to if (newBoolValue) flag.value else null)
                        )
                        FlagTypeRepoModel.STRING -> OverriddenFlagsContainer(
                            stringValues = mapOf(flag.name to if (newBoolValue) flag.value else null)
                        )
                        FlagTypeRepoModel.EXTVAL -> OverriddenFlagsContainer(
                            extValues = mapOf(flag.name to if (newBoolValue) flag.value else null)
                        )
                        else -> null
                    }

                    overriddenFlags?.let {
                        overrideFlagsUseCase.invoke(
                            packageName = packageName,
                            flags = it
                        )
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

    private fun <T> collectFlagsFlow(
        dataFlow: Flow<UiStates<T>>,
        loadingState: MutableStateFlow<SuggestionsScreenUiState>,
        errorState: MutableStateFlow<SuggestionsScreenUiState>,
        coroutineContext: CoroutineContext = EmptyCoroutineContext,
        onSuccess: suspend (UiStates.Success<T>) -> Unit
    ) {
        viewModelScope.launch(coroutineContext) {
            dataFlow.collect { uiStates ->
                handleUiStates(
                    uiStates = uiStates,
                    loadingState = loadingState,
                    errorState = errorState,
                    onSuccess = onSuccess
                )
            }
        }
    }

    private suspend fun <T> handleUiStates(
        uiStates: UiStates<T>,
        loadingState: MutableStateFlow<SuggestionsScreenUiState>,
        errorState: MutableStateFlow<SuggestionsScreenUiState>,
        onSuccess: suspend (UiStates.Success<T>) -> Unit
    ) {
        when (uiStates) {
            is UiStates.Success -> onSuccess(uiStates)

            is UiStates.Loading -> loadingState.value = UiStates.Loading()

            is UiStates.Error -> errorState.value = UiStates.Error()
        }
    }

}
