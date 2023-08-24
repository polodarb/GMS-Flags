package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

sealed interface FlagChangeUiStates {
    data object Loading : FlagChangeUiStates

    data class Success(
        val data: Map<String, String>
    ) : FlagChangeUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : FlagChangeUiStates
}