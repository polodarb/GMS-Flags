package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.repo.DatabaseRepository

class SuggestionScreenViewModel(
    private val repository: DatabaseRepository
) : ViewModel() {

    private val usersList = mutableListOf<String>()

    fun initUsers() {
        usersList.clear()
        usersList.addAll(repository.getUsers())
    }

    fun clearPhenotypeCache(pkgName: String) {
        val androidPkgName = repository.androidPackage(pkgName)
        CoroutineScope(Dispatchers.IO).launch {
            Shell.cmd("am force-stop $androidPkgName").exec()
            Shell.cmd("rm -rf /data/data/$androidPkgName/files/phenotype").exec()
            repeat(3) {
                Shell.cmd("am start -a android.intent.action.MAIN -n $androidPkgName &").exec()
                Shell.cmd("am force-stop $androidPkgName").exec()
            }
        }
    }

    fun overrideFlag(
        packageName: String,
        name: String,
        flagType: Int = 0,
        intVal: String? = null,
        boolVal: String? = null,
        floatVal: String? = null,
        stringVal: String? = null,
        extensionVal: String? = null,
        committed: Int = 0
    ) {
        repository.deleteRowByFlagName(packageName, name)
        repository.overrideFlag(
            packageName = packageName,
            user = "",
            name = name,
            flagType = flagType,
            intVal = intVal,
            boolVal = boolVal,
            floatVal = floatVal,
            stringVal = stringVal,
            extensionVal = extensionVal,
            committed = committed
        )
        for (i in usersList) {
            repository.overrideFlag(
                packageName = packageName,
                user = i,
                name = name,
                flagType = flagType,
                intVal = intVal,
                boolVal = boolVal,
                floatVal = floatVal,
                stringVal = stringVal,
                extensionVal = extensionVal,
                committed = committed
            )
        }
        clearPhenotypeCache(packageName)
    }

}