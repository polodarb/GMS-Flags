package ua.polodarb.gmsflags.data.repo

import android.content.ContentValues
import android.util.Log
import io.requery.android.database.sqlite.SQLiteDatabase
import kotlin.system.measureTimeMillis

class GmsDatabaseRepository(
    private val gmsDB: SQLiteDatabase
) {

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

        val measureNanoTime = measureTimeMillis {
            gmsDB.insertWithOnConflict(
                "FlagOverrides",
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE
            )
        }
        Log.d("db_test", measureNanoTime.toString())

    }

}