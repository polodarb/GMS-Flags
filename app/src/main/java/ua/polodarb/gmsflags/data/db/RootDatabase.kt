package ua.polodarb.gmsflags.data.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.IBinder
import android.os.Process
import android.util.Log
import com.topjohnwu.superuser.ipc.RootService
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteDatabase.OPEN_READWRITE
import io.requery.android.database.sqlite.SQLiteDatabase.openDatabase
import ua.polodarb.gmsflags.IRootDatabase

@SuppressLint("SdCardPath")
class RootDatabase : RootService() {

    private lateinit var db: SQLiteDatabase

    override fun onBind(intent: Intent): IBinder {
        try {
            db = openDatabase(DB_PATH, null, OPEN_READWRITE)
        } catch (e: SQLiteException) {
            // TODO: Handle exception
            Log.wtf(TAG, e.message)
        }
        return object : IRootDatabase.Stub() {

            override fun getGmsPackages(): Map<String, String> = this@RootDatabase.getGmsPackages()

            override fun getBoolFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getBoolFlags(pkgName)

            override fun getIntFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getIntFlags(pkgName)

            override fun getFloatFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getFloatFlags(pkgName)

            override fun getStringFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getStringFlags(pkgName)

            override fun androidPackage(pkgName: String): String =
                this@RootDatabase.androidPackage(pkgName)

            override fun getUsers(): MutableList<String> =
                this@RootDatabase.getUsers()

            override fun deleteRowByFlagName(packageName: String, name: String) =
                this@RootDatabase.deleteRowByFlagName(packageName, name)

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
            ) {
                return this@RootDatabase.overrideFlag(
                    packageName,
                    user,
                    name,
                    flagType,
                    intVal,
                    boolVal,
                    floatVal,
                    stringVal,
                    extensionVal,
                    committed
                )
            }
        }
    }

    override fun onUnbind(intent: Intent): Boolean {
        if (db.isOpen) db.close()
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        if (db.isOpen) db.close()
        super.onDestroy()
    }

    fun getUsers(): MutableList<String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT user FROM Flags WHERE user IS NOT \"\";", null
        )
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val user = cursor.getString(0)
            list.add(user)
        }
        return list
    }

    fun androidPackage(pkgName: String): String {
        val cursor = db.rawQuery(
            "SELECT androidPackageName FROM Packages WHERE packageName = '$pkgName' LIMIT 1;", null
        )
        val androidPackage = if (cursor.moveToFirst()) return cursor.getString(0) else "" // todo
        cursor.close()
        return androidPackage
    }

    fun deleteRowByFlagName(
        packageName: String,
        name: String
    ) {
        val whereClause = "packageName = ? AND name = ?"
        val whereArgs = arrayOf(packageName, name)

        db.delete("FlagOverrides", whereClause, whereArgs)
    }

    fun overrideFlag(
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
    ) {
        val values = ContentValues().apply {
            put("packageName", packageName)
            put("user", user)
            put("name", name)
            put("flagType", flagType)
            put("intVal", intVal)
            put("boolVal", boolVal)
            put("floatVal", floatVal) // todo: check type
            put("stringVal", stringVal)
            put("extensionVal", extensionVal)
            put("committed", committed)
        }

        db.insertWithOnConflict("FlagOverrides", null, values, SQLiteDatabase.CONFLICT_REPLACE)

    }

    private fun getBoolFlags(pkgName: String): Map<String, String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.boolVal, f.boolVal) " +
                    "AS boolVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, boolVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.boolVal IS NOT NULL " +
                    "ORDER BY f.name ASC;",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        return list.toMap()
    }

    private fun getIntFlags(pkgName: String): Map<String, String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.intVal, f.intVal) " +
                    "AS intVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, intVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.intVal IS NOT NULL;",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        return list.toMap()
    }

    private fun getFloatFlags(pkgName: String): Map<String, String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.floatVal, f.floatVal) " +
                    "AS floatVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, floatVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.floatVal IS NOT NULL;",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        return list.toMap()
    }

    private fun getStringFlags(pkgName: String): Map<String, String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.stringVal, f.stringVal) " +
                    "AS stringVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, stringVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.stringVal IS NOT NULL " +
                    "AND f.stringVal <> '';",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        return list.toMap()
    }

    private fun getGmsPackages(): Map<String, String> {
        val cursor = db.rawQuery(GET_GMS_PACKAGES, null)
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        return list.toMap()
    }

    private companion object {
        const val TAG = "RootDatabase"
        const val DB_PATH = "/data/data/com.google.android.gms/databases/phenotype.db"
        const val GET_GMS_PACKAGES =
            "SELECT packageName, COUNT(DISTINCT name) FROM Flags group by packageName"
    }
}