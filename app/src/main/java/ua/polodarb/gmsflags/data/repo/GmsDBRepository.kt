package ua.polodarb.gmsflags.data.repo

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.invoke
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.repository.uiStates.UiStates

class GmsDBRepository(
    private val context: Context
) {

    private val gmsApplication = context as GMSApplication
    private val rootDatabase get() = gmsApplication.getRootDatabase()

    fun overrideFlag(
        packageName: String?,
        user: String?,
        name: String?,
        flagType: Int,
        intVal: String?,
        boolVal: String?,
        floatVal: String?,
        stringVal: String?,
        extensionVal: String?,
        committed: Int
    ) {
        gmsApplication.getRootDatabase().overrideFlag(
            packageName,
            user,
            name,
            flagType,
            intVal,
            boolVal,
            floatVal,
            stringVal,
            extensionVal,
            committed
        )
    }

    suspend fun getGmsPackages() = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        delay(150)

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val list = Dispatchers.IO { rootDatabase.gmsPackages }
                if (list.isNotEmpty()) emit(ua.polodarb.repository.uiStates.UiStates.Success(list))
                else emit(ua.polodarb.repository.uiStates.UiStates.Error())
            }
        }
    }

    fun getBoolFlags(
        packageName: String, delay: Boolean
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(ua.polodarb.repository.uiStates.UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolFlags = Dispatchers.IO { rootDatabase.getBoolFlags(packageName) }
                emit(ua.polodarb.repository.uiStates.UiStates.Success(boolFlags))
            }
        }
    }

    fun getIntFlags(
        packageName: String, delay: Boolean
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(ua.polodarb.repository.uiStates.UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val intFlags = Dispatchers.IO { rootDatabase.getIntFlags(packageName) }
                emit(ua.polodarb.repository.uiStates.UiStates.Success(intFlags))
            }
        }
    }

    fun getFloatFlags(
        packageName: String, delay: Boolean
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(ua.polodarb.repository.uiStates.UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val floatFlags = Dispatchers.IO { rootDatabase.getFloatFlags(packageName) }
                emit(ua.polodarb.repository.uiStates.UiStates.Success(floatFlags))
            }
        }
    }

    fun getStringFlags(
        packageName: String, delay: Boolean
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(ua.polodarb.repository.uiStates.UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val stringFlags = Dispatchers.IO { rootDatabase.getStringFlags(packageName) }
                emit(ua.polodarb.repository.uiStates.UiStates.Success(stringFlags))
            }
        }
    }

    fun getOverriddenBoolFlagsByPackage(
        packageName: String
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenBoolFlagsByPackage(packageName)
                }
                emit(ua.polodarb.repository.uiStates.UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getOverriddenIntFlagsByPackage(
        packageName: String
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenIntFlagsByPackage(packageName)
                }
                emit(ua.polodarb.repository.uiStates.UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getOverriddenFloatFlagsByPackage(
        packageName: String
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenFloatFlagsByPackage(packageName)
                }
                emit(ua.polodarb.repository.uiStates.UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getOverriddenStringFlagsByPackage(
        packageName: String
    ) = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags =
                    rootDatabase.getOverriddenStringFlagsByPackage(packageName)
                emit(ua.polodarb.repository.uiStates.UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getAllOverriddenBoolFlags() = flow<ua.polodarb.repository.uiStates.UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = rootDatabase.allOverriddenBoolFlags
                emit(ua.polodarb.repository.uiStates.UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getUsers() = flow<MutableList<String>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(Dispatchers.IO { rootDatabase.users })
            }
        }
    }

    suspend fun androidPackage(pkgName: String) = flow<String> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(Dispatchers.IO {
                    rootDatabase.androidPackage(pkgName)
                })
            }
        }
    }

    suspend fun deleteRowByFlagName(packageName: String, name: String) = Dispatchers.IO {
        gmsApplication.getRootDatabase().deleteRowByFlagName(packageName, name)
    }

    suspend fun deleteOverriddenFlagByPackage(packageName: String) = Dispatchers.IO {
        gmsApplication.getRootDatabase().deleteOverriddenFlagByPackage(packageName)
    }
}
