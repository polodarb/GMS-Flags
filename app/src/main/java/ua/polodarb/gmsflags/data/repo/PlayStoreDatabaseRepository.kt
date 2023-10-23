package ua.polodarb.gmsflags.data.repo

import android.content.ContentValues
import android.util.Log
import io.requery.android.database.sqlite.SQLiteDatabase
import kotlin.system.measureTimeMillis

class PlayStoreDatabaseRepository(
    private val vendingDB: SQLiteDatabase
) {

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

        if (packageName?.contains("finsky") == true || packageName?.contains("vending") == true) {
            vendingDB.insertWithOnConflict("FlagOverrides", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        }

    }

}