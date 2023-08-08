package ua.polodarb.gmsflags.ui.screens.flagChangeScreen

sealed interface FlagChangeOtherTypesUiStates {
    data object Loading : FlagChangeOtherTypesUiStates

    data class Success(
        val data: Map<String, String>
    ) : FlagChangeOtherTypesUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : FlagChangeOtherTypesUiStates
}