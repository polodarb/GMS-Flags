package ua.polodarb.gmsflags.data.repo.interactors

import android.content.Context
import android.util.Log
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke
import ua.polodarb.gmsflags.data.repo.GmsDBRepository

class GmsDBInteractor(
    private val context: Context,
    private val repository: GmsDBRepository
) {

    suspend fun overrideFlag(
        packageName: String,
        name: String,
        flagType: Int = 0,
        intVal: String? = null,
        boolVal: String? = null,
        floatVal: String? = null,
        stringVal: String? = null,
        extensionVal: String? = null,
        committed: Int = 0,
        clearData: Boolean = true,
        usersList: List<String>
    ) = Dispatchers.IO {
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
        if (clearData) clearPhenotypeCache(packageName)
    }

    suspend fun clearPhenotypeCache(pkgName: String) {
        repository.androidPackage(pkgName).collect { androidPkgName ->
            Shell.cmd("am force-stop $androidPkgName").exec()
            Shell.cmd("rm -rf /data/data/$androidPkgName/files/phenotype").exec()
            if (pkgName.contains("finsky") || pkgName.contains("vending")) {
                Shell.cmd("rm -rf /data/data/com.android.vending/files/experiment*").exec()
                Shell.cmd("am force-stop com.android.vending").exec()
            }
            if (pkgName.contains("com.google.android.apps.photos")) {
                Shell.cmd("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/phenotype*")
                    .exec()
                Shell.cmd("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/com.google.android.apps.photos.phenotype.xml")
                    .exec()
                Shell.cmd("am force-stop com.google.android.apps.photos").exec()
            }
            repeat(3) {
                Shell.cmd("am start -a android.intent.action.MAIN -n $androidPkgName &").exec()
                Shell.cmd("am force-stop $androidPkgName").exec()
            }
        }
    }

}