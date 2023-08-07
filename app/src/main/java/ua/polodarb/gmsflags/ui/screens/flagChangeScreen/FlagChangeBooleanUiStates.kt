package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

import ua.polodarb.gmsflags.data.repo.DatabaseRepository.PentagonMap

sealed interface FlagChangeBooleanUiStates {
    data object Loading : FlagChangeBooleanUiStates

    data class Success(
        val data: Map<String, Boolean>
    ) : FlagChangeBooleanUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : FlagChangeBooleanUiStates
}