package ua.polodarb.gmsflags.ui.screens.savedScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.RoomDBRepository
import ua.polodarb.gmsflags.ui.screens.suggestionsScreen.SuggestionsScreenUiStates

class SavedScreenViewModel(
    val roomRepository: RoomDBRepository
): ViewModel() {

    private val _stateSavedPackages =
        MutableStateFlow<List<String>>(emptyList())
    val stateSavedPackages: StateFlow<List<String>> = _stateSavedPackages.asStateFlow()

    init {
        getAllSavedPackages()
    }

    fun getAllSavedPackages() {
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

}