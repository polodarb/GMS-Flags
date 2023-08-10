package ua.polodarb.gmsflags.data.repo

import android.content.Context
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.di.GMSApplication
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeBooleanUiStates
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeOtherTypesUiStates

class DatabaseRepository(
    private val context: Context
) {

    suspend fun getGmsPackages() = flow<ScreenUiStates> {

        delay(200)

        val list = (context as GMSApplication).getRootDatabase().gmsPackages

        if (list.isNotEmpty()) emit(ScreenUiStates.Success(list))

        else emit(ScreenUiStates.Error())

    }

    suspend fun getBoolFlags(packageName: String) = flow<FlagChangeBooleanUiStates> {
        emit(FlagChangeBooleanUiStates.Loading)

        delay(200)

        val boolFlagsMap = mutableMapOf<String, Boolean>()

        val gmsApplication = context as GMSApplication

        val boolFlags = gmsApplication.getRootDatabase().getBoolFlags(packageName)

        if (boolFlags.isNotEmpty()) {
            for (flag in boolFlags) {
                val parts = flag.split("|")
                if (parts.size == 2) {
                    val text = parts[0]
                    val value = parts[1].toInt() == 1

                    boolFlagsMap[text] = value
                }
            }
        } else {
            emit(FlagChangeBooleanUiStates.Error())
        }

        emit(FlagChangeBooleanUiStates.Success(boolFlagsMap))

    }

    suspend fun getIntFlags(packageName: String) = flow<FlagChangeOtherTypesUiStates> {
        emit(FlagChangeOtherTypesUiStates.Loading)

        delay(200)

        val intFlagsMap = mutableMapOf<String, String>()

        val gmsApplication = context as GMSApplication

        val intFlags = gmsApplication.getRootDatabase().getIntFlags(packageName)

        if (intFlags.isNotEmpty()) {
            for (flag in intFlags) {
                val parts = flag.split("|")
                if (parts.size == 2) {
                    val text = parts[0]
                    val value = parts[1]

                    intFlagsMap[text] = value
                }
            }
        } else {
            emit(FlagChangeOtherTypesUiStates.Error())
        }

        emit(FlagChangeOtherTypesUiStates.Success(intFlagsMap))

    }

    suspend fun getFloatFlags(packageName: String) = flow<FlagChangeOtherTypesUiStates> {
        emit(FlagChangeOtherTypesUiStates.Loading)

        delay(200)

        val floatFlagsMap = mutableMapOf<String, String>()

        val gmsApplication = context as GMSApplication

        val floatFlags = gmsApplication.getRootDatabase().getFloatFlags(packageName)

        if (floatFlags.isNotEmpty()) {
            for (flag in floatFlags) {
                val parts = flag.split("|")
                if (parts.size == 2) {
                    val text = parts[0]
                    val value = parts[1]

                    floatFlagsMap[text] = value
                }
            }
        } else {
            emit(FlagChangeOtherTypesUiStates.Error())
        }

        emit(FlagChangeOtherTypesUiStates.Success(floatFlagsMap))

    }

    suspend fun getStringFlags(packageName: String) = flow<FlagChangeOtherTypesUiStates> {
        emit(FlagChangeOtherTypesUiStates.Loading)

        delay(200)

        val stringFlagsMap = mutableMapOf<String, String>()

        val gmsApplication = context as GMSApplication

        val stringFlags = gmsApplication.getRootDatabase().getStringFlags(packageName)

        if (stringFlags.isNotEmpty()) {
            for (flag in stringFlags) {
                val parts = flag.split("|")
                if (parts.size == 2) {
                    val text = parts[0]
                    val value = parts[1]

                    stringFlagsMap[text] = value
                }
            }
        } else {
            emit(FlagChangeOtherTypesUiStates.Error())
        }

        emit(FlagChangeOtherTypesUiStates.Success(stringFlagsMap))

    }

}