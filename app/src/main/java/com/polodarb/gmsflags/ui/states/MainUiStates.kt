package com.polodarb.gmsflags.ui.states

import com.polodarb.gmsflags.data.local.FlagsEntity

sealed class MainUiStates {
    object Loading : MainUiStates() {}

    data class Success(val data: FlagsEntity) : MainUiStates()

    data class Error(val throwable: String) : MainUiStates()
}