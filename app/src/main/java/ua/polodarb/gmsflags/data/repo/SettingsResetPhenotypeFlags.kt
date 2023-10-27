package ua.polodarb.gmsflags.data.repo

import io.requery.android.database.sqlite.SQLiteDatabase

class SettingsResetPhenotypeFlags(
    val gmsDB: SQLiteDatabase,
    val vendingDB: SQLiteDatabase,
    ) {

    fun deleteAllOverriddenFlagsFromGMS() {
        gmsDB.execSQL(
            "DELETE FROM FlagOverrides;"
        )
    }

    /**
     * This method deletes all overridden flags from PlayStore (Vending) Database
     */
    fun deleteAllOverriddenFlagsFromPlayStore() {
        vendingDB.execSQL(
            "DELETE FROM FlagOverrides;"
        )
    }

}