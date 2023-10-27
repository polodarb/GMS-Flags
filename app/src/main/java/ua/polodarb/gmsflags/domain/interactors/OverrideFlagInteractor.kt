package ua.polodarb.gmsflags.domain.interactors

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.GmsDatabaseRepository

class OverrideFlagInteractor(
    private val repository: GmsDatabaseRepository
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
        clearData: Boolean = true
    ) = withContext(Dispatchers.IO) {
//        repository.deleteRowByFlagName(packageName, name)
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
        for (i in repository.getUsers().first()) {
            repository.overrideFlag(
                packageName = packageName,
                user = i.toString(),
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

    suspend fun clearPhenotypeCache(pkgName: String) = withContext(Dispatchers.IO) {
        val androidPkgName = repository.androidPackage(pkgName)
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