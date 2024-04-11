package ua.polodarb.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.polodarb.repository.databases.local.LocalDBRepository
import ua.polodarb.repository.databases.local.model.SavedFlags

class SavedScreenViewModel(
    private val localDBRepository: LocalDBRepository
): ViewModel() {

    private val _stateSavedPackages =
        MutableStateFlow<List<String>>(emptyList())
    val stateSavedPackages: StateFlow<List<String>> = _stateSavedPackages.asStateFlow()

    private val _stateSavedFlags =
        MutableStateFlow<List<SavedFlags>>(emptyList())
    val stateSavedFlags: StateFlow<List<SavedFlags>> = _stateSavedFlags.asStateFlow()

    init {
        getAllSavedPackages()
        getAllSavedFlags()
    }

    private fun getAllSavedPackages() {
        viewModelScope.launch {
            localDBRepository.getSavedPackages().collect {
                _stateSavedPackages.value = it
            }
        }
    }

    fun deleteSavedPackage(pkgName: String) {
        viewModelScope.launch {
            localDBRepository.deleteSavedPackage(pkgName)
        }
    }

    private fun getAllSavedFlags() {
        viewModelScope.launch {
            localDBRepository.getSavedFlags().collect {
                _stateSavedFlags.value = it
            }
        }
    }

    fun deleteSavedFlag(flagName: String, pkgName: String) {
        viewModelScope.launch {
            localDBRepository.deleteSavedFlag(flagName, pkgName)
        }
    }
}