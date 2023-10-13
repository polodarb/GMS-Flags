package ua.polodarb.gmsflags.ui.screens.packagesScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
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
class PackagesScreenViewModel(
    private val gmsRepository: GmsDBRepository,
    private val roomRepository: RoomDBRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ScreenUiStates>(ScreenUiStates.Loading)
    val state: StateFlow<ScreenUiStates> = _state.asStateFlow()

    private val _stateSavedPackages =
        MutableStateFlow<List<String>>(emptyList())
    val stateSavedPackages: StateFlow<List<String>> = _stateSavedPackages.asStateFlow()

    // Search
    var searchQuery = mutableStateOf("")
    private val listFiltered: MutableMap<String, String> = mutableMapOf()

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

    private fun initGmsPackagesList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gmsRepository.getGmsPackages().collect { uiState ->
                    when (uiState) {
                        is ScreenUiStates.Success -> {
                            listFiltered.putAll(uiState.data)
                            getGmsPackagesList()
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

    fun getGmsPackagesList() {
        if (listFiltered.isNotEmpty()) {
            _state.value = ScreenUiStates.Success(
                listFiltered.filter {
                    it.key.contains(searchQuery.value, ignoreCase = true)
                }.toSortedMap()
            )
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