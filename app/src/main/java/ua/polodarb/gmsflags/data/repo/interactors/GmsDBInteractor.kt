package ua.polodarb.gmsflags.data.repo.interactors

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.polodarb.gmsflags.data.repo.GmsDBRepository

class GmsDBInteractor(
    private val context: Context,
    private val repository: GmsDBRepository
) {

    fun clearPhenotypeCache(pkgName: String) {
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