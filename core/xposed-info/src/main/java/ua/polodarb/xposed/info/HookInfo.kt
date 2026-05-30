package ua.polodarb.xposed.info

data class HookInfo(
    val packageName: String = "",
    val processName: String = "",
    val pid: Int = 0,
    val bootId: String = "",
    val startedAt: Long = 0,
    val updatedAt: Long = 0,
    val connections: Long = 0,
    val triggerCalls: Long = 0,
    val triggerMerges: Long = 0,
) {

    fun serialize(): String = buildString {
        appendLine("$KEY_PACKAGE=$packageName")
        appendLine("$KEY_PROCESS=$processName")
        appendLine("$KEY_PID=$pid")
        appendLine("$KEY_BOOT_ID=$bootId")
        appendLine("$KEY_STARTED_AT=$startedAt")
        appendLine("$KEY_UPDATED_AT=$updatedAt")
        appendLine("$KEY_CONNECTIONS=$connections")
        appendLine("$KEY_TRIGGER_CALLS=$triggerCalls")
        appendLine("$KEY_TRIGGER_MERGES=$triggerMerges")
    }

    companion object {
        private const val KEY_PACKAGE = "package"
        private const val KEY_PROCESS = "process"
        private const val KEY_PID = "pid"
        private const val KEY_BOOT_ID = "boot_id"
        private const val KEY_STARTED_AT = "started_at"
        private const val KEY_UPDATED_AT = "updated_at"
        private const val KEY_CONNECTIONS = "connections"
        private const val KEY_TRIGGER_CALLS = "trigger_calls"
        private const val KEY_TRIGGER_MERGES = "trigger_merges"

        fun deserialize(raw: String): HookInfo? {
            val map = raw.lineSequence()
                .mapNotNull { line ->
                    val idx = line.indexOf('=')
                    if (idx > 0) line.substring(0, idx) to line.substring(idx + 1)
                    else null
                }
                .toMap()

            if (map.isEmpty()) return null

            return HookInfo(
                packageName = map[KEY_PACKAGE].orEmpty(),
                processName = map[KEY_PROCESS].orEmpty(),
                pid = map[KEY_PID]?.toIntOrNull() ?: 0,
                bootId = map[KEY_BOOT_ID].orEmpty(),
                startedAt = map[KEY_STARTED_AT]?.toLongOrNull() ?: 0,
                updatedAt = map[KEY_UPDATED_AT]?.toLongOrNull() ?: 0,
                connections = map[KEY_CONNECTIONS]?.toLongOrNull() ?: 0,
                triggerCalls = map[KEY_TRIGGER_CALLS]?.toLongOrNull() ?: 0,
                triggerMerges = map[KEY_TRIGGER_MERGES]?.toLongOrNull() ?: 0,
            )
        }
    }
}
