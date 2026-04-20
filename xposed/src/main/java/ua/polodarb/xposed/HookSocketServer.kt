package ua.polodarb.xposed

import android.net.LocalServerSocket
import ua.polodarb.xposed.info.HookInfo

class HookSocketServer(
    private val infoProvider: () -> HookInfo,
) {

    @Volatile
    private var cachedData: ByteArray = ByteArray(0)

    fun start() {
        val info = infoProvider()
        val socketName = "${HookInfo.SOCKET_PREFIX}${info.processName}"
        cachedData = info.serialize().toByteArray()

        val refreshThread = Thread({
            try {
                while (true) {
                    Thread.sleep(CACHE_REFRESH_MS)
                    cachedData = infoProvider().serialize().toByteArray()
                }
            } catch (_: InterruptedException) {}
        }, "gmsflags-cache")
        refreshThread.isDaemon = true
        refreshThread.start()

        val thread = Thread({
            try {
                val server = LocalServerSocket(socketName)
                XposedLogger.logI("Socket server started: $socketName")
                while (true) {
                    val client = try {
                        server.accept()
                    } catch (t: Throwable) {
                        XposedLogger.logW("Accept failed: ${t.message}")
                        continue
                    } ?: continue
                    try {
                        val data = cachedData
                        client.outputStream.write(data)
                        client.outputStream.flush()
                        runCatching { client.shutdownOutput() }
                    } catch (t: Throwable) {
                        XposedLogger.logD("Client disconnected early: ${t.message}")
                    } finally {
                        runCatching { client.close() }
                    }
                }
            } catch (t: Throwable) {
                XposedLogger.logE("Socket server failed for $socketName", t)
            }
        }, "gmsflags-socket")
        thread.isDaemon = true
        thread.start()
    }

    companion object {
        private const val CACHE_REFRESH_MS = 2000L
    }
}
