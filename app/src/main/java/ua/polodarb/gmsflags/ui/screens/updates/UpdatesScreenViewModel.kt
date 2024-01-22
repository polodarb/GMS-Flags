package ua.polodarb.gmsflags.ui.screens.updates

import android.util.Log
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
import ua.polodarb.gmsflags.data.prefs.shared.PreferenceConstants
import ua.polodarb.gmsflags.data.prefs.shared.PreferencesManager
import ua.polodarb.gmsflags.data.remote.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.gmsflags.data.repo.mappers.GoogleUpdatesMapper
import ua.polodarb.gmsflags.data.repo.mappers.NewRssArticle
import ua.polodarb.gmsflags.ui.screens.UiStates

class UpdatesScreenViewModel(
    private val googleAppUpdatesService: GoogleAppUpdatesService,
    private val googleUpdatesMapper: GoogleUpdatesMapper,
    private val sharedPrefs: PreferencesManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiStates<List<NewRssArticle>>>(UiStates.Loading())
    val uiState: StateFlow<UiStates<List<NewRssArticle>>> = _uiState.stateIn(
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
                val result = withContext(Dispatchers.IO) {
                    val response = googleAppUpdatesService.getLatestRelease()
                    googleUpdatesMapper.map(response)
                }
                _uiState.update {
                    UiStates.Success(result.articles)
                }
                Log.d("UpdatesScreenViewModel", "loadArticles: ${result.articles}")
            } catch (err: Throwable) {
                _uiState.update { UiStates.Error(err) }
            }
        }
    }

}
