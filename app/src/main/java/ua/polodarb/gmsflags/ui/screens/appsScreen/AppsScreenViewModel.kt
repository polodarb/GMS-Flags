package ua.polodarb.gmsflags.ui.screens.appsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates

class AppsScreenViewModel(
    private val repository: AppsListRepository
): ViewModel() {

    private val _state =
        MutableStateFlow<AppsScreenUiStates>(AppsScreenUiStates.Loading)
    val state: StateFlow<AppsScreenUiStates> = _state.asStateFlow()

    init {
        getAllInstalledApps()
    }

    private fun getAllInstalledApps() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getAllInstalledApps().collect { uiStates ->
                    when (uiStates) {
                        is AppsScreenUiStates.Success -> {
                            _state.value = AppsScreenUiStates.Success(uiStates.data)
                        }

                        is AppsScreenUiStates.Loading -> {
                            _state.value = AppsScreenUiStates.Loading
                        }

                        is AppsScreenUiStates.Error -> {
                            _state.value = AppsScreenUiStates.Error()
                        }
                    }
                }
            }
        }
    }

}