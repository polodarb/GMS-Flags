package ua.polodarb.flagsChange.extScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.repository.databases.gms.GmsDBInteractor
import ua.polodarb.repository.databases.gms.GmsDBRepository
import java.util.Collections

typealias AddFlagType = List<String>

class AddMultipleFlagsViewModel(
    private val pkgName: String,
    private val repository: GmsDBRepository,
    private val gmsDBInteractor: GmsDBInteractor
) : ViewModel() {

    init {
        initUsers()
    }

    private val _booleanFlags = MutableStateFlow<AddFlagType>(emptyList())
    val booleanFlags: StateFlow<AddFlagType> = _booleanFlags.asStateFlow()

    private val _integerFlags = MutableStateFlow<AddFlagType>(emptyList())
    val integerFlags: StateFlow<AddFlagType> = _integerFlags.asStateFlow()

    private val _floatFlags = MutableStateFlow<AddFlagType>(emptyList())
    val floatFlags: StateFlow<AddFlagType> = _floatFlags.asStateFlow()

    private val _stringFlags = MutableStateFlow<AddFlagType>(emptyList())
    val stringFlags: StateFlow<AddFlagType> = _stringFlags.asStateFlow()

    private val _boolSwitch = MutableStateFlow(true)
    val boolSwitch: StateFlow<Boolean> = _boolSwitch.asStateFlow()

    private val usersList = Collections.synchronizedList(mutableListOf<String>())

    fun setBooleanFlag(flag: List<String>) {
        _booleanFlags.value = flag
    }

    fun setIntegerFlag(flag: List<String>) {
        _integerFlags.value = flag
    }

    fun setFloatFlag(flag: List<String>) {
        _floatFlags.value = flag
    }

    fun setStringFlag(flag: List<String>) {
        _stringFlags.value = flag
    }

    fun updateBoolSwitch(value: Boolean) {
        _boolSwitch.value = value
    }

    private fun initUsers() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.getUsers().collect {
                    usersList.addAll(it)
                }
            }
        }
    }

    fun overrideFlags(onStart: () -> Unit, onComplete: () -> Unit) {
        val startTime = System.currentTimeMillis()
        onStart.invoke()

        viewModelScope.launch(Dispatchers.IO) {
            if (booleanFlags.value.isNotEmpty()) {
                booleanFlags.value.forEach {
                    gmsDBInteractor.overrideFlag(
                        packageName = pkgName,
                        name = it,
                        boolVal = if (boolSwitch.value) "1" else "0",
                        usersList = usersList
                    )
                }
            }

            if (integerFlags.value.isNotEmpty()) {
                integerFlags.value.forEach {
                    val parts = it.split("=")
                    if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
                        gmsDBInteractor.overrideFlag(
                            packageName = pkgName,
                            name = parts[0],
                            intVal = parts[1],
                            usersList = usersList
                        )
                    }
                }
            }

            if (floatFlags.value.isNotEmpty()) {
                floatFlags.value.forEach {
                    val parts = it.split("=")
                    if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
                        gmsDBInteractor.overrideFlag(
                            packageName = pkgName,
                            name = parts[0],
                            floatVal = parts[1],
                            usersList = usersList
                        )
                    }
                }
            }

            if (stringFlags.value.isNotEmpty()) {
                stringFlags.value.forEach {
                    val parts = it.split("=")
                    if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
                        gmsDBInteractor.overrideFlag(
                            packageName = pkgName,
                            name = parts[0],
                            stringVal = parts[1],
                            usersList = usersList
                        )
                    }
                }
            }

            val elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime < 1000) {
                delay(1000 - elapsedTime)
            }

            onComplete.invoke()
        }
    }

}

