package ua.polodarb.gmsflags.data.repo

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.ui.screens.UiStates

class GmsDBRepository(
    private val context: Context
) {

    private val gmsApplication = context as GMSApplication

    suspend fun overrideFlag(
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
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
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
        }
    }

    suspend fun getGmsPackages() = flow<UiStates<Map<String, String>>> {
        delay(150)

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val list = (context as GMSApplication).getRootDatabase().gmsPackages
                if (list.isNotEmpty()) emit(UiStates.Success(list))
                else emit(UiStates.Error())
            }
        }

    }

    suspend fun getBoolFlags(packageName: String, delay: Boolean) =
        flow<UiStates<Map<String, String>>> {
            if (delay) delay(200)

            val boolFlags = gmsApplication.getRootDatabase().getBoolFlags(packageName)
            if (boolFlags.isNotEmpty())
                emit(UiStates.Success(boolFlags))
        }

    suspend fun getIntFlags(packageName: String, delay: Boolean) =
        flow<UiStates<Map<String, String>>> {
            if (delay) delay(200)

            val intFlags = gmsApplication.getRootDatabase().getIntFlags(packageName)
            if (intFlags.isNotEmpty())
                emit(UiStates.Success(intFlags))
        }

    suspend fun getFloatFlags(packageName: String, delay: Boolean) =
        flow<UiStates<Map<String, String>>> {
            if (delay) delay(200)

            val floatFlags = gmsApplication.getRootDatabase().getFloatFlags(packageName)
            if (floatFlags.isNotEmpty())
                emit(UiStates.Success(floatFlags))
        }

    suspend fun getStringFlags(packageName: String, delay: Boolean) =
        flow<UiStates<Map<String, String>>> {
            if (delay) delay(200)

            val stringFlags = gmsApplication.getRootDatabase().getStringFlags(packageName)
            if (stringFlags.isNotEmpty())
                emit(UiStates.Success(stringFlags))

        }

    fun getOverriddenBoolFlagsByPackage(packageName: String): UiStates<Map<String, String>> {
        val boolOverriddenFlags =
            gmsApplication.getRootDatabase().getOverriddenBoolFlagsByPackage(packageName)
        return (UiStates.Success(boolOverriddenFlags))
    }

    fun getOverriddenIntFlagsByPackage(packageName: String): UiStates<Map<String, String>> {
        val boolOverriddenFlags =
            gmsApplication.getRootDatabase().getOverriddenIntFlagsByPackage(packageName)
        return (UiStates.Success(boolOverriddenFlags))
    }

    fun getOverriddenFloatFlagsByPackage(packageName: String): UiStates<Map<String, String>> {
        val boolOverriddenFlags =
            gmsApplication.getRootDatabase().getOverriddenFloatFlagsByPackage(packageName)
        return (UiStates.Success(boolOverriddenFlags))
    }

    fun getOverriddenStringFlagsByPackage(packageName: String): UiStates<Map<String, String>> {
        val boolOverriddenFlags =
            gmsApplication.getRootDatabase().getOverriddenStringFlagsByPackage(packageName)
        return (UiStates.Success(boolOverriddenFlags))
    }

    fun getAllOverriddenBoolFlags() = flow<UiStates<Map<String, String>>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = gmsApplication.getRootDatabase().allOverriddenBoolFlags
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    fun getUsers() = flow<MutableList<String>> {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(gmsApplication.getRootDatabase().users)
            }
        }
    }

    fun androidPackage(pkgName: String): String {
        val usersList = gmsApplication.getRootDatabase().androidPackage(pkgName)
        return usersList
    }

    suspend fun deleteRowByFlagName(packageName: String, name: String) {
        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                gmsApplication.getRootDatabase().deleteRowByFlagName(packageName, name)
            }
        }
    }

    fun deleteOverriddenFlagByPackage(packageName: String) {
        gmsApplication.getRootDatabase().deleteOverriddenFlagByPackage(packageName)
    }

}