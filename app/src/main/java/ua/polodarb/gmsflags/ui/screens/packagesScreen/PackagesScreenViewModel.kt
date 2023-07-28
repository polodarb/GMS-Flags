package ua.polodarb.gmsflags.ui.screens.packagesScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.Repository

class PackagesScreenViewModel(
    val repository: Repository
) : ViewModel() {

    private val _state = MutableStateFlow<PackagesScreenUiStates>(PackagesScreenUiStates.Loading)
    val state: StateFlow<PackagesScreenUiStates> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getGmsPackages().collect { uiState ->
                    when (uiState) {
                        is PackagesScreenUiStates.Success -> {
                            _state.value = PackagesScreenUiStates.Success(uiState.data)
                            Log.e("VM", "${_state.value}")
                        }

                        is PackagesScreenUiStates.Error -> {

                        }

                        is PackagesScreenUiStates.Loading -> {}
                    }
                }
            }
        }
    }

}