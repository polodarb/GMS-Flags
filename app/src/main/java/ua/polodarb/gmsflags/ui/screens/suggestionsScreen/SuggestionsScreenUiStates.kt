package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

sealed interface SuggestionsScreenUiStates {
    data object Loading : SuggestionsScreenUiStates

    data class Success(
        val data: List<SuggestedFlag>
    ) : SuggestionsScreenUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : SuggestionsScreenUiStates
}