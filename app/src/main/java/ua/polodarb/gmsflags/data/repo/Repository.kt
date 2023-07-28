package ua.polodarb.gmsflags.data.repo

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.IRootDatabase
import ua.polodarb.gmsflags.di.GMSApplication
import ua.polodarb.gmsflags.ui.MainActivity
import ua.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreenUiStates

class Repository(
    private val context: Context
) {

    suspend fun getGmsPackages() = flow<PackagesScreenUiStates> {

        val list = (context as GMSApplication).rootDatabase.gmsPackages

        if (list.isNotEmpty()) {
            emit(PackagesScreenUiStates.Success(list))
        } else {
            emit(PackagesScreenUiStates.Error())
        }
    }

}