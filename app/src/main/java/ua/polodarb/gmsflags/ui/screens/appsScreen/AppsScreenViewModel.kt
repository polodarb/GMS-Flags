package ua.polodarb.gmsflags.ui.screens.appsScreen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.AppInfo
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.ui.screens.appsScreen.dialog.DialogUiStates

class AppsScreenViewModel(
    private val repository: AppsListRepository
) : ViewModel() {

    private val _state =
        MutableStateFlow<AppsScreenUiStates>(AppsScreenUiStates.Loading)
    val state: StateFlow<AppsScreenUiStates> = _state.asStateFlow()

    private val _dialogDataState =
        MutableStateFlow<DialogUiStates>(DialogUiStates.Loading)
    val dialogDataState: StateFlow<DialogUiStates> = _dialogDataState.asStateFlow()

    private val _dialogPackage = MutableStateFlow("")
    val dialogPackage: StateFlow<String> = _dialogPackage.asStateFlow()


    var searchQuery = mutableStateOf("")

    private val listAppsFiltered: MutableList<AppInfo> = mutableListOf()

    init {
        initAllInstalledApps()
    }

    fun setPackageToDialog(pkgName: String) {
        _dialogPackage.value = pkgName
    }

    fun getListByPackages(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getListByPackages(pkgName).collect { uiStates ->
                    when (uiStates) {
                        is DialogUiStates.Success -> {
                            _dialogDataState.value = DialogUiStates.Success(uiStates.data)
                        }

                        is DialogUiStates.Loading -> {
                            _dialogDataState.value = DialogUiStates.Loading
                        }

                        is DialogUiStates.Error -> {
                            _dialogDataState.value = DialogUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    private fun initAllInstalledApps() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getAllInstalledApps().collectLatest { uiStates ->
                    Log.e("vmui", uiStates.toString())
                    when (uiStates) {
                        is AppsScreenUiStates.Success -> {
                            listAppsFiltered.addAll(uiStates.data)
                            getAllInstalledApps()
                        }

                        is AppsScreenUiStates.Loading -> {
                            _state.value = AppsScreenUiStates.Loading
                        }

                        is AppsScreenUiStates.Error -> {
                            _state.value = AppsScreenUiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getAllInstalledApps() {
        if (listAppsFiltered.isNotEmpty()) {
            _state.value = AppsScreenUiStates.Success(
                listAppsFiltered.filter {
                    it.appName.contains(searchQuery.value, ignoreCase = true)
                }
            )
        }
    }
}
