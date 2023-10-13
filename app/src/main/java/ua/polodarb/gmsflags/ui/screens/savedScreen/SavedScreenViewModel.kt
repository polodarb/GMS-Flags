package ua.polodarb.gmsflags.ui.screens.savedScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.databases.local.enities.SavedFlags
import ua.polodarb.gmsflags.data.repo.RoomDBRepository

class SavedScreenViewModel(
    val roomRepository: RoomDBRepository
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

    // Packages
    private fun getAllSavedPackages() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.getSavedPackages().collect {
                    _stateSavedPackages.value = it
                }
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

    // Flags
    private fun getAllSavedFlags() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.getSavedFlags().collect {
                    _stateSavedFlags.value = it
                }
            }
        }
    }

    fun deleteSavedFlag(flagName: String, pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                roomRepository.deleteSavedFlag(flagName, pkgName)
            }
        }
    }

}