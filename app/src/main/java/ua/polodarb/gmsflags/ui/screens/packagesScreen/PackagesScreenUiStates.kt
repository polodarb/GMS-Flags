package ua.polodarb.gmsflags.ui.screens.packagesScreen

sealed interface PackagesScreenUiStates {
    data object Loading : PackagesScreenUiStates

    data class Success(
        val data: List<String>
    ) : PackagesScreenUiStates

    data class Error(
        val throwable: Throwable? = null
    ) : PackagesScreenUiStates
}