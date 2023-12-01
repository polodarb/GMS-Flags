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
                val list = Dispatchers.IO {
                    (context as GMSApplication).getRootDatabase().gmsPackages
                }
                if (list.isNotEmpty()) emit(UiStates.Success(list))
                else emit(UiStates.Error())
            }
        }
    }

    fun getBoolFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val boolFlags = Dispatchers.IO { rootDatabase.getBoolFlags(packageName) }
        emit(UiStates.Success(boolFlags))
    }

    fun getIntFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val intFlags = Dispatchers.IO { rootDatabase.getIntFlags(packageName) }
        emit(UiStates.Success(intFlags))
    }

    fun getFloatFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val floatFlags = Dispatchers.IO { rootDatabase.getFloatFlags(packageName) }
        emit(UiStates.Success(floatFlags))
    }

    fun getStringFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val stringFlags = Dispatchers.IO {
            gmsApplication.getRootDatabase().getStringFlags(packageName)
        }
        emit(UiStates.Success(stringFlags))
    }

    fun getOverriddenBoolFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    gmsApplication.getRootDatabase().getOverriddenBoolFlagsByPackage(packageName)
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
                    gmsApplication.getRootDatabase().getOverriddenIntFlagsByPackage(packageName)
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
                    gmsApplication.getRootDatabase()
                        .getOverriddenFloatFlagsByPackage(packageName)
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
                    gmsApplication.getRootDatabase()
                        .getOverriddenStringFlagsByPackage(packageName)
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getAllOverriddenBoolFlags() = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags =
                    gmsApplication.getRootDatabase().allOverriddenBoolFlags
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getUsers() = flow<MutableList<String>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(Dispatchers.IO { gmsApplication.getRootDatabase().users } )
            }
        }
    }

    suspend fun androidPackage(pkgName: String) = flow<String> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(Dispatchers.IO {
                    gmsApplication.getRootDatabase().androidPackage(pkgName)
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
