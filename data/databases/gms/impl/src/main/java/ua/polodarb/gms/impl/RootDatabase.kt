package ua.polodarb.gms.impl

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.IBinder
import android.util.Log
import com.topjohnwu.superuser.ipc.RootService
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteDatabase.OPEN_READWRITE
import io.requery.android.database.sqlite.SQLiteDatabase.openDatabase as openSQLiteDatabase
import ua.polodarb.common.Constants.DB_PATH_GMS
import ua.polodarb.common.Constants.DB_PATH_VENDING
import ua.polodarb.common.Constants.DB_PATH_WALLET
import ua.polodarb.gms.IRootDatabase
import android.net.LocalSocket
import android.net.LocalSocketAddress
import ua.polodarb.xposed.info.HookInfo
import java.io.File

class RootDatabase : RootService() {

    private lateinit var gmsDB: SQLiteDatabase
    private lateinit var vendingDB: SQLiteDatabase

    override fun onBind(intent: Intent): IBinder {
        try {
            gmsDB = openPhenotypeDatabase(DB_PATH_GMS)
            vendingDB = openPhenotypeDatabase(DB_PATH_VENDING)
        } catch (e: SQLiteException) {
            Log.e("RootDatabase", "Database not found", e)
            throw DatabaseNotFoundException("Database not found")
        }
        return object : IRootDatabase.Stub() {

            override fun getGmsPackages(): Map<String, String?> = this@RootDatabase.getGmsPackages()

            override fun getGooglePackages(): List<String> = this@RootDatabase.getGooglePackages()

            override fun getPhenotypeVersions(): Map<String, String> =
                this@RootDatabase.getPhenotypeVersions()

            override fun getXposedHookStates(): Map<String, String> =
                this@RootDatabase.getXposedHookStates()

            override fun getBoolFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getBoolFlags(pkgName)

            override fun getIntFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getIntFlags(pkgName)

            override fun getFloatFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getFloatFlags(pkgName)

            override fun getStringFlags(pkgName: String): Map<String, String> =
                this@RootDatabase.getStringFlags(pkgName)

            override fun getAllBoolFlags(): Map<String, String> =
                this@RootDatabase.getAllBoolFlags()

            override fun getAllIntFlags(): Map<String, String> =
                this@RootDatabase.getAllIntFlags()

            override fun getAllFloatFlags(): Map<String, String> =
                this@RootDatabase.getAllFloatFlags()

            override fun getAllStringFlags(): Map<String, String> =
                this@RootDatabase.getAllStringFlags()

            override fun getOverriddenBoolFlagsByPackage(pkgName: String?): Map<String?, String?> =
                this@RootDatabase.getOverriddenBoolFlagsByPackage(pkgName)

            override fun getOverriddenIntFlagsByPackage(pkgName: String): Map<String?, String?> =
                this@RootDatabase.getOverriddenIntFlagsByPackage(pkgName)

            override fun getOverriddenFloatFlagsByPackage(pkgName: String): Map<String?, String?> =
                this@RootDatabase.getOverriddenFloatFlagsByPackage(pkgName)

            override fun getOverriddenStringFlagsByPackage(pkgName: String): Map<String?, String?> =
                this@RootDatabase.getOverriddenStringFlagsByPackage(pkgName)

            override fun getAllOverriddenBoolFlags(): Map<String?, String?> =
                this@RootDatabase.getAllOverriddenBoolFlags()

            override fun getAllOverriddenIntFlags(): Map<String?, String?> =
                this@RootDatabase.getAllOverriddenIntFlags()

            override fun getAllOverriddenFloatFlags(): Map<String?, String?> =
                this@RootDatabase.getAllOverriddenFloatFlags()

            override fun getAllOverriddenStringFlags(): Map<String?, String?> =
                this@RootDatabase.getAllOverriddenStringFlags()

            override fun androidPackage(pkgName: String): String =
                this@RootDatabase.androidPackage(pkgName)

            override fun getUsers(): MutableList<String> =
                this@RootDatabase.getUsers()

            override fun getListByPackages(pkgName: String): List<String> =
                this@RootDatabase.getListByPackages(pkgName)

            override fun deleteAllOverriddenFlagsFromGMS() =
                this@RootDatabase.deleteAllOverriddenFlagsFromGMS()

            override fun deleteAllOverriddenFlagsFromPlayStore() =
                this@RootDatabase.deleteAllOverriddenFlagsFromPlayStore()

            override fun deleteRowByFlagName(packageName: String, name: String) =
                this@RootDatabase.deleteRowByFlagName(packageName, name)

            override fun deleteOverriddenFlagByPackage(packageName: String) =
                this@RootDatabase.deleteOverriddenFlagByPackage(packageName)

            override fun overrideFlag(
                packageName: String?,
                user: String?,
                name: String?,
                flagType: Int,
                intVal: String?,
                boolVal: String?,
                floatVal: String?,
                stringVal: String?,
                extensionVal: ByteArray?,
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

            override fun fixWalletAttestation(): Int =
                this@RootDatabase.fixWalletAttestation()
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

    fun getGooglePackages(): List<String> {
        if (isPhixitSchemaUsed()) return getPhixitPackages()

        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT P.androidPackageName\n" +
                    "FROM Packages P\n" +
                    "JOIN (\n" +
                    "    SELECT DISTINCT\n" +
                    "        SUBSTR(packageName, INSTR(packageName, '#') + 1) AS sub_package\n" +
                    "    FROM Flags\n" +
                    ") F ON P.androidPackageName = F.sub_package;", null
        )
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val item = cursor.getString(0)
            list.add(item)
        }
        cursor.close()
        return list
    }

    fun getUsers(): MutableList<String> {
        if (isPhixitSchemaUsed()) return mutableListOf("")

        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT user FROM Flags WHERE user IS NOT \"\";", null
        )
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val user = cursor.getString(0)
            list.add(user)
        }
        cursor.close()
        return list
    }

    fun getPhenotypeVersions(): Map<String, String> {
        return mapOf(
            TARGET_GMS_PACKAGE_NAME to gmsDB.version.toString(),
            TARGET_VENDING_PACKAGE_NAME to vendingDB.version.toString()
        )
    }

    fun getXposedHookStates(): Map<String, String> {
        return mapOf(
            TARGET_GMS_PACKAGE_NAME to xposedHookState(TARGET_GMS_PROCESS_NAME),
            TARGET_VENDING_PACKAGE_NAME to xposedHookState(TARGET_VENDING_PROCESS_NAME)
        )
    }

    fun getListByPackages(pkgName: String): List<String> {
        if (isPhixitSchemaUsed()) {
            return getPhixitPackages(pkgName)
        }

        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT packageName FROM Flags WHERE packageName LIKE '%$pkgName%';", null
        )
        val list = mutableListOf<String>()
        while (cursor.moveToNext()) {
            val item = cursor.getString(0)
            list.add(item)
        }
        cursor.close()
        return list
    }

    fun androidPackage(pkgName: String): String {
        if (isPhixitSchemaUsed()) return pkgName

        val cursor = gmsDB.rawQuery(
            "SELECT androidPackageName FROM Packages WHERE packageName = '$pkgName' LIMIT 1;", null
        )
        val androidPackage = if (cursor.moveToFirst()) return cursor.getString(0) else "" // todo
        cursor.close()
        return androidPackage
    }

    fun deleteAllOverriddenFlagsFromGMS() {
        gmsDB.execSQL(
            "DELETE FROM ${overrideTable(gmsDB)};"
        )
    }

    fun deleteAllOverriddenFlagsFromPlayStore() {
        vendingDB.execSQL(
            "DELETE FROM ${overrideTable(vendingDB)};"
        )
    }

    fun deleteRowByFlagName(
        packageName: String,
        name: String
    ) {
        val whereClause = "packageName = ? AND name = ?"
        val whereArgs = arrayOf(packageName, name)

        gmsDB.delete(overrideTable(gmsDB), whereClause, whereArgs)

        if (isVendingPackage(packageName)) {
            vendingDB.delete(overrideTable(vendingDB), whereClause, whereArgs)
        }
    }

    fun deleteOverriddenFlagByPackage(
        packageName: String
    ) {
        val whereClause = "packageName = ?"
        val whereArgs = arrayOf(packageName)

        gmsDB.delete(overrideTable(gmsDB), whereClause, whereArgs)

        if (isVendingPackage(packageName)) {
            vendingDB.delete(overrideTable(vendingDB), whereClause, whereArgs)
        }
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
        extensionVal: ByteArray?,
        committed: Int
    ) {
        if (packageName == null || name == null) return

        val values = ContentValues().apply {
            put("packageName", packageName)
            put("user", user ?: "")
            put("name", name)
            put("flagType", flagType)
            put("intVal", intVal)
            put("boolVal", boolVal)
            put("floatVal", floatVal)
            put("stringVal", stringVal)
            put("extensionVal", extensionVal)
            put("committed", committed)
        }

        gmsDB.insertWithOnConflict(overrideTable(gmsDB), null, values, SQLiteDatabase.CONFLICT_REPLACE)

        if (isVendingPackage(packageName)) {
            vendingDB.insertWithOnConflict(overrideTable(vendingDB), null, values, SQLiteDatabase.CONFLICT_REPLACE)
        }

        if (gmsDB.isPhixitSchemaUsed()) {
            applyPhixitOverridesToPackage(gmsDB, packageName)
        }
        if (isVendingPackage(packageName) && vendingDB.isPhixitSchemaUsed()) {
            applyPhixitOverridesToPackage(vendingDB, packageName)
        }
    }


    private fun getBoolFlags(pkgName: String): Map<String, String> {
        val list = mutableMapOf<String, String>()
        if (gmsDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(gmsDB, pkgName, PhixitFlag.Bool::class.java))
        } else {
            val gmsOverrideTable = overrideTable(gmsDB)
            val cursor = gmsDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.boolVal, f.boolVal) " +
                        "AS boolVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, boolVal FROM $gmsOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.boolVal IS NOT NULL " +
                        "ORDER BY f.name ASC;",
                null
            )
            while (cursor.moveToNext()) {
                list[cursor.getString(0)] = cursor.getString(1)
            }
            cursor.close()
        }

        if (vendingDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(vendingDB, pkgName, PhixitFlag.Bool::class.java))
        } else {
            val vendingOverrideTable = overrideTable(vendingDB)
            val cursorVending = vendingDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.boolVal, f.boolVal) " +
                        "AS boolVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, boolVal FROM $vendingOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.boolVal IS NOT NULL " +
                        "ORDER BY f.name ASC;",
                null
            )
            while (cursorVending.moveToNext()) {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            }
            cursorVending.close()
        }
        return list.toMap()
    }

    private fun getIntFlags(pkgName: String): Map<String, String> {
        val list = mutableMapOf<String, String>()
        if (gmsDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(gmsDB, pkgName, PhixitFlag.Int::class.java))
        } else {
            val gmsOverrideTable = overrideTable(gmsDB)
            val cursor = gmsDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.intVal, f.intVal) " +
                        "AS intVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, intVal FROM $gmsOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.intVal IS NOT NULL;",
                null
            )
            while (cursor.moveToNext()) {
                list[cursor.getString(0)] = cursor.getString(1)
            }
            cursor.close()
        }

        if (vendingDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(vendingDB, pkgName, PhixitFlag.Int::class.java))
        } else {
            val vendingOverrideTable = overrideTable(vendingDB)
            val cursorVending = vendingDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.intVal, f.intVal) " +
                        "AS intVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, intVal FROM $vendingOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.intVal IS NOT NULL;",
                null
            )
            while (cursorVending.moveToNext()) {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            }
            cursorVending.close()
        }
        return list.toMap()
    }

    private fun getFloatFlags(pkgName: String): Map<String, String> {
        val list = mutableMapOf<String, String>()
        if (gmsDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(gmsDB, pkgName, PhixitFlag.Float::class.java))
        } else {
            val gmsOverrideTable = overrideTable(gmsDB)
            val cursor = gmsDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.floatVal, f.floatVal) " +
                        "AS floatVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, floatVal FROM $gmsOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.floatVal IS NOT NULL;",
                null
            )
            while (cursor.moveToNext()) {
                list[cursor.getString(0)] = cursor.getString(1)
            }
            cursor.close()
        }

        if (vendingDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(vendingDB, pkgName, PhixitFlag.Float::class.java))
        } else {
            val vendingOverrideTable = overrideTable(vendingDB)
            val cursorVending = vendingDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.floatVal, f.floatVal) " +
                        "AS floatVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, floatVal FROM $vendingOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.floatVal IS NOT NULL;",
                null
            )
            while (cursorVending.moveToNext()) {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            }
            cursorVending.close()
        }
        return list.toMap()
    }

    private fun getStringFlags(pkgName: String): Map<String, String> {
        val list = mutableMapOf<String, String>()
        if (gmsDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(gmsDB, pkgName, PhixitFlag.StringValue::class.java))
        } else {
            val gmsOverrideTable = overrideTable(gmsDB)
            val cursor = gmsDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.stringVal, f.stringVal) " +
                        "AS stringVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, stringVal FROM $gmsOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.stringVal IS NOT NULL " +
                        "AND f.stringVal <> '';",
                null
            )
            while (cursor.moveToNext()) {
                list[cursor.getString(0)] = cursor.getString(1)
            }
            cursor.close()
        }

        if (vendingDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitFlagsByType(vendingDB, pkgName, PhixitFlag.StringValue::class.java))
        } else {
            val vendingOverrideTable = overrideTable(vendingDB)
            val cursorVending = vendingDB.rawQuery(
                "SELECT DISTINCT f.name, COALESCE(fo.stringVal, f.stringVal) " +
                        "AS stringVal FROM Flags f LEFT JOIN " +
                        "(SELECT name, stringVal FROM $vendingOverrideTable) fo " +
                        "ON f.name = fo.name " +
                        "WHERE f.packageName = '$pkgName' " + // pkgName
                        "AND f.stringVal IS NOT NULL " +
                        "AND f.stringVal <> '';",
                null
            )
            while (cursorVending.moveToNext()) {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            }
            cursorVending.close()
        }
        return list.toMap()
    }

    private fun getAllBoolFlags(): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT name, packageName" +
                    " FROM (" +
                    " SELECT name, packageName" +
                    " FROM Flags" +
                    " WHERE boolVal IS NOT NULL" +
                    " UNION" +
                    " SELECT name, packageName" +
                    " FROM ${overrideTable(gmsDB)}" +
                    " WHERE boolVal IS NOT NULL" +
                    ")" +
                    " GROUP BY packageName, name;",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT packageName, name" +
                    " FROM (" +
                    " SELECT packageName, name" +
                    " FROM Flags" +
                    " WHERE boolVal IS NOT NULL" +
                    " UNION" +
                    " SELECT packageName, name" +
                    " FROM ${overrideTable(vendingDB)}" +
                    " WHERE boolVal IS NOT NULL" +
                    ")" +
                    " GROUP BY packageName, name;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getAllIntFlags(): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT packageName, name" +
                    " FROM (" +
                    " SELECT packageName, name" +
                    " FROM Flags" +
                    " WHERE intVal IS NOT NULL" +
                    " UNION" +
                    " SELECT packageName, name" +
                    " FROM ${overrideTable(gmsDB)}" +
                    " WHERE intVal IS NOT NULL" +
                    ")" +
                    " GROUP BY packageName, name;",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT packageName, name" +
                    " FROM (" +
                    " SELECT packageName, name" +
                    " FROM Flags" +
                    " WHERE intVal IS NOT NULL" +
                    " UNION" +
                    " SELECT packageName, name" +
                    " FROM ${overrideTable(vendingDB)}" +
                    " WHERE intVal IS NOT NULL" +
                    ")" +
                    " GROUP BY packageName, name;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getAllFloatFlags(): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT packageName, name" +
                    " FROM (" +
                    " SELECT packageName, name" +
                    " FROM Flags" +
                    " WHERE floatVal IS NOT NULL" +
                    " UNION" +
                    " SELECT packageName, name" +
                    " FROM ${overrideTable(gmsDB)}" +
                    " WHERE floatVal IS NOT NULL" +
                    ")" +
                    " GROUP BY packageName, name;",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT packageName, name" +
                    " FROM (" +
                    " SELECT packageName, name" +
                    " FROM Flags" +
                    " WHERE floatVal IS NOT NULL" +
                    " UNION" +
                    " SELECT packageName, name" +
                    " FROM ${overrideTable(vendingDB)}" +
                    " WHERE floatVal IS NOT NULL" +
                    ")" +
                    "GROUP BY packageName, name;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getAllStringFlags(): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT packageName, name" +
                    " FROM (" +
                    " SELECT packageName, name" +
                    " FROM Flags" +
                    " WHERE stringVal IS NOT NULL" +
                    " UNION" +
                    " SELECT packageName, name" +
                    " FROM ${overrideTable(gmsDB)}" +
                    " WHERE stringVal IS NOT NULL" +
                    ")" +
                    "GROUP BY packageName, name;",
            null
        )
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT packageName, name" +
                    " FROM (" +
                    " SELECT packageName, name" +
                    " FROM Flags" +
                    " WHERE stringVal IS NOT NULL" +
                    " UNION" +
                    " SELECT packageName, name" +
                    " FROM ${overrideTable(vendingDB)}" +
                    " WHERE stringVal IS NOT NULL" +
                    ")" +
                    "GROUP BY packageName, name;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getOverriddenBoolFlagsByPackage(pkgName: String?): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, boolVal FROM ${overrideTable(gmsDB)} WHERE packageName = '$pkgName' AND boolVal IS NOT NULL;",
            null
        )
        val list = mutableMapOf<String?, String?>()
        if (cursor.moveToFirst()) {
            do {
                list[cursor.getString(0)] = cursor.getString(1)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, boolVal FROM ${overrideTable(vendingDB)} WHERE packageName = '$pkgName' AND boolVal IS NOT NULL;",
            null
        )
        if (cursorVending.moveToFirst()) {
            do {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            } while (cursorVending.moveToNext())
        }
        cursorVending.close()
        return list
    }


    private fun getOverriddenIntFlagsByPackage(pkgName: String): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, intVal FROM ${overrideTable(gmsDB)} WHERE packageName = '$pkgName' AND intVal IS NOT NULL;",
            null
        )
        val list = mutableMapOf<String?, String?>()
        if (cursor.moveToFirst()) {
            do {
                list[cursor.getString(0)] = cursor.getString(1)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, intVal FROM ${overrideTable(vendingDB)} WHERE packageName = '$pkgName' AND intVal IS NOT NULL;",
            null
        )
        if (cursorVending.moveToFirst()) {
            do {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            } while (cursorVending.moveToNext())
        }
        cursorVending.close()
        return list
    }

    private fun getOverriddenFloatFlagsByPackage(pkgName: String): Map<String?, String?> {  // todo: not used
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, floatVal FROM ${overrideTable(gmsDB)} WHERE packageName = '$pkgName' AND floatVal IS NOT NULL;",
            null
        )
        val list = mutableMapOf<String?, String?>()
        if (cursor.moveToFirst()) {
            do {
                list[cursor.getString(0)] = cursor.getString(1)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, floatVal FROM ${overrideTable(vendingDB)} WHERE packageName = '$pkgName' AND floatVal IS NOT NULL;",
            null
        )
        if (cursorVending.moveToFirst()) {
            do {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            } while (cursorVending.moveToNext())
        }
        cursorVending.close()
        return list
    }

    private fun getOverriddenStringFlagsByPackage(pkgName: String): Map<String?, String?> { // todo: not used
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, stringVal FROM ${overrideTable(gmsDB)} WHERE packageName = '$pkgName' AND stringVal IS NOT NULL;",
            null
        )
        val list = mutableMapOf<String?, String?>()
        if (cursor.moveToFirst()) {
            do {
                list[cursor.getString(0)] = cursor.getString(1)
            } while (cursor.moveToNext())
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, stringVal FROM ${overrideTable(vendingDB)} WHERE packageName = '$pkgName' AND stringVal IS NOT NULL;",
            null
        )
        if (cursorVending.moveToFirst()) {
            do {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            } while (cursorVending.moveToNext())
        }
        cursorVending.close()
        return list
    }

    fun getAllOverriddenBoolFlags(): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, boolVal\n" +
                    "FROM ${overrideTable(gmsDB)}\n" +
                    "WHERE name IS NOT NULL AND boolVal IS NOT NULL;\n",
            null
        )
        val list = mutableMapOf<String?, String?>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, boolVal\n" +
                    "FROM ${overrideTable(vendingDB)}\n" +
                    "WHERE name IS NOT NULL AND boolVal IS NOT NULL;\n",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    fun getAllOverriddenIntFlags(): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, intVal\n" +
                    "FROM ${overrideTable(gmsDB)}\n" +
                    "WHERE name IS NOT NULL AND intVal IS NOT NULL;\n",
            null
        )
        val list = mutableMapOf<String?, String?>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, intVal\n" +
                    "FROM ${overrideTable(vendingDB)}\n" +
                    "WHERE name IS NOT NULL AND intVal IS NOT NULL;\n",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    fun getAllOverriddenFloatFlags(): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, floatVal\n" +
                    "FROM ${overrideTable(gmsDB)}\n" +
                    "WHERE name IS NOT NULL AND floatVal IS NOT NULL;\n",
            null
        )
        val list = mutableMapOf<String?, String?>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, floatVal\n" +
                    "FROM ${overrideTable(vendingDB)}\n" +
                    "WHERE name IS NOT NULL AND floatVal IS NOT NULL;\n",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    fun getAllOverriddenStringFlags(): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, stringVal\n" +
                    "FROM ${overrideTable(gmsDB)}\n" +
                    "WHERE name IS NOT NULL AND stringVal IS NOT NULL;\n",
            null
        )
        val list = mutableMapOf<String?, String?>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT name, stringVal\n" +
                    "FROM ${overrideTable(vendingDB)}\n" +
                    "WHERE name IS NOT NULL AND stringVal IS NOT NULL;\n",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getGmsPackages(): Map<String, String?> {
        val list = mutableMapOf<String, String?>()
        if (gmsDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitPackageCounts(gmsDB))
        } else {
            val gmsOverrideTable = overrideTable(gmsDB)
            val cursor = gmsDB.rawQuery("SELECT f.packageName, COUNT(DISTINCT f.name) AS unique_name_count\n" +
                    "FROM (\n" +
                    "    SELECT packageName, name\n" +
                    "    FROM Flags\n" +
                    "    UNION ALL\n" +
                    "    SELECT packageName, name\n" +
                    "    FROM $gmsOverrideTable\n" +
                    ") AS f\n" +
                    "GROUP BY f.packageName;\n", null)
            while (cursor.moveToNext()) {
                list[cursor.getString(0)] = cursor.getString(1)
            }
            cursor.close()
        }

        if (vendingDB.isPhixitSchemaUsed()) {
            list.putAll(getPhixitPackageCounts(vendingDB))
        } else {
            val cursorVending = vendingDB.rawQuery(
                "SELECT packageName, COUNT(DISTINCT name) FROM Flags group by packageName",
                null
            )
            while (cursorVending.moveToNext()) {
                list[cursorVending.getString(0)] = cursorVending.getString(1)
            }
            cursorVending.close()
        }

        return list.toMap()
    }

    private fun isPhixitSchemaUsed(): Boolean {
        return gmsDB.isPhixitSchemaUsed()
    }

    private fun openPhenotypeDatabase(path: String): SQLiteDatabase {
        val db = openSQLiteDatabase(path, null, OPEN_READWRITE)
        if (db.isPhixitSchemaUsed()) {
            db.execSQL(CREATE_PHIXIT_OVERRIDE_TABLE_SQL)
        }
        return db
    }

    private fun SQLiteDatabase.isPhixitSchemaUsed(): Boolean {
        return version >= MINIMAL_PHENOTYPE_VERSION
    }

    private fun overrideTable(db: SQLiteDatabase): String {
        return if (db.isPhixitSchemaUsed()) PHIXIT_OVERRIDE_TABLE else LEGACY_OVERRIDE_TABLE
    }

    private fun isVendingPackage(packageName: String): Boolean {
        return packageName.contains("finsky") || packageName.contains("vending")
    }

    private fun xposedHookState(processName: String): String {
        val socketName = "${HookInfo.SOCKET_PREFIX}$processName"
        return try {
            LocalSocket().use { socket ->
                socket.connect(LocalSocketAddress(socketName, LocalSocketAddress.Namespace.ABSTRACT))
                socket.soTimeout = SOCKET_TIMEOUT_MS
                try {
                    socket.inputStream.bufferedReader().readText()
                } catch (_: Throwable) {
                    // Connection succeeded — hook IS running, but read failed (broken pipe).
                    // Return a minimal marker so the UI still shows "running".
                    HookInfo(processName = processName).serialize()
                }
            }
        } catch (_: Throwable) {
            ""
        }
    }

    private fun getPhixitPackages(filter: String? = null): List<String> {
        val packages = mutableListOf<String>()
        val whereClause = if (filter == null) "" else " WHERE name LIKE ?"
        val args = filter?.let { arrayOf("%$it%") }

        gmsDB.rawQuery(
            """
            SELECT scp.name
            FROM static_config_packages scp
            JOIN (
                SELECT name, MAX(rowid) AS rowid
                FROM static_config_packages
                $whereClause
                GROUP BY name
            ) latest ON latest.rowid = scp.rowid
            ORDER BY scp.name ASC;
            """.trimIndent(),
            args
        ).use { cursor ->
            while (cursor.moveToNext()) {
                packages.add(cursor.getString(0))
            }
        }

        return packages
    }

    private fun getPhixitPackageCounts(db: SQLiteDatabase): Map<String, String?> {
        val counts = linkedMapOf<String, String?>()
        db.rawQuery(
            """
            SELECT scp.name
            FROM static_config_packages scp
            JOIN (
                SELECT name, MAX(rowid) AS rowid
                FROM static_config_packages
                GROUP BY name
            ) latest ON latest.rowid = scp.rowid
            ORDER BY scp.name ASC;
            """.trimIndent(),
            null
        ).use { cursor ->
            while (cursor.moveToNext()) {
                counts[cursor.getString(0)] = null
            }
        }
        return counts
    }

    private fun <T : PhixitFlag> getPhixitFlagsByType(
        db: SQLiteDatabase,
        packageName: String,
        type: Class<T>
    ): Map<String, String> {
        val flags = linkedMapOf<String, String>()
        getPhixitFlags(db, packageName).forEach { flag ->
            if (type.isInstance(flag)) {
                flags[flag.name] = flag.toDisplayValue()
            }
        }

        // TODO!
//        readPhixitOverrides(db, packageName).forEach { flag ->
//            if (type.isInstance(flag)) {
//                flags[flag.name] = flag.toDisplayValue()
//            }
//        }

        return flags.toSortedMap()
    }

    private fun getPhixitFlags(db: SQLiteDatabase, packageName: String): List<PhixitFlag> {
        val flags = mutableListOf<PhixitFlag>()
        db.rawQuery(
            """
            SELECT pp.flags_content
            FROM param_partitions pp
            WHERE pp.static_config_package_id = (
                SELECT static_config_package_id
                FROM static_config_packages
                WHERE name = ?
                ORDER BY rowid DESC, static_config_package_id DESC
                LIMIT 1
            )
            ORDER BY pp.param_partition_id ASC;
            """.trimIndent(),
            arrayOf(packageName)
        ).use { cursor ->
            while (cursor.moveToNext()) {
                try {
                    flags += PhixitFlagsCodec.decode(cursor.getBlob(0))
                } catch (e: Exception) {
                    Log.e("RootDatabase", "Failed to decode Phixit flags for $packageName", e)
                }
            }
        }
        return flags
    }

    private fun readPhixitOverrides(db: SQLiteDatabase, packageName: String): List<PhixitFlag> {
        val flags = mutableListOf<PhixitFlag>()
        db.rawQuery(
            """
            SELECT name, flagType, intVal, boolVal, floatVal, stringVal, extensionVal
            FROM ${overrideTable(db)}
            WHERE packageName = ?;
            """.trimIndent(),
            arrayOf(packageName)
        ).use { cursor ->
            while (cursor.moveToNext()) {
                val name = cursor.getString(0)
                when (cursor.getInt(1)) {
                    0 -> flags += PhixitFlag.Bool(name, cursor.getString(3) == "1" || cursor.getString(3) == "true")
                    1 -> cursor.getString(2)?.toLongOrNull()?.let { flags += PhixitFlag.Int(name, it) }
                    2 -> cursor.getString(4)?.toDoubleOrNull()?.let {
                        flags += PhixitFlag.Float(name, java.lang.Double.doubleToRawLongBits(it))
                    }
                    3 -> cursor.getString(5)?.let { flags += PhixitFlag.StringValue(name, it) }
                    4 -> cursor.getBlob(6)?.let { flags += PhixitFlag.Extension(name, it) }
                }
            }
        }
        return flags
    }

    private fun applyPhixitOverridesToPackage(db: SQLiteDatabase, packageName: String) {
        val overrides = readPhixitOverrides(db, packageName)
        if (overrides.isEmpty()) return

        val overridesByName = overrides.associateBy { it.name }
        db.rawQuery(
            """
            SELECT pp.param_partition_id, pp.flags_content
            FROM param_partitions pp
            WHERE pp.static_config_package_id = (
                SELECT static_config_package_id
                FROM static_config_packages
                WHERE name = ?
                ORDER BY rowid DESC, static_config_package_id DESC
                LIMIT 1
            )
            ORDER BY pp.param_partition_id ASC;
            """.trimIndent(),
            arrayOf(packageName)
        ).use { cursor ->
            while (cursor.moveToNext()) {
                val partitionId = cursor.getInt(0)
                val current = try {
                    PhixitFlagsCodec.decode(cursor.getBlob(1))
                } catch (e: Exception) {
                    Log.e("RootDatabase", "Failed to decode Phixit partition $partitionId", e)
                    null
                }
                if (current != null) {
                    val merged = current.map { overridesByName[it.name] ?: it }.toMutableList()
                    val existingNames = merged.mapTo(mutableSetOf()) { it.name }
                    merged += overrides.filter { it.name !in existingNames }

                    db.execSQL(
                        "UPDATE param_partitions SET flags_content = ? WHERE param_partition_id = ?",
                        arrayOf(PhixitFlagsCodec.encode(merged.sortedBy { it.name.toLongOrNull() ?: Long.MAX_VALUE }), partitionId)
                    )
                }
            }
        }
    }

    private fun PhixitFlag.toDisplayValue(): String = when (this) {
        is PhixitFlag.Bool -> if (value) "1" else "0"
        is PhixitFlag.Int -> value.toString()
        is PhixitFlag.Float -> java.lang.Double.longBitsToDouble(value).toString()
        is PhixitFlag.StringValue -> value
        is PhixitFlag.Extension -> value.contentToString()
    }

    fun fixWalletAttestation(): Int {
        var db: SQLiteDatabase? = null
        return try {
            val dbFile = File(DB_PATH_WALLET)
            if (!dbFile.exists()) throw IllegalStateException("Wallet database not found")
            db = openSQLiteDatabase(dbFile.path, null, OPEN_READWRITE)
            db.execSQL("UPDATE Wallets SET fails_attestation = 0 WHERE fails_attestation != 0;")
            db.compileStatement("SELECT changes()").use { it.simpleQueryForLong().toInt() }
        } finally {
            db?.close()
        }
    }

    companion object {
        private const val MINIMAL_PHENOTYPE_VERSION = 1034

        private const val LEGACY_OVERRIDE_TABLE = "FlagOverrides"
        private const val PHIXIT_OVERRIDE_TABLE = "GmsFlagsOverrides"

        private const val TARGET_GMS_PACKAGE_NAME = "com.google.android.gms"
        private const val TARGET_GMS_PROCESS_NAME = "com.google.android.gms.persistent"
        private const val TARGET_VENDING_PACKAGE_NAME = "com.android.vending"
        private const val TARGET_VENDING_PROCESS_NAME = "com.android.vending"

        private const val SOCKET_TIMEOUT_MS = 2000

        private const val CREATE_PHIXIT_OVERRIDE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS $PHIXIT_OVERRIDE_TABLE (
                packageName TEXT NOT NULL,
                user TEXT,
                name TEXT NOT NULL,
                flagType INTEGER NOT NULL,
                intVal TEXT,
                boolVal TEXT,
                floatVal TEXT,
                stringVal TEXT,
                extensionVal BLOB,
                committed INTEGER NOT NULL DEFAULT 1,
                PRIMARY KEY(packageName, user, name)
            );
        """
    }

}

class DatabaseNotFoundException(message: String) : Exception(message)
