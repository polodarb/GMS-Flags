package ua.polodarb.flagsfile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.flagsfile.model.LoadedFlagsUI
import ua.polodarb.flagsfile.model.toLoadedFlagsUIList
import ua.polodarb.repository.databases.gms.GmsDBInteractor
import ua.polodarb.repository.databases.gms.GmsDBRepository
import ua.polodarb.repository.flagsFile.FlagsFromFileRepository
import ua.polodarb.repository.uiStates.UiStates
import java.util.Collections

class LoadFileScreenViewModel(
    private val fileUri: Uri?,
    private val repository: FlagsFromFileRepository,
    private val gmsDBRepository: GmsDBRepository,
    private val gmsDBInteractor: GmsDBInteractor
) : ViewModel() {

    private val _flagsData = MutableStateFlow<UiStates<LoadedFlagsUI>>(UiStates.Loading())
    val flagsData: StateFlow<UiStates<LoadedFlagsUI>> = _flagsData.asStateFlow()

    private val usersList = Collections.synchronizedList(mutableListOf<String>())

    init {
        viewModelScope.launch {
            initUsers()
            read()
        }
    }

    fun updateFlagOverride(flagName: String, newValue: Boolean) {
        when (val currentState = _flagsData.value) {
            is UiStates.Success -> {
                val updatedFlags = currentState.data.flags.map { flag ->
                    if (flag.name == flagName) {
                        flag.copy(override = newValue)
                    } else {
                        flag
                    }
                }
                _flagsData.value = currentState.copy(data = currentState.data.copy(flags = updatedFlags))
            }
            else -> Unit
        }
    }

    private suspend fun read() {
        viewModelScope.launch(Dispatchers.IO) {
            fileUri?.let {
                repository.read(it).collect { uiState ->
                    Log.e("file", uiState.toString())
                    when (uiState) {
                        is UiStates.Loading -> {
                            _flagsData.value = UiStates.Loading()
                        }
                        is UiStates.Error -> {
                            _flagsData.value = UiStates.Error(uiState.throwable)
                        }
                        is UiStates.Success -> {
                            _flagsData.value = UiStates.Success(uiState.data.toLoadedFlagsUIList())
                        }
                    }
                }
            } ?: run {
                _flagsData.value = UiStates.Error(Throwable("fileUri is null"))
            }
        }
    }

    private fun initUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gmsDBRepository.getUsers().collect {
                    usersList.addAll(it)
                }
            }
        }
    }

    fun overrideFlags(progress: (Float) -> Unit, onComplete: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val data = _flagsData.value) {
                is UiStates.Success -> {
                    val flags = data.data.flags
                    val totalFlags = flags.size
                    var flagsProcessed = 0

                    data.data.flags.forEach { flag ->
                        when (flag.type) {
                            "Boolean" -> {
                                if (flag.override) {
                                    gmsDBInteractor.overrideFlag(
                                        packageName = data.data.packageName,
                                        name = flag.name,
                                        boolVal = if (flag.value == true) "1" else "0",
                                        usersList = usersList
                                    )
                                }
                            }
                            "Int" -> {
                                if (flag.override) {
                                    gmsDBInteractor.overrideFlag(
                                        packageName = data.data.packageName,
                                        name = flag.name,
                                        intVal = flag.value.toString(),
                                        usersList = usersList
                                    )
                                }
                            }
                            "Float" -> {
                                if (flag.override) {
                                    gmsDBInteractor.overrideFlag(
                                        packageName = data.data.packageName,
                                        name = flag.name,
                                        floatVal = flag.value.toString(),
                                        usersList = usersList
                                    )
                                }
                            }
                            "String" -> {
                                if (flag.override) {
                                    gmsDBInteractor.overrideFlag(
                                        packageName = data.data.packageName,
                                        name = flag.name,
                                        stringVal = flag.value.toString(),
                                        usersList = usersList
                                    )
                                }
                            }
                            "ExtensionVal" -> {
                                if (flag.override) {
                                    gmsDBInteractor.overrideFlag(
                                        packageName = data.data.packageName,
                                        name = flag.name,
                                        extensionVal = flag.value.toString(),
                                        usersList = usersList
                                    )
                                }
                            }
                        }

                        flagsProcessed++
                        val progressResult = (flagsProcessed.toFloat() / totalFlags.toFloat())
                        progress(progressResult)
                    }

                    delay(500)
                    onComplete.invoke()
                }
                else -> Unit
            }
        }
    }


}