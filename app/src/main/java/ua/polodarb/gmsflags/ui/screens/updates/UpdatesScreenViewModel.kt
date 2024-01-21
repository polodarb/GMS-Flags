package ua.polodarb.gmsflags.ui.screens.updates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.remote.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.gmsflags.data.remote.googleUpdates.dto.RssMainModel
import ua.polodarb.gmsflags.data.repo.mappers.GoogleUpdatesMapper
import ua.polodarb.gmsflags.data.repo.mappers.NewRssModel
import ua.polodarb.gmsflags.ui.screens.UiStates

class UpdatesScreenViewModel(
    private val googleAppUpdatesService: GoogleAppUpdatesService,
    private val googleUpdatesMapper: GoogleUpdatesMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiStates<NewRssModel>>(UiStates.Loading())
    val uiState: StateFlow<UiStates<NewRssModel>> = _uiState.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        UiStates.Loading()
    )

    init {
        loadArticles()
    }

    fun loadArticles() {
        _uiState.update { UiStates.Loading() }
        viewModelScope.launch {
            try {
                val home = withContext(Dispatchers.IO) {
                    val response = googleAppUpdatesService.getLatestRelease()
                    googleUpdatesMapper.map(response)
                }
                _uiState.update {
                    UiStates.Success(home)
                }
            } catch (err: Throwable) {
                _uiState.update { UiStates.Error(err) }
            }
        }
    }

}
