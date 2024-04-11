package ua.polodarb.settings

object LocalConstants {
    const val PLAY_STORE_PKG = "com.android.vending"
    const val STOP_PLAY_STORE_CMD = "am force-stop $PLAY_STORE_PKG"
    const val CLEAN_PLAY_STORE_CMD = "rm -rf /data/data/$PLAY_STORE_PKG/files/experiment*"
    const val PHOTOS_PKG = "com.google.android.apps.photos"
    const val STOP_PHOTOS_CMD = "am force-stop $PHOTOS_PKG"
    const val CLEAN_PHOTOS_CMD = "rm -rf /data/data/$PHOTOS_PKG/shared_prefs/*phenotype*"
}