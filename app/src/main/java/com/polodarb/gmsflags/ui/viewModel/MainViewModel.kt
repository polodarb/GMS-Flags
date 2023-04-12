package com.polodarb.gmsflags.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polodarb.gmsflags.data.repository.Repository
import com.polodarb.gmsflags.ui.states.MainUiStates
import com.topjohnwu.superuser.Shell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repo: Repository
) : ViewModel() {

    private val _state = MutableStateFlow<MainUiStates>(MainUiStates.Loading)
    val state: StateFlow<MainUiStates> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Shell.setDefaultBuilder(
                    Shell.Builder.create()
                        .setFlags(Shell.FLAG_REDIRECT_STDERR)
                        .setTimeout(10)
                )

//                _state.value =
            }
        }
    }

}