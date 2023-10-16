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
import ua.polodarb.gmsflags.ui.screens.UiStates

typealias PackagesScreenUiStates = UiStates<Map<String, String>>

class PackagesScreenViewModel(
    private val gmsRepository: GmsDBRepository,
    private val roomRepository: RoomDBRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<PackagesScreenUiStates>(UiStates.Loading())
    val state: StateFlow<PackagesScreenUiStates> = _state.asStateFlow()

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

    private fun initGmsPackagesList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gmsRepository.getGmsPackages().collect { uiState ->
                    when (uiState) {
                        is UiStates.Success -> {
                            listFiltered.putAll(uiState.data)
                            getGmsPackagesList()
                        }

                        is UiStates.Loading -> {
                            _state.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _state.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getGmsPackagesList() {
        if (listFiltered.isNotEmpty()) {
            _state.value = UiStates.Success(
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