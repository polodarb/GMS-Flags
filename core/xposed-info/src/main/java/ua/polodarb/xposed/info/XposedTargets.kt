package ua.polodarb.xposed.info

object XposedTargets {
    const val GMS_PACKAGE_NAME = "com.google.android.gms"
    const val GMS_PROCESS_NAME = "com.google.android.gms.persistent"
    const val VENDING_PACKAGE_NAME = "com.android.vending"
    const val VENDING_PROCESS_NAME = "com.android.vending"

    val TARGET_PACKAGES = setOf(
        GMS_PACKAGE_NAME to GMS_PROCESS_NAME,
        VENDING_PACKAGE_NAME to VENDING_PROCESS_NAME,
    )
}
