package ua.polodarb.gms.impl

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteException
import android.os.IBinder
import android.util.Log
import com.topjohnwu.superuser.ipc.RootService
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteDatabase.OPEN_READWRITE
import io.requery.android.database.sqlite.SQLiteDatabase.openDatabase
import ua.polodarb.common.Constants.DB_PATH_GMS
import ua.polodarb.common.Constants.DB_PATH_VENDING
import ua.polodarb.gms.IRootDatabase

class RootDatabase : RootService() {

    private lateinit var gmsDB: SQLiteDatabase
    private lateinit var vendingDB: SQLiteDatabase

    override fun onBind(intent: Intent): IBinder {
        try {
            gmsDB = openDatabase(DB_PATH_GMS, null, OPEN_READWRITE)
            vendingDB = openDatabase(DB_PATH_VENDING, null, OPEN_READWRITE)
        } catch (e: SQLiteException) {
            Log.e("RootDatabase", "Database not found", e)
            throw DatabaseNotFoundException("Database not found")
        }
        return object : IRootDatabase.Stub() {

            override fun getGmsPackages(): Map<String, String> = this@RootDatabase.getGmsPackages()

            override fun getGooglePackages(): List<String> = this@RootDatabase.getGooglePackages()

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

            override fun isPhixitSchemaUsed(): Boolean =
                this@RootDatabase.isPhixitSchemaUsed()

            override fun isFlagOverridesTableEmpty(): Boolean =
                this@RootDatabase.isFlagOverridesTableEmpty()

            override fun isDbFullyRecreated(): Boolean =
                this@RootDatabase.isDbFullyRecreated()

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

    fun getListByPackages(pkgName: String): List<String> {
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
        val cursor = gmsDB.rawQuery(
            "SELECT androidPackageName FROM Packages WHERE packageName = '$pkgName' LIMIT 1;", null
        )
        val androidPackage = if (cursor.moveToFirst()) return cursor.getString(0) else "" // todo
        cursor.close()
        return androidPackage
    }

    fun deleteAllOverriddenFlagsFromGMS() {
        gmsDB.execSQL(
            "DELETE FROM FlagOverrides;"
        )
    }

    fun deleteAllOverriddenFlagsFromPlayStore() {
        vendingDB.execSQL(
            "DELETE FROM FlagOverrides;"
        )
    }

    fun deleteRowByFlagName(
        packageName: String,
        name: String
    ) {
        val whereClause = "packageName = ? AND name = ?"
        val whereArgs = arrayOf(packageName, name)

        gmsDB.delete("FlagOverrides", whereClause, whereArgs)

        if (packageName.contains("finsky") || packageName.contains("vending")) {
            vendingDB.delete("FlagOverrides", whereClause, whereArgs)
        }
    }

    fun deleteOverriddenFlagByPackage(
        packageName: String
    ) {
        val whereClause = "packageName = ?"
        val whereArgs = arrayOf(packageName)

        gmsDB.delete("FlagOverrides", whereClause, whereArgs)

        if (packageName.contains("finsky") || packageName.contains("vending")) {
            vendingDB.delete("FlagOverrides", whereClause, whereArgs)
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
        val values = ContentValues().apply {
            put("packageName", packageName)
            put("user", user)
            put("name", name)
            put("flagType", flagType)
            put("intVal", intVal)
            put("boolVal", boolVal)
            put("floatVal", floatVal)
            put("stringVal", stringVal)
            put("extensionVal", extensionVal)
            put("committed", committed)
        }

        gmsDB.insertWithOnConflict("FlagOverrides", null, values, SQLiteDatabase.CONFLICT_REPLACE)

        if (packageName?.contains("finsky") == true || packageName?.contains("vending") == true) {
            vendingDB.insertWithOnConflict("FlagOverrides", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        }
    }


    private fun getBoolFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
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
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.boolVal, f.boolVal) " +
                    "AS boolVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, boolVal FROM FlagOverrides) fo " +
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
        return list.toMap()
    }

    private fun getIntFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
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
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.intVal, f.intVal) " +
                    "AS intVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, intVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.intVal IS NOT NULL;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getFloatFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
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
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.floatVal, f.floatVal) " +
                    "AS floatVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, floatVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + // pkgName
                    "AND f.floatVal IS NOT NULL;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getStringFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
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
        cursor.close()

        val cursorVending = vendingDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.stringVal, f.stringVal) " +
                    "AS stringVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, stringVal FROM FlagOverrides) fo " +
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
                    " FROM FlagOverrides" +
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
                    " FROM FlagOverrides" +
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
                    " FROM FlagOverrides" +
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
                    " FROM FlagOverrides" +
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
                    " FROM FlagOverrides" +
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
                    " FROM FlagOverrides" +
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
                    " FROM FlagOverrides" +
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
                    " FROM FlagOverrides" +
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
            "SELECT DISTINCT name, boolVal FROM FlagOverrides WHERE packageName = '$pkgName' AND boolVal IS NOT NULL;",
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
            "SELECT DISTINCT name, boolVal FROM FlagOverrides WHERE packageName = '$pkgName' AND boolVal IS NOT NULL;",
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
            "SELECT DISTINCT name, intVal FROM FlagOverrides WHERE packageName = '$pkgName' AND intVal IS NOT NULL;",
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
            "SELECT DISTINCT name, intVal FROM FlagOverrides WHERE packageName = '$pkgName' AND intVal IS NOT NULL;",
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
            "SELECT DISTINCT name, floatVal FROM FlagOverrides WHERE packageName = '$pkgName' AND floatVal IS NOT NULL;",
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
            "SELECT DISTINCT name, floatVal FROM FlagOverrides WHERE packageName = '$pkgName' AND floatVal IS NOT NULL;",
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
            "SELECT DISTINCT name, stringVal FROM FlagOverrides WHERE packageName = '$pkgName' AND stringVal IS NOT NULL;",
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
            "SELECT DISTINCT name, stringVal FROM FlagOverrides WHERE packageName = '$pkgName' AND stringVal IS NOT NULL;",
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
                    "FROM FlagOverrides\n" +
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
                    "FROM FlagOverrides\n" +
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
                    "FROM FlagOverrides\n" +
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
                    "FROM FlagOverrides\n" +
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
                    "FROM FlagOverrides\n" +
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
                    "FROM FlagOverrides\n" +
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
                    "FROM FlagOverrides\n" +
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
                    "FROM FlagOverrides\n" +
                    "WHERE name IS NOT NULL AND stringVal IS NOT NULL;\n",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun getGmsPackages(): Map<String, String> {
        val cursor = gmsDB.rawQuery("SELECT f.packageName, COUNT(DISTINCT f.name) AS unique_name_count\n" +
                "FROM (\n" +
                "    SELECT packageName, name\n" +
                "    FROM Flags\n" +
                "    UNION ALL\n" +
                "    SELECT packageName, name\n" +
                "    FROM FlagOverrides\n" +
                ") AS f\n" +
                "GROUP BY f.packageName;\n", null)
        val list = mutableMapOf<String, String>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()

        val cursorVending = vendingDB.rawQuery("SELECT packageName, COUNT(DISTINCT name) FROM Flags group by packageName", null)
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    private fun isFlagOverridesTableEmpty(): Boolean {
        val query = gmsDB.rawQuery("SELECT COUNT(*) FROM FlagOverrides", null)
        query.use { cursor ->
            if (cursor.moveToFirst()) {
                val count = cursor.getInt(0)
                return count == 0
            }
        }
        return false
    }

    // Checking for FlagOverrides table after a gms reset
    private fun isDbFullyRecreated(): Boolean {
        return try {
            val query = gmsDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", arrayOf("Flags"))

            query.use { cursor ->
                cursor.count > 0
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun isPhixitSchemaUsed(): Boolean {
        return try {
            val query1 = gmsDB.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", arrayOf("flag_overrides"))

            query1.use { cursor1 ->
                cursor1.count > 0
            }
        } catch (e: Exception) {
            false
        }
    }

}

class DatabaseNotFoundException(message: String) : Exception(message)