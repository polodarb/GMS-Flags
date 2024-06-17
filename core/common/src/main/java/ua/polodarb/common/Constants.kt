package ua.polodarb.common

import android.annotation.SuppressLint

@SuppressLint("SdCardPath")
object Constants {
    const val DB_PATH_GMS = "/data/data/com.google.android.gms/databases/phenotype.db"
    const val DB_PATH_VENDING = "/data/data/com.android.vending/databases/phenotype.db"
    const val GMS_DB_CRASH_MSG = "RootDatabase is not initialized yet."
    const val GMS_DB_CRASH_MSG_PHIXIT = "GMS DB used a phixit-schema. GMS Flags doesn't support it."
}
