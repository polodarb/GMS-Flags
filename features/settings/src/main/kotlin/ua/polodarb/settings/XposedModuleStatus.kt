package ua.polodarb.settings

object XposedModuleStatus {
    const val GMS_PACKAGE_NAME = "com.google.android.gms"
    const val VENDING_PACKAGE_NAME = "com.android.vending"

    fun resolveState(version: Int?, hookState: String?): XposedTargetState {
        return when {
            version == null -> XposedTargetState.UNKNOWN
            version < MINIMAL_PHENOTYPE_VERSION -> XposedTargetState.LEGACY_SCHEMA
            hookState == XPOSED_STATE_RUNNING -> XposedTargetState.PHIXIT_RUNNING
            else -> XposedTargetState.PHIXIT_NOT_RUNNING
        }
    }

    private const val MINIMAL_PHENOTYPE_VERSION = 1034
    private const val XPOSED_STATE_RUNNING = "running"
}

enum class XposedTargetState {
    UNKNOWN,
    LEGACY_SCHEMA,
    PHIXIT_RUNNING,
    PHIXIT_NOT_RUNNING
}

data class XposedTargetStatus(
    val packageName: String,
    val label: String,
    val version: Int?,
    val state: XposedTargetState
)
