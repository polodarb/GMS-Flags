package ua.polodarb.gmsflags.ui.screens.packagesScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.DatabaseRepository

class PackagesScreenViewModel(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenUiStates>(ScreenUiStates.Loading)
    val state: StateFlow<ScreenUiStates> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getGmsPackages().collect { uiState ->
                    when (uiState) {
                        is ScreenUiStates.Success -> {
                            _state.value = ScreenUiStates.Success(uiState.data)
                        }

                        is ScreenUiStates.Loading -> {
                            _state.value = ScreenUiStates.Loading
                        }

                        is ScreenUiStates.Error -> {
                            _state.value = ScreenUiStates.Error()
                        }
                    }
                }
            }
        }
    }

}