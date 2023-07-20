package ua.polodarb.gmsflags.ui.viewModel

import androidx.lifecycle.ViewModel
import com.topjohnwu.superuser.ShellUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import ua.polodarb.gmsflags.data.repository.Repository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repo: Repository
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