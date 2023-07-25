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
            override fun getGmsPackages() = this@RootDatabase.getGmsPackages()
        }
    }

    private fun getGmsPackages() {
        val cursor = db.rawQuery(GET_GMS_PACKAGES, null)
        while (cursor.moveToNext()) {
            Log.e(TAG, cursor.getString(0))
        }

    }
    private companion object {
        const val TAG = "RootDatabase"
        const val DB_PATH = "/data/data/com.google.android.gms/databases/phenotype.db"
        const val GET_GMS_PACKAGES = "SELECT packageName, COUNT(*) FROM Flags group by packageName"
    }
}