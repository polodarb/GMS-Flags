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
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.data.repo.RoomDBRepository
import ua.polodarb.gmsflags.ui.screens.suggestionsScreen.SuggestionsScreenUiStates

class PackagesScreenViewModel(
    private val gmsRepository: GmsDBRepository,
    private val roomRepository: RoomDBRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenUiStates>(ScreenUiStates.Loading)
    val state: StateFlow<ScreenUiStates> = _state.asStateFlow()

    private val _stateSavedPackages =
        MutableStateFlow<List<String>>(emptyList())
    val stateSavedPackages: StateFlow<List<String>> = _stateSavedPackages.asStateFlow()

    init {
        initGmsPackagesList()
        getAllSavedPackages()
    }

//    fun updateSavedState(pkgName: String) {
//        val currentState = _stateSavedPackages.value.toMutableList()
//        if (currentState.isNotEmpty()) {
//            currentState.remove(pkgName)
//            _stateSavedPackages.value = currentState.toList()
//        }
//    }

    fun initGmsPackagesList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gmsRepository.getGmsPackages().collect { uiState ->
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

    private fun getAllSavedPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.getSavedPackages().collect {
                    _stateSavedPackages.value = it
                }
            }
        }
    }

    fun savePackage(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.savePackage(pkgName)
            }
        }
    }

    fun deleteSavedPackage(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.deleteSavedPackage(pkgName)
            }
        }
    }

}