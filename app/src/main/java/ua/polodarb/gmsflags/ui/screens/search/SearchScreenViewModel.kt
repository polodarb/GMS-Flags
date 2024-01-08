package ua.polodarb.gmsflags.ui.screens.search

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.AppInfo
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.data.repo.RoomDBRepository
import ua.polodarb.gmsflags.ui.screens.UiStates

typealias AppInfoList = UiStates<PersistentList<AppInfo>>
typealias AppDialogList = UiStates<PersistentList<String>>
typealias PackagesScreenUiStates = UiStates<Map<String, String>>

class SearchScreenViewModel(
    private val repository: AppsListRepository,
    private val gmsRepository: GmsDBRepository,
    private val roomRepository: RoomDBRepository
) : ViewModel() {

    // Apps List
    private val _appsListUiState =
        MutableStateFlow<AppInfoList>(UiStates.Loading())
    val appsListUiState: StateFlow<AppInfoList> = _appsListUiState.asStateFlow()

    private val _dialogDataState =
        MutableStateFlow<AppDialogList>(UiStates.Loading())
    val dialogDataState: StateFlow<AppDialogList> = _dialogDataState.asStateFlow()

    private val _dialogPackage = MutableStateFlow("")
    val dialogPackage: StateFlow<String> = _dialogPackage.asStateFlow()


    // Packages List
    private val _packagesListUiState = MutableStateFlow<PackagesScreenUiStates>(UiStates.Loading())
    val packagesListUiState: StateFlow<PackagesScreenUiStates> = _packagesListUiState.asStateFlow()

    private val _stateSavedPackages =
        MutableStateFlow<List<String>>(emptyList())
    val stateSavedPackages: StateFlow<List<String>> = _stateSavedPackages.asStateFlow()


    // All Flags List // TODO
//    private val _allFlagsListUiState = MutableStateFlow<PackagesScreenUiStates>(UiStates.Loading())
//    val allFlagsListUiState: StateFlow<PackagesScreenUiStates> = _allFlagsListUiState.asStateFlow()

    // Search and filter
    var appsSearchQuery = mutableStateOf("")
    private val appsListFiltered: MutableList<AppInfo> = mutableListOf()

    var packagesSearchQuery = mutableStateOf("")
    private val packagesListFiltered: MutableMap<String, String> = mutableMapOf()

    var allFlagsSearchQuery = mutableStateOf("")
    private val allFlagsListFiltered: MutableMap<String, String> = mutableMapOf()

    fun clearSearchQuery() {
        appsSearchQuery.value = ""
        packagesSearchQuery.value = ""
        allFlagsSearchQuery.value = ""
    }

    init {
        initAllInstalledApps()
        initGmsPackagesList()
        getAllSavedPackages()
    }

    fun setPackageToDialog(pkgName: String) {
        _dialogPackage.value = pkgName
    }

    fun setEmptyList() {
        _dialogDataState.value = UiStates.Success(persistentListOf())
    }

    /**
     * **AppsListScreen** - get list of packages in app
     */
    fun getListByPackages(pkgName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getListByPackages(pkgName).collect { uiStates ->
                    when (uiStates) {
                        is UiStates.Success -> {
                            _dialogDataState.value = UiStates.Success(uiStates.data.toPersistentList())
                        }

                        is UiStates.Loading -> {
                            _dialogDataState.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _dialogDataState.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    /**
     * **AppsListScreen** - init list of all installed apps
     */
    private fun initAllInstalledApps() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getAllInstalledApps().collectLatest { uiStates ->
                    when (uiStates) {
                        is UiStates.Success -> {
                            appsListFiltered.addAll(uiStates.data)
                            getAllInstalledApps()
                        }

                        is UiStates.Loading -> {
                            _appsListUiState.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _appsListUiState.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    /**
     * **AppsListScreen** - get list of all installed apps
     */
    fun getAllInstalledApps() {
        if (appsListFiltered.isNotEmpty()) {
            _appsListUiState.value = UiStates.Success(
                appsListFiltered.filter {
                    it.appName.contains(appsSearchQuery.value, ignoreCase = true)
                }.toPersistentList()
            )
        }
    }

    private fun initGmsPackagesList() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gmsRepository.getGmsPackages().collect { uiState ->
                    when (uiState) {
                        is UiStates.Success -> {
                            packagesListFiltered.putAll(uiState.data)
                            getGmsPackagesList()
                        }

                        is UiStates.Loading -> {
                            _packagesListUiState.value = UiStates.Loading()
                        }

                        is UiStates.Error -> {
                            _packagesListUiState.value = UiStates.Error()
                        }
                    }
                }
            }
        }
    }

    fun getGmsPackagesList() {
        if (packagesListFiltered.isNotEmpty()) {
            _packagesListUiState.value = UiStates.Success(
                packagesListFiltered.filter {
                    it.key.contains(packagesSearchQuery.value, ignoreCase = true)
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
