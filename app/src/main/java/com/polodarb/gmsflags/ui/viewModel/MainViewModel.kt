package com.polodarb.gmsflags.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.polodarb.gmsflags.data.repository.Repository
import com.polodarb.gmsflags.ui.screens.packagesScreen.DB_PATH
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

    lateinit var rootShell: Shell

        init {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (Shell.getShell().status != Shell.ROOT_SHELL) {
                        Shell.setDefaultBuilder(
                            Shell.Builder.create()
                                .setFlags(Shell.FLAG_REDIRECT_STDERR)
                                .setTimeout(10)
                        )
                    }

                }
            }
        }

    fun getPackagesShell(): List<String> {
        return Shell.cmd(
            "cd $DB_PATH",
            "sqlite3 phenotype.db" +
                    " \"SELECT DISTINCT packageName FROM Flags\""
        ).exec().out
    }

}