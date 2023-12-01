package ua.polodarb.gmsflags.data.repo

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.invoke
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.ui.screens.UiStates

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

    suspend fun getGmsPackages() = flow<UiStates<Map<String, String>>> {
        delay(150)

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val list = Dispatchers.IO { rootDatabase.gmsPackages }
                if (list.isNotEmpty()) emit(UiStates.Success(list))
                else emit(UiStates.Error())
            }
        }
    }

    fun getBoolFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolFlags = Dispatchers.IO { rootDatabase.getBoolFlags(packageName) }
                emit(UiStates.Success(boolFlags))
            }
        }
    }

    fun getIntFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val intFlags = Dispatchers.IO { rootDatabase.getIntFlags(packageName) }
                emit(UiStates.Success(intFlags))
            }
        }
    }

    fun getFloatFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val floatFlags = Dispatchers.IO { rootDatabase.getFloatFlags(packageName) }
                emit(UiStates.Success(floatFlags))
            }
        }
    }

    fun getStringFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val stringFlags = Dispatchers.IO { rootDatabase.getStringFlags(packageName) }
                emit(UiStates.Success(stringFlags))
            }
        }
    }

    fun getOverriddenBoolFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenBoolFlagsByPackage(packageName)
                }
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getOverriddenIntFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenIntFlagsByPackage(packageName)
                }
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getOverriddenFloatFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenFloatFlagsByPackage(packageName)
                }
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getOverriddenStringFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags =
                    rootDatabase.getOverriddenStringFlagsByPackage(packageName)
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getAllOverriddenBoolFlags() = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = rootDatabase.allOverriddenBoolFlags
                emit(UiStates.Success(boolOverriddenFlags))
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
