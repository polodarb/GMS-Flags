package ua.polodarb.gmsflags.data.databases.gms

import android.content.Intent
import android.os.IBinder
import com.topjohnwu.superuser.ipc.RootService
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteDatabase.OPEN_READWRITE
import io.requery.android.database.sqlite.SQLiteDatabase.openDatabase
import ua.polodarb.gmsflags.IRootDatabase
import ua.polodarb.gmsflags.data.repo.GmsDatabaseRepository
import ua.polodarb.gmsflags.domain.interactors.OverrideFlagInteractor
import ua.polodarb.gmsflags.utils.Constants.DB_PATH_GMS
import ua.polodarb.gmsflags.utils.Constants.DB_PATH_VENDING

class GmsFlagsRootService : RootService() {

    private lateinit var gmsDB: SQLiteDatabase
    private lateinit var vendingDB: SQLiteDatabase
    private lateinit var repository: GmsDatabaseRepository
    private lateinit var interactor: OverrideFlagInteractor

    override fun onBind(intent: Intent): IBinder {
        gmsDB = openDatabase(DB_PATH_GMS, null, OPEN_READWRITE)
        vendingDB = openDatabase(DB_PATH_VENDING, null, OPEN_READWRITE)
        repository = GmsDatabaseRepository(gmsDB, vendingDB)
        interactor = OverrideFlagInteractor(repository)
        return object : IRootDatabase.Stub() {

            override fun getGmsPackages(): Map<String, String> = repository.getGmsPackages()

            override fun getGooglePackages(): List<String> = repository.getGooglePackages()

            override fun getBoolFlags(pkgName: String): Map<String, String> =
                repository.getBoolFlags(pkgName)

            override fun getIntFlags(pkgName: String): Map<String, String> =
                repository.getIntFlags(pkgName)

            override fun getFloatFlags(pkgName: String): Map<String, String> =
                repository.getFloatFlags(pkgName)

            override fun getStringFlags(pkgName: String): Map<String, String> =
                repository.getStringFlags(pkgName)

            override fun getOverriddenBoolFlagsByPackage(pkgName: String?): Map<String?, String?> =
                repository.getOverriddenBoolFlagsByPackage(pkgName)

            override fun getOverriddenIntFlagsByPackage(pkgName: String): Map<String?, String?> =
                repository.getOverriddenIntFlagsByPackage(pkgName)

            override fun getOverriddenFloatFlagsByPackage(pkgName: String): Map<String?, String?> =
                repository.getOverriddenFloatFlagsByPackage(pkgName)

            override fun getOverriddenStringFlagsByPackage(pkgName: String): Map<String?, String?> =
                repository.getOverriddenStringFlagsByPackage(pkgName)

            override fun getAllOverriddenBoolFlags(): Map<String?, String?> =
                repository.getAllOverriddenBoolFlags()

            override fun getAllOverriddenIntFlags(): Map<String?, String?> =
                repository.getAllOverriddenIntFlags()

            override fun getAllOverriddenFloatFlags(): Map<String?, String?> =
                repository.getAllOverriddenFloatFlags()

            override fun getAllOverriddenStringFlags(): Map<String?, String?> =
                repository.getAllOverriddenStringFlags()

            override fun androidPackage(pkgName: String): String =
                repository.androidPackage(pkgName)

            override fun getUsers(): MutableList<String> =
                repository.getUsers()

            override fun getListByPackages(pkgName: String): List<String> =
                repository.getListByPackages(pkgName)

            override fun deleteAllOverriddenFlagsFromGMS() =
                repository.deleteAllOverriddenFlagsFromGMS()

            override fun deleteAllOverriddenFlagsFromPlayStore() =
                repository.deleteAllOverriddenFlagsFromPlayStore()

            override fun deleteRowByFlagName(pkgName: String, flagName: String) =
                repository.deleteRowByFlagName(pkgName, flagName)

            override fun deleteOverriddenFlagByPackage(packageName: String) =
                repository.deleteOverriddenFlagByPackage(packageName)

            override fun overrideFlag(
                packageName: String?,
                user: String?,
                name: String?,
                flagType: Int,
                intVal: String?,
                boolVal: String?,
                floatVal: String?,
                stringVal: String?,
                extensionVal: String?,
                committed: Int
            ) = repository.overrideFlag(
                packageName = packageName,
                user = user,
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

    override fun onUnbind(intent: Intent): Boolean {
        if (gmsDB.isOpen) gmsDB.close()
        if (vendingDB.isOpen) vendingDB.close()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        if (gmsDB.isOpen) gmsDB.close()
        if (vendingDB.isOpen) vendingDB.close()
        super.onDestroy()
    }

}