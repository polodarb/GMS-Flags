package ua.polodarb.gmsflags.data.repo

import android.content.Context
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.di.GMSApplication
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeUiStates

class DatabaseRepository(
    private val context: Context
) {

    private val gmsApplication = context as GMSApplication

    fun overrideFlag(
        packageName: String,
        user: String,
        name: String,
        flagType: String?,
        intVal: String?,
        boolVal: String?,
        floatVal: String?,
        stringVal: String?,
        extensionVal: String?,
        committed: String
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

        val list = (context as GMSApplication).getRootDatabase().gmsPackages

        if (list.isNotEmpty()) emit(ScreenUiStates.Success(list))

        else emit(ScreenUiStates.Error())

    }

    suspend fun getBoolFlags(packageName: String) = flow<FlagChangeUiStates> {
        emit(FlagChangeUiStates.Loading)

        delay(200)

        val boolFlags = gmsApplication.getRootDatabase().getBoolFlags(packageName)

        if (boolFlags.isEmpty())
            emit(FlagChangeUiStates.Error())

        emit(FlagChangeUiStates.Success(boolFlags))

    }

    suspend fun getIntFlags(packageName: String) = flow<FlagChangeUiStates> {
        emit(FlagChangeUiStates.Loading)

        delay(200)

        val intFlags = gmsApplication.getRootDatabase().getIntFlags(packageName)

        if (intFlags.isEmpty())
            emit(FlagChangeUiStates.Error())

        emit(FlagChangeUiStates.Success(intFlags))

    }

    suspend fun getFloatFlags(packageName: String) = flow<FlagChangeUiStates> {
        emit(FlagChangeUiStates.Loading)

        delay(200)

        val floatFlags = gmsApplication.getRootDatabase().getFloatFlags(packageName)

        if (floatFlags.isEmpty())
            emit(FlagChangeUiStates.Error())

        emit(FlagChangeUiStates.Success(floatFlags))

    }

    suspend fun getStringFlags(packageName: String) = flow<FlagChangeUiStates> {
        emit(FlagChangeUiStates.Loading)

        delay(200)

        val stringFlags = gmsApplication.getRootDatabase().getStringFlags(packageName)

        if (stringFlags.isEmpty())
            emit(FlagChangeUiStates.Error())

        emit(FlagChangeUiStates.Success(stringFlags))

    }

    fun getUsers(): MutableList<String> = gmsApplication.getRootDatabase().users

    fun androidPackage(pkgName: String): String {

        val users = gmsApplication.getRootDatabase().androidPackage(pkgName)
        return users
    }
    
    fun deleteRowByFlagName(packageName: String, name: String) {
        gmsApplication.getRootDatabase().deleteRowByFlagName(packageName, name)
    }

}