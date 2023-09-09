package ua.polodarb.gmsflags.core

object Constants {
    const val TAG = "RootDatabase"
    const val DB_PATH = "/data/data/com.google.android.gms/databases/phenotype.db"
    const val GET_GMS_PACKAGES =
        "SELECT packageName, COUNT(DISTINCT name) FROM Flags group by packageName"
}