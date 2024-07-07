package ua.polodarb.repository.impl.databases.gms

import android.util.Log
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

        // Function to execute shell commands and log errors if any
        fun execShellCommand(command: String) {
            try {
                Shell.cmd(command).exec()
            } catch (e: Exception) {
                Log.e("ClearPhenotypeCache", "Error executing command: $command", e)
            }
        }

        // General cleanup for the given package
        execShellCommand("am force-stop $androidPkgName")
        execShellCommand("rm -rf /data/data/$androidPkgName/files/phenotype")

        // Specific cleanup for certain packages
        when {
            pkgName.contains("finsky") || pkgName.contains("vending") -> {
                execShellCommand("rm -rf /data/data/com.android.vending/files/experiment*")
                execShellCommand("am force-stop com.android.vending")
            }
            pkgName.contains("com.google.android.dialer") -> {
                execShellCommand("rm -rf /data/data/com.google.android.dialer/files/phenotype*")
                execShellCommand("rm -rf /data/data/com.google.android.dialer/shared_prefs/dialer_phenotype_flags.xml")
            }
            pkgName.contains("com.google.android.apps.photos") -> {
                execShellCommand("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/phenotype*")
                execShellCommand("rm -rf /data/data/com.google.android.apps.photos/shared_prefs/com.google.android.apps.photos.phenotype.xml")
                execShellCommand("am force-stop com.google.android.apps.photos")
            }
        }

        // Restart the application 3 times
        repeat(3) {
            execShellCommand("am force-stop $androidPkgName")
            execShellCommand("am start -a android.intent.action.MAIN -n $androidPkgName &")
        }
    }

}