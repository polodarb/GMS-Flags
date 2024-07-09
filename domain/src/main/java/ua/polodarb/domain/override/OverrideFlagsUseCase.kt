package ua.polodarb.domain.override

import kotlinx.coroutines.flow.first
import tw.ktrssreader.utils.convertToByteArray
import ua.polodarb.byteUtils.ByteUtils
import ua.polodarb.domain.override.models.OverriddenFlagsContainer
import ua.polodarb.repository.databases.gms.GmsDBInteractor
import ua.polodarb.repository.databases.gms.GmsDBRepository
import java.util.Collections

class OverrideFlagsUseCase(
    private val repository: GmsDBRepository,
    private val interactor: GmsDBInteractor,
    private val byteUtils: ByteUtils,
) {

    private val usersList = Collections.synchronizedList(mutableListOf<String>())

    // TODO: Rewrite to OverriddenFlagsContainer
    suspend operator fun invoke(
        packageName: String,
        name: String,
        flagType: Int = 0,
        intVal: String? = null,
        boolVal: String? = null,
        floatVal: String? = null,
        stringVal: String? = null,
        extensionVal: String? = null,
        committed: Int = 0,
        clearData: Boolean = true
    ) {
        try {
            usersList.addAll(repository.getUsers().first())
            interactor.overrideFlag(
                packageName = packageName,
                name = name,
                usersList = usersList,
                clearData = clearData,
                boolVal = boolVal,
                intVal = intVal,
                floatVal = floatVal,
                stringVal = stringVal
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend operator fun invoke(
        packageName: String,
        flags: OverriddenFlagsContainer
    ) {
        try {
            usersList.addAll(repository.getUsers().first())
            with(flags) {
                boolValues?.let {
                    boolValues.forEach {
                        interactor.overrideFlag(
                            packageName = packageName,
                            name = it.key,
                            boolVal = it.value,
                            usersList = usersList,
                            clearData = false,
                        )
                    }
                }
                intValues?.let {
                    intValues.forEach {
                        interactor.overrideFlag(
                            packageName = packageName,
                            name = it.key,
                            intVal = it.value,
                            usersList = usersList,
                            clearData = false,
                        )
                    }
                }
                floatValues?.let {
                    floatValues.forEach {
                        interactor.overrideFlag(
                            packageName = packageName,
                            name = it.key,
                            floatVal = it.value,
                            usersList = usersList,
                            clearData = false,
                        )
                    }
                }
                stringValues?.let {
                    stringValues.forEach {
                        interactor.overrideFlag(
                            packageName = packageName,
                            name = it.key,
                            stringVal = it.value,
                            usersList = usersList,
                            clearData = false,
                        )
                    }
                }
                extValues?.let {
                    extValues.forEach {
                        interactor.overrideFlag(
                            packageName = packageName,
                            name = it.key,
                            extensionVal = byteUtils.convertToByteArray(it.value),
                            usersList = usersList,
                            clearData = false,
                        )
                    }
                }
            }
            interactor.clearPhenotypeCache(packageName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}