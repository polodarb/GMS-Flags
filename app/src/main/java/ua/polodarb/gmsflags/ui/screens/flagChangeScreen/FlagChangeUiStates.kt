package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import ua.polodarb.gmsflags.data.repo.DatabaseRepository.PentagonMap

sealed interface FlagChangeUiStates {
    data object Loading : FlagChangeUiStates

    data class Success(
        val data: PentagonMap
    ) : FlagChangeUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : FlagChangeUiStates
}