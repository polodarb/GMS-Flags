package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

sealed interface FlagChangeBooleanUiStates {
    data object Loading : FlagChangeBooleanUiStates

    data class Success(
        val data: Map<String, Boolean>
    ) : FlagChangeBooleanUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : FlagChangeBooleanUiStates
}