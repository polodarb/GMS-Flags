package ua.polodarb.gmsflags.ui.screens.appsScreen

sealed interface AppsScreenUiStates {
    data object Loading : AppsScreenUiStates

    data class Success(
        val data: List<AppInfo>
    ) : AppsScreenUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : AppsScreenUiStates
}