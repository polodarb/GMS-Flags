package ua.polodarb.gmsflags.ui.viewModel

import androidx.lifecycle.ViewModel
import com.topjohnwu.superuser.ShellUtils
import ua.polodarb.gmsflags.data.repo.DatabaseRepository

class MainViewModel(
    val repo: DatabaseRepository
) : ViewModel() {

//    private val _state = MutableStateFlow<MainUiStates>(MainUiStates.Loading)
//    val state: StateFlow<MainUiStates> = _state.asStateFlow()

    companion object {
        private var rootShell: ShellUtils? = null
    }

//    init {
//            viewModelScope.launch {
//                withContext(Dispatchers.IO) {
//                    initShell()
//                }
//            }
//        }
//
//    private fun initShell() {
//        if (Shell.getShell().status != Shell.ROOT_SHELL) {
//            Shell.setDefaultBuilder(
//                Shell.Builder.create()
//                    .setFlags(Shell.FLAG_REDIRECT_STDERR)
//                    .setTimeout(10)
//            )
//        }
//    }

}