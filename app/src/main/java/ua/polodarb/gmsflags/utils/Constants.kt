package ua.polodarb.gmsflags.utils

import android.annotation.SuppressLint

@SuppressLint("SdCardPath")
object Constants {
    const val DB_PATH_GMS = "/data/data/com.google.android.gms/databases/phenotype.db"
    const val DB_PATH_VENDING = "/data/data/com.android.vending/databases/phenotype.db"
    const val GMS_DATABASE_CRASH_MSG = "GmsFlagsRootService is not initialized yet."
}
