package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.DatabaseRepository
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates

class FlagChangeScreenViewModel(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val _state = MutableStateFlow<FlagChangeUiStates>(FlagChangeUiStates.Loading)
    val state: StateFlow<FlagChangeUiStates> = _state.asStateFlow()

    fun getFlagsData(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getFlagsData(pkgName).collect { uiState ->
                    when (uiState) {
                        is FlagChangeUiStates.Success -> {
                            _state.value = FlagChangeUiStates.Success(uiState.data)
                        }

                        is FlagChangeUiStates.Error -> {
                            _state.value = FlagChangeUiStates.Error(Throwable("LIST_ERROR"))
                        }

                        is FlagChangeUiStates.Loading -> {}
                    }
                    }
                }
            }
        }

}