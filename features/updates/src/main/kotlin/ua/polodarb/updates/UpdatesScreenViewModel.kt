package ua.polodarb.updates

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
import ua.polodarb.preferences.datastore.DatastoreManager
import ua.polodarb.preferences.datastore.models.LastUpdatesAppModel
import ua.polodarb.preferences.sharedPrefs.PreferenceConstants
import ua.polodarb.preferences.sharedPrefs.PreferencesManager
import ua.polodarb.repository.googleUpdates.GoogleUpdatesRepository
import ua.polodarb.repository.googleUpdates.model.MainRssArticle
import ua.polodarb.repository.uiStates.UiStates

class UpdatesScreenViewModel(
    private val repository: GoogleUpdatesRepository,
    private val sharedPrefs: PreferencesManager,
    private val datastore: DatastoreManager,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiStates<List<MainRssArticle>>>(UiStates.Loading())
    val uiState: StateFlow<UiStates<List<MainRssArticle>>> = _uiState.stateIn(
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
                val result = repository.getLatestRelease()
                _uiState.update {
                    UiStates.Success(result.articles)
                }
                sharedPrefs.saveData(
                    PreferenceConstants.GOOGLE_LAST_UPDATE,
                    "${result.articles.first().title}/${result.articles.first().version}"
                ) // TODO: delete after a few updates
                datastore.setLastUpdatedGoogleApp(
                    LastUpdatesAppModel(
                        appName = result.articles.first().title,
                        appVersion = result.articles.first().version
                    )
                )
            } catch (err: Throwable) {
                _uiState.update { UiStates.Error(err) }
            }
        }
    }

}
