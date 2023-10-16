package ua.polodarb.gmsflags.ui.screens

sealed interface UiStates<T> {
    class Loading<T> : UiStates<T>

    data class Success<T>(
        val data: T
    ) : UiStates<T>

    data class Error<T>(
        val throwable: Throwable? = null
    ) : UiStates<T>
}