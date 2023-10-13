package ua.polodarb.gmsflags.data.repo

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.ui.screens.UiStates
import ua.polodarb.gmsflags.ui.screens.packagesScreen.ScreenUiStates

class GmsDBRepository(
    private val context: Context
) {

    private val gmsApplication = context as GMSApplication

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

    suspend fun getGmsPackages() = flow<ScreenUiStates> {
        emit(ScreenUiStates.Loading)

        delay(150)

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val list = (context as GMSApplication).getRootDatabase().gmsPackages
                if (list.isNotEmpty()) emit(ScreenUiStates.Success(list))
                else emit(ScreenUiStates.Error())
            }
        }

    }

    suspend fun getBoolFlags(packageName: String, delay: Boolean) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val boolFlags = gmsApplication.getRootDatabase().getBoolFlags(packageName)
        if (boolFlags.isNotEmpty())
            emit(UiStates.Success(boolFlags))
    }

    suspend fun getIntFlags(packageName: String, delay: Boolean) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val intFlags = gmsApplication.getRootDatabase().getIntFlags(packageName)
        if (intFlags.isNotEmpty())
            emit(UiStates.Success(intFlags))
    }

    suspend fun getFloatFlags(packageName: String, delay: Boolean) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val floatFlags = gmsApplication.getRootDatabase().getFloatFlags(packageName)
        if (floatFlags.isNotEmpty())
            emit(UiStates.Success(floatFlags))
    }

    suspend fun getStringFlags(packageName: String, delay: Boolean) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)

        val stringFlags = gmsApplication.getRootDatabase().getStringFlags(packageName)
        if (stringFlags.isNotEmpty())
            emit(UiStates.Success(stringFlags))

    }

    fun getOverriddenBoolFlagsByPackage(packageName: String): UiStates<MutableMap<String, String>> {
        val boolOverriddenFlags =
            gmsApplication.getRootDatabase().getOverriddenBoolFlagsByPackage(packageName)
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

    fun getUsers(): MutableList<String> = gmsApplication.getRootDatabase().users

    fun androidPackage(pkgName: String): String {
        val usersList = gmsApplication.getRootDatabase().androidPackage(pkgName)
        return usersList
    }

    fun deleteRowByFlagName(packageName: String, name: String) {
        gmsApplication.getRootDatabase().deleteRowByFlagName(packageName, name)
    }

    fun deleteOverriddenFlagByPackage(packageName: String) {
        gmsApplication.getRootDatabase().deleteOverriddenFlagByPackage(packageName)
    }

}