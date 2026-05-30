package ua.polodarb.xposed

import android.os.SystemClock
import java.io.File
import ua.polodarb.xposed.info.XposedConstants
import ua.polodarb.xposed.info.HookInfo

class HookStatusWriter(
    private val statusFile: File,
    private val infoProvider: () -> HookInfo,
) {

    private val bootId = readBootId()

    fun start() {
        writeStatus()

        val thread = Thread({
            try {
                while (true) {
                    Thread.sleep(XposedConstants.HOOK_STATUS_REFRESH_MS)
                    writeStatus()
                }
            } catch (_: InterruptedException) {
            } catch (t: Throwable) {
                XposedLogger.logE("Hook status writer stopped", t)
            }
        }, "gmsflags-status")
        thread.isDaemon = true
        thread.start()
    }

    private fun writeStatus() {
        runCatching {
            statusFile.parentFile?.let { parent ->
                if (!parent.exists()) parent.mkdirs()
            }

            val status = infoProvider()
                .copy(
                    bootId = bootId,
                    updatedAt = SystemClock.elapsedRealtime(),
                )
                .serialize()

            val tmpFile = File("${statusFile.absolutePath}.tmp")
            tmpFile.writeText(status)
            if (!tmpFile.renameTo(statusFile)) {
                statusFile.writeText(status)
                tmpFile.delete()
            }
        }.onFailure {
            XposedLogger.logW("Failed to write hook status: ${it.message}")
        }
    }

    private fun readBootId(): String {
        return runCatching {
            File(XposedConstants.BOOT_ID_PATH).readText().trim()
        }.getOrDefault("")
    }
}
