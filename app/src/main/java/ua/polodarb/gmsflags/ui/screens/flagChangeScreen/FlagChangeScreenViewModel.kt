package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.DatabaseRepository
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates

class FlagChangeScreenViewModel(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenUiStates>(ScreenUiStates.Loading)
    val state: StateFlow<ScreenUiStates> = _state.asStateFlow()

    fun getBoolFlags(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getBoolFlags(pkgName).collect { uiState ->
                    when (uiState) {
                        is ScreenUiStates.Success -> {
                            _state.value = ScreenUiStates.Success(uiState.data)
                            Log.e("VM", "${_state.value}")
                        }

                        is ScreenUiStates.Error -> {

                        }

                        is ScreenUiStates.Loading -> {}
                    }
                }
            }
        }
    }

}