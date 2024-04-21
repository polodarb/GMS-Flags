package ua.polodarb.flagsfile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.polodarb.repository.flagsFile.FlagsFromFileRepository
import ua.polodarb.repository.flagsFile.model.LoadedFlags
import ua.polodarb.repository.uiStates.UiStates

class LoadFileScreenViewModel(
    private val fileUri: Uri?,
    private val repository: FlagsFromFileRepository
) : ViewModel() {

    private val _flagsData = MutableStateFlow<UiStates<LoadedFlags>>(UiStates.Loading())
    val flagsData: StateFlow<UiStates<LoadedFlags>> = _flagsData.asStateFlow()

    init {
//        Log.e("TAG", "uri: $fileUri")
        viewModelScope.launch {
            read()
        }
    }

    private suspend fun read() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.e("TAG", "read: $fileUri")
            fileUri?.let {
                repository.read(it).collect { uiState ->
                    Log.e("TAG", "read: $uiState")
                    when (uiState) {
                        is UiStates.Loading -> {
                            _flagsData.value = UiStates.Loading()
                        }
                        is UiStates.Error -> {
                            _flagsData.value = UiStates.Error(Throwable("Error read file"))
                        }
                        is UiStates.Success -> {
                            _flagsData.value = UiStates.Success(uiState.data)
                        }
                    }
                }
            } ?: run {
                _flagsData.value = UiStates.Error(Throwable("Error read file"))
            }
//            _flagsData.value = fileUri?.let {
//                try {
//                    val result = repository.read(it)
//                    Log.e("TAG", "read: $result")
//                    UiStates.Success(result)
//                } catch (e: Exception) {
//                    Log.e("TAG", "error: $e")
//                    UiStates.Error()
//                }
//            } ?: UiStates.Error()
//            if (fileUri == null) {
//                Log.e("TAG", "uri: $fileUri")
//                _flagsData.value = UiStates.Error()
//            } else {
//                runCatching {
//                    val result = repository.read(fileUri)
//                    _flagsData.value = UiStates.Success(result)
//                }.onFailure {
//                    Log.e("TAG", "error: $it")
//                    _flagsData.value = UiStates.Error()
//                }
//            }
        }
    }

}