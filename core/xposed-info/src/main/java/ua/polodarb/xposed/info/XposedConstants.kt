package ua.polodarb.xposed.info

object XposedConstants {
    const val XPOSED_DIR = "gmsflags_xposed"
    const val HOOK_STATUS_FILE_NAME = "hook_status"
    const val HOOK_STATUS_REFRESH_MS = 2000L
    const val HOOK_STATUS_STALE_AFTER_MS = 10000L
    const val BOOT_ID_PATH = "/proc/sys/kernel/random/boot_id"
}
