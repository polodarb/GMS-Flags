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
import ua.polodarb.domain.override.OverrideFlagsUseCase
import ua.polodarb.domain.override.models.OverriddenFlagsContainer
import ua.polodarb.repository.databases.gms.GmsDBRepository
import java.util.Collections

typealias AddFlagType = List<String>

class AddMultipleFlagsViewModel(
    private val pkgName: String,
    private val repository: GmsDBRepository,
    private val overrideFlagsUseCase: OverrideFlagsUseCase
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
                    overrideFlagsUseCase.invoke(
                        packageName = pkgName,
                        flags = OverriddenFlagsContainer(
                            boolValues = mapOf(it to if (boolSwitch.value) "1" else "0")
                        )
                    )
                }
            }

            if (integerFlags.value.isNotEmpty()) {
                integerFlags.value.forEach {
                    val parts = it.split("=")
                    if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
                        overrideFlagsUseCase.invoke(
                            packageName = pkgName,
                            flags = OverriddenFlagsContainer(
                                intValues = mapOf(parts[0] to parts[1])
                            )
                        )
                    }
                }
            }

            if (floatFlags.value.isNotEmpty()) {
                floatFlags.value.forEach {
                    val parts = it.split("=")
                    if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
                        overrideFlagsUseCase.invoke(
                            packageName = pkgName,
                            flags = OverriddenFlagsContainer(
                                floatValues = mapOf(parts[0] to parts[1])
                            )
                        )
                    }
                }
            }

            if (stringFlags.value.isNotEmpty()) {
                stringFlags.value.forEach {
                    val parts = it.split("=")
                    if (parts.size == 2 && parts[0].isNotEmpty() && parts[1].isNotEmpty()) {
                        overrideFlagsUseCase.invoke(
                            packageName = pkgName,
                            flags = OverriddenFlagsContainer(
                                stringValues = mapOf(parts[0] to parts[1])
                            )
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

