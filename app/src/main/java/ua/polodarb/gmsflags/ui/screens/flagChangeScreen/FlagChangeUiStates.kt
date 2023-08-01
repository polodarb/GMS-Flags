package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import ua.polodarb.gmsflags.data.repo.DatabaseRepository.QuadMap

sealed interface FlagChangeUiStates {
    data object Loading : FlagChangeUiStates

    data class Success(
        val data: QuadMap
    ) : FlagChangeUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : FlagChangeUiStates
}