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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.protobuf.ProtoBuf
import ua.polodarb.domain.countryIso.SimCountryIsoUseCase
import ua.polodarb.domain.override.OverrideFlagsUseCase
import ua.polodarb.domain.override.models.OverriddenFlagsContainer
import ua.polodarb.domain.suggestedFlags.SuggestedFlagsUseCase
import ua.polodarb.domain.suggestedFlags.models.GroupedSuggestedFlagsModel
import ua.polodarb.repository.appsList.AppsListRepository
import ua.polodarb.repository.databases.gms.GmsDBRepository
import ua.polodarb.repository.suggestedFlags.models.FlagInfoRepoModel
import ua.polodarb.repository.suggestedFlags.models.FlagTypeRepoModel
import ua.polodarb.repository.uiStates.UiStates
import ua.polodarb.suggestions.specialFlags.callScreen.A6
import ua.polodarb.suggestions.specialFlags.callScreen.CallScreenI18nConfig
import ua.polodarb.suggestions.specialFlags.callScreen.CountryConfig
import ua.polodarb.suggestions.specialFlags.callScreen.Language
import ua.polodarb.suggestions.specialFlags.callScreen.LanguageConfig
import java.util.Collections
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

typealias SuggestionsScreenUiState = UiStates<GroupedSuggestedFlagsModel>

class SuggestionScreenViewModel(
    private val repository: GmsDBRepository,
    private val appsRepository: AppsListRepository,
    private val flagsUseCase: SuggestedFlagsUseCase,
    private val overrideFlagsUseCase: OverrideFlagsUseCase,
    private val simCountryIsoUseCase: SimCountryIsoUseCase,
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

    fun updateFlagValue(newValue: Boolean, index: Int) {
        val currentState = _stateSuggestionsFlags.value
        if (currentState is UiStates.Success<GroupedSuggestedFlagsModel>) {
            val updatedPrimary = currentState.data.primary.toMutableList()
            val updatedSecondary = currentState.data.secondary.toMutableList()

            if (index != -1) {
                if (index < updatedPrimary.size) {
                    updatedPrimary[index] = updatedPrimary[index].copy(enabled = newValue)
                } else if (index < updatedPrimary.size + updatedSecondary.size) {
                    val secondaryIndex = index - updatedPrimary.size
                    updatedSecondary[secondaryIndex] = updatedSecondary[secondaryIndex].copy(enabled = newValue)
                }

                val newData = currentState.data.copy(
                    primary = updatedPrimary,
                    secondary = updatedSecondary
                )

                _stateSuggestionsFlags.value = UiStates.Success(
                    newData.copy(
                        primary = newData.primary.sortedByDescending { it.flag.isPrimary },
                        secondary = newData.secondary.sortedByDescending { it.flag.isPrimary }
                    )
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
                        _stateSuggestionsFlags.value = UiStates.Success(GroupedSuggestedFlagsModel(
                            primary = flags.sortedByDescending { it.flag.isPrimary }.filter { it.flag.isPrimary == true },
                            secondary = flags.sortedByDescending { it.flag.isPrimary }.filter { it.flag.isPrimary != true }
                        ))
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
        newBoolValue: Boolean
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
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

    @OptIn(ExperimentalSerializationApi::class)
    suspend fun overrideCallScreenI18nConfig(locale: String) {
        val callScreenI18nConfig = CallScreenI18nConfig(
            countryConfigs = listOf(
                CountryConfig(
                    country = extractCountryIso(locale),
                    languageConfig = LanguageConfig(
                        languages = listOf(
                            Language(
                                languageCode = locale,
                                a6 = A6(a7 = byteArrayOf(2))
                            )
                        )
                    )
                )
            )
        )

        val byteString = ProtoBuf.encodeToHexString(callScreenI18nConfig)
        val overriddenFlags = OverriddenFlagsContainer(
            extValues = mapOf("CallScreenI18n__call_screen_i18n_config" to byteString)
        )

//            Log.e("callscreen", overriddenFlags.toString())

        overrideFlagsUseCase.invoke("com.google.android.dialer", overriddenFlags)
    }

    private fun extractCountryIso(languageCode: String): String {
        val parts = languageCode.split("-")
        if (parts.size > 1) return parts[1].lowercase()

        val result = simCountryIsoUseCase.invoke()
        if (result.isSuccess) return result.getOrThrow()
        throw result.exceptionOrNull() ?: Exception()
    }
}
