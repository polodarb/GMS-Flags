package ua.polodarb.settings

import ua.polodarb.xposed.info.HookInfo
import ua.polodarb.xposed.info.XposedTargets

object XposedModuleStatus {
    val GMS_PACKAGE_NAME = XposedTargets.GMS_PACKAGE_NAME
    val VENDING_PACKAGE_NAME = XposedTargets.VENDING_PACKAGE_NAME

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

    private const val MINIMAL_PHENOTYPE_VERSION = 1001
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
