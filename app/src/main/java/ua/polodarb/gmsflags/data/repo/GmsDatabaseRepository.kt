package ua.polodarb.gmsflags.data.repo

import android.content.ContentValues
import android.util.Log
import io.requery.android.database.sqlite.SQLiteDatabase
import kotlin.system.measureTimeMillis

class GmsDatabaseRepository(
    val gmsDB: SQLiteDatabase,
    val vendingDB: SQLiteDatabase
) {

    /**
     * @return List of phenotype packages and the count of flags in these packages from the Flags table
     */
    fun getGmsPackages(): Map<String, String> {
        val cursor = gmsDB.rawQuery("SELECT packageName, COUNT(DISTINCT name) FROM Flags group by packageName", null)
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

    /**
     * @return List of all google apps packages (To create a list of apps)
     */
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

    /**
     * @return List of all **Boolean flags** with values in selected package name
     * @param pkgName package name
     */
    fun getBoolFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.boolVal, f.boolVal) " +
                    "AS boolVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, boolVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + 
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
                    "WHERE f.packageName = '$pkgName' " + 
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

    /**
     * @return List of all **Integer flags** with values in selected package name
     * @param pkgName package name
     */
    fun getIntFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.intVal, f.intVal) " +
                    "AS intVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, intVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + 
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
                    "WHERE f.packageName = '$pkgName' " + 
                    "AND f.intVal IS NOT NULL;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    /**
     * @return List of all **Float flags** with values in selected package name
     * @param pkgName package name
     */
    fun getFloatFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.floatVal, f.floatVal) " +
                    "AS floatVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, floatVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + 
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
                    "WHERE f.packageName = '$pkgName' " + 
                    "AND f.floatVal IS NOT NULL;",
            null
        )
        while (cursorVending.moveToNext()) {
            list[cursorVending.getString(0)] = cursorVending.getString(1)
        }
        cursorVending.close()
        return list.toMap()
    }

    /**
     * @return List of all **String flags** with values in selected package name
     * @param pkgName package name
     */
    fun getStringFlags(pkgName: String): Map<String, String> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT f.name, COALESCE(fo.stringVal, f.stringVal) " +
                    "AS stringVal FROM Flags f LEFT JOIN " +
                    "(SELECT name, stringVal FROM FlagOverrides) fo " +
                    "ON f.name = fo.name " +
                    "WHERE f.packageName = '$pkgName' " + 
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
                    "WHERE f.packageName = '$pkgName' " + 
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

    /**
     * @return List of overridden **Boolean flags** with values in selected package name
     * @param pkgName Package name
     */
    fun getOverriddenBoolFlagsByPackage(pkgName: String?): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, boolVal FROM FlagOverrides WHERE packageName = '$pkgName';",
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
            "SELECT DISTINCT name, boolVal FROM FlagOverrides WHERE packageName = '$pkgName';",
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

    /**
     * @return List of overridden **Integer flags** with values in selected package name
     * @param pkgName Package name
     */
    fun getOverriddenIntFlagsByPackage(pkgName: String): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, intVal FROM FlagOverrides WHERE packageName = \"$pkgName\";",
            null
        )
        val list = mutableMapOf<String?, String?>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()
        return list.toMap()
    }

    /**
     * @return List of overridden **Float flags** with values in selected package name
     * @param pkgName Package name
     */
    fun getOverriddenFloatFlagsByPackage(pkgName: String): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, floatVal FROM FlagOverrides WHERE packageName = \"$pkgName\";",
            null
        )
        val list = mutableMapOf<String?, String?>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()
        return list.toMap()
    }

    /**
     * @return List of overridden **String flags** with values in selected package name
     * @param pkgName Package name
     */
    fun getOverriddenStringFlagsByPackage(pkgName: String): Map<String?, String?> {
        val cursor = gmsDB.rawQuery(
            "SELECT DISTINCT name, stringVal FROM FlagOverrides WHERE packageName = \"$pkgName\";",
            null
        )
        val list = mutableMapOf<String?, String?>()
        while (cursor.moveToNext()) {
            list[cursor.getString(0)] = cursor.getString(1)
        }
        cursor.close()
        return list.toMap()
    }

    /**
     * @return List of all **Boolean flags** from FlagsOverride table
     */
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

    /**
     * @return List of all **Integer flags** from FlagsOverride table
     */
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

    /**
     * @return List of all **Float flags** from FlagsOverride table
     */
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

    /**
     * @return List of all **String flags** from FlagsOverride table
     */
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

    /**
     * @return Get **App Package Name** from selected sub-package name (package#package)
     */
    fun androidPackage(pkgName: String): String {
        val cursor = gmsDB.rawQuery(
            "SELECT androidPackageName FROM Packages WHERE packageName = '$pkgName' LIMIT 1;", null
        )
        val androidPackage = if (cursor.moveToFirst()) return cursor.getString(0) else "" // todo
        cursor.close()
        return androidPackage
    }

    /**
     * @return Get user's Google Account (Only mail). It is required to override flags.
     */
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

    /**
     * @param pkgName Package name
     * @return Get package names of selected app (package)
     */
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

//    /**
//     * This method deletes all overridden flags from GMS Database
//     */
//    fun deleteAllOverriddenFlagsFromGMS() {
//        gmsDB.execSQL(
//            "DELETE FROM FlagOverrides;"
//        )
//    }

//    /**
//     * This method deletes all overridden flags from PlayStore (Vending) Database
//     */
//    fun deleteAllOverriddenFlagsFromPlayStore() {
//        vendingDB.execSQL(
//            "DELETE FROM FlagOverrides;"
//        )
//    }

    /**
     * This method overrides **one flag** to GMS and Vending databases.
     * @param packageName Package name for which the flag is to be deleted
     * @param name Name of the flag
     */
    fun deleteRowByFlagName(
        pkgName: String,
        flagName: String
    ) {
        val whereClause = "packageName = ? AND name = ?"
        val whereArgs = arrayOf(pkgName, flagName)

        gmsDB.delete("FlagOverrides", whereClause, whereArgs)

        if (pkgName.contains("finsky") || pkgName.contains("vending")) {
            vendingDB.delete("FlagOverrides", whereClause, whereArgs)
        }
    }

    /**
     * This method deletes all overridden flag by package.
     * @param packageName Package name for which the flags is to be deleted
     */
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

    /**
     * This method overrides **one flag** to GMS and Vending databases.
     * @param packageName Package name of the app to override flag
     * @param user Google Account of user (It is required to override flags)
     * @param name Name of the flag
     * @param flagType Type of the flag to override (Default: 0)
     * @param intVal Value of the int type flag
     * @param boolVal Value of the bool type flag
     * @param floatVal value of the float type flag
     * @param stringVal Value of the string type flag
     * @param extensionVal Value of the extension type flag (Default: 0)
     */
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
            put("floatVal", floatVal)
            put("stringVal", stringVal)
            put("extensionVal", extensionVal)
            put("committed", committed)
        }
        gmsDB.insertWithOnConflict(
            "FlagOverrides",
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        if (packageName?.contains("finsky") == true || packageName?.contains("vending") == true) {
            vendingDB.insertWithOnConflict("FlagOverrides", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        }
    }

}