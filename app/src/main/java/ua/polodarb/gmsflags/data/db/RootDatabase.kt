package ua.polodarb.gmsflags.data.db

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.IBinder
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

            override fun getGmsPackages(): MutableList<String> = this@RootDatabase.getGmsPackages()

            override fun getBoolFlags(pkgName: String): MutableList<String> =
                this@RootDatabase.getBoolFlags(pkgName)

            override fun getIntFlags(pkgName: String): MutableList<String> =
                this@RootDatabase.getIntFlags(pkgName)

            override fun getFloatFlags(pkgName: String): MutableList<String> =
                this@RootDatabase.getFloatFlags(pkgName)

            override fun getStringFlags(pkgName: String): MutableList<String> =
                this@RootDatabase.getStringFlags(pkgName)

//            override fun getExtensionsFlags(pkgName: String): MutableList<String> =
//                this@RootDatabase.getExtensionsFlags(pkgName)
        }
    }

//    private fun getExtensionsFlags(pkgName: String): MutableList<String> {
//        val cursor = db.rawQuery(
//            "SELECT DISTINCT f.name, \n" +
//                    "COALESCE(CAST(fo.extensionVal AS TEXT), CAST(HEX(f.extensionVal) AS TEXT)) AS extensionVal \n" +
//                    "FROM Flags f \n" +
//                    "LEFT JOIN (SELECT name, HEX(extensionVal) AS extensionVal FROM FlagOverrides) fo \n" +
//                    "ON f.name = fo.name \n" +
//                    "WHERE f.packageName = '$pkgName' \n" +
//                    "AND f.extensionVal IS NOT NULL;\n",
//            null
//        )
//        val list = mutableListOf<String>()
//        while (cursor.moveToNext()) {
//            list.add("${cursor.getString(0)}|${cursor.getString(1)}")
//        }
//        return list
//    }

    private fun getBoolFlags(pkgName: String): MutableList<String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.boolVal, f.boolVal) " +
                    "AS boolVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, boolVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.boolVal IS NOT NULL;",
            null
        )
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            list.add("${cursor.getString(0)}|${cursor.getString(1)}")
        }
        return list
    }

    private fun getIntFlags(pkgName: String): MutableList<String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.intVal, f.intVal) " +
                    "AS intVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, intVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.intVal IS NOT NULL;",
            null
        )
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            list.add("${cursor.getString(0)}|${cursor.getString(1)}")
        }
        return list
    }

    private fun getFloatFlags(pkgName: String): MutableList<String> {
        val cursor = db.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.floatVal, f.floatVal) " +
                    "AS floatVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, floatVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.floatVal IS NOT NULL;",
            null
        )
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            list.add("${cursor.getString(0)}|${cursor.getString(1)}")
        }
        return list
    }

    private fun getStringFlags(pkgName: String): MutableList<String> {
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
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            list.add("${cursor.getString(0)}|${cursor.getString(1)}")
        }
        return list
    }

    private fun getGmsPackages(): MutableList<String> {
        val cursor = db.rawQuery(GET_GMS_PACKAGES, null)
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            list.add("${cursor.getString(0)}|${cursor.getString(1)}")
        }
        return list
    }

    private companion object {
        const val TAG = "RootDatabase"
        const val DB_PATH = "/data/data/com.google.android.gms/databases/phenotype.db"
        const val GET_GMS_PACKAGES = "SELECT packageName, COUNT(DISTINCT name) FROM Flags group by packageName"
    }
}