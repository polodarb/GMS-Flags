package ua.polodarb.gmsflags.ui.screens

import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates

sealed interface UiStates {
    data object Loading : UiStates

    data class Success<T>(
        val data: T
    ) : UiStates

    data class Error(
        val throwable: Throwable? = null
    ) : UiStates
}