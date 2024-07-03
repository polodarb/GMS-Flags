package ua.polodarb.repository.impl.databases.gms

import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.invoke
import ua.polodarb.repository.databases.gms.GmsDBInteractor
import ua.polodarb.repository.databases.gms.GmsDBRepository

class GmsDBInteractorImpl(
    private val repository: GmsDBRepository
): GmsDBInteractor {

    override suspend fun overrideFlag(
        packageName: String,
        name: String,
        flagType: Int,
        intVal: String?,
        boolVal: String?,
        floatVal: String?,
        stringVal: String?,
        extensionVal: ByteArray?,
        committed: Int,
        clearData: Boolean,
        usersList: List<String>
    ) = Dispatchers.IO {
        with(repository) {
            deleteRowByFlagName(packageName, name)
            overrideFlag(
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
                overrideFlag(
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
        }
        if (clearData) clearPhenotypeCache(packageName)
    }

    override suspend fun clearPhenotypeCache(pkgName: String) {
        val androidPkgName = repository.getAndroidPackage(pkgName).first()
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