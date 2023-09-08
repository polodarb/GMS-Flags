package ua.polodarb.gmsflags.ui.screens.appsScreen.dialog

sealed interface DialogUiStates {
    data object Loading : DialogUiStates

    data class Success(
        val data: List<String>
    ) : DialogUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : DialogUiStates
}