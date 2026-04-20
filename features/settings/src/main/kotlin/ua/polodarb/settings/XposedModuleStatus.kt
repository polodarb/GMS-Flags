package ua.polodarb.settings

import ua.polodarb.xposed.info.HookInfo

object XposedModuleStatus {
    const val GMS_PACKAGE_NAME = "com.google.android.gms"
    const val VENDING_PACKAGE_NAME = "com.android.vending"

    fun resolveState(
        version: Int?,
        hookInfo: HookInfo?,
        lastHookInfo: HookInfo? = null,
    ): XposedTargetState {
        return when {
            version == null -> XposedTargetState.UNKNOWN
            version < MINIMAL_PHENOTYPE_VERSION -> XposedTargetState.LEGACY_SCHEMA
            hookInfo != null -> XposedTargetState.PHIXIT_RUNNING
            lastHookInfo != null -> XposedTargetState.PHIXIT_STOPPED
            else -> XposedTargetState.PHIXIT_NOT_RUNNING
        }
    }

    private const val MINIMAL_PHENOTYPE_VERSION = 1034
}

enum class XposedTargetState {
    UNKNOWN,
    LEGACY_SCHEMA,
    PHIXIT_RUNNING,
    PHIXIT_STOPPED,
    PHIXIT_NOT_RUNNING,
}

data class XposedTargetStatus(
    val packageName: String,
    val label: String,
    val version: Int?,
    val state: XposedTargetState,
    val hookInfo: HookInfo? = null,
)
