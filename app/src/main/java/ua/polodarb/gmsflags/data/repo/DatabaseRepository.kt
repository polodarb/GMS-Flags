package ua.polodarb.gmsflags.data.repo

import android.content.Context
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.di.GMSApplication
import ua.polodarb.gmsflags.ui.screens.ScreenUiStates

class DatabaseRepository(
    private val context: Context
) {

    suspend fun getGmsPackages() = flow<ScreenUiStates> {
        val list = (context as GMSApplication).rootDatabase.gmsPackages

        if (list.isNotEmpty()) emit(ScreenUiStates.Success(list))
        else emit(ScreenUiStates.Error())

    }

    suspend fun getBoolFlags(pkgName: String) = flow<ScreenUiStates> {
        val list = (context as GMSApplication).rootDatabase.getBoolFlags(pkgName)

        if (list.isNotEmpty()) emit(ScreenUiStates.Success(list))
        else emit(ScreenUiStates.Error())

    }

}