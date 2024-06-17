package ua.polodarb.domain

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.repository.databases.gms.GmsDBInteractor
import ua.polodarb.repository.databases.gms.GmsDBRepository
import java.util.Collections

class OverrideFlagsUseCase(
    private val repository: GmsDBRepository,
    private val interactor: GmsDBInteractor
) {

    private val usersList = Collections.synchronizedList(mutableListOf<String>())

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
        Log.d("usecase", "invoke()")
        try {
            Log.d("usecase", "enter to try")
            usersList.addAll(repository.getUsers().first())
            Log.d("usecase", "user is init")
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
            Log.d("usecase", "flags overrided")
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("GMS Flags", "Flag override error: ${e.message}")
        }
    }

    private suspend fun initUsers() {
        withContext(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                repository.getUsers().collect {
                    usersList.addAll(it)
                }
            }
        }
    }
}