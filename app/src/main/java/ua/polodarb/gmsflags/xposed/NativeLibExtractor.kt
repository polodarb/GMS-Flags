package ua.polodarb.gmsflags.xposed

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import ua.polodarb.gmsflags.BuildConfig
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

class NativeLibExtractor(
    context: Context,
    private val packageName: String = BuildConfig.APPLICATION_ID,
) {

    companion object {
        private const val TAG = "PhixitHook-NativeLibExtractor"

        private const val NATIVE_DIR = "phixit_libs"
    }

    private val nativeDir = findWritableDir(context)
    private val hashFile = File(nativeDir, "apk_hash")
    private val apkFile = requireNotNull(context.getApkPath(packageName)) {
        "Failed to get APK path for package: $packageName"
    }
    private val deviceAbi = requireNotNull(getBestMatchingAbi(apkFile.path)) {
        "No compatible ABI found for APK: ${apkFile.path}"
    }

    init {
        Log.i(TAG, "Initializing, working for $packageName ($deviceAbi)")
        try {
            extractIfNeeded()
            Log.i(TAG, "Native libraries successfully extracted")
        } catch (th: Throwable) {
            Log.e(TAG, "Error managing native libraries", th)
        }
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    fun loadLibrary(libName: String) {
        val filename = if (libName.startsWith("lib")) libName else "lib$libName"
        val libFile = File(nativeDir, if (filename.endsWith(".so")) filename else "$filename.so")

        if (!libFile.exists()) {
            throw UnsatisfiedLinkError("Native library not found: $libFile")
        }

        System.load(libFile.absolutePath)
        Log.i(TAG, "Loaded library: $libName")
    }

    private fun extractIfNeeded() {
        val currentHash = calculateApkHash(apkFile.path)

        if (isExtractionUpToDate(currentHash)) {
            Log.i(TAG, "Native libs already up-to-date")
            return
        }

        nativeDir.deleteRecursively()
        require(nativeDir.mkdirs()) { "Failed to create lib directory" }

        extractLibrariesFromApk()
        hashFile.writeText(currentHash)
    }

    private fun isExtractionUpToDate(currentHash: String): Boolean {
        return nativeDir.exists() && hashFile.exists() &&
                hashFile.bufferedReader().use { it.readLine() } == currentHash
    }

    private fun extractLibrariesFromApk() {
        ZipFile(apkFile).use { apkZip ->
            val abiPattern = Regex("^lib/$deviceAbi/.*\\.so$")

            apkZip.entries().asSequence()
                .filter { abiPattern.matches(it.name) }
                .forEach { entry ->
                    extractLibrary(apkZip, entry)
                }
        }
    }

    private fun extractLibrary(apkZip: ZipFile, entry: ZipEntry) {
        val libName = entry.name.substringAfterLast('/')
        val output = File(nativeDir, libName)

        apkZip.getInputStream(entry).use { input ->
            output.outputStream().use { outputStream ->
                input.copyTo(outputStream, bufferSize = 1024)
            }
        }

        output.apply {
            setExecutable(true, false)
            setReadable(true, false)
            setWritable(false, false)
        }

        Log.i(TAG, "Extracted: ${output.name} (${output.path})")
    }

    private fun Context.getApkPath(packageName: String): File {
        val appInfo = packageManager.getApplicationInfo(packageName, 0)
        return File(appInfo.sourceDir)
    }

    private fun getBestMatchingAbi(apkPath: String?): String? {
        val deviceAbis = Build.SUPPORTED_64_BIT_ABIS + Build.SUPPORTED_32_BIT_ABIS
        val apkAbis = getAbisFromApk(apkPath)
        return deviceAbis.firstOrNull { it in apkAbis }
    }

    private fun getAbisFromApk(apkPath: String?): Set<String> {
        return ZipFile(apkPath).use { apkZip ->
            apkZip.entries().asSequence()
                .filter { it.name.startsWith("lib/") && it.name.endsWith(".so") }
                .mapNotNull { entry ->
                    entry.name.split('/').getOrNull(1)
                }
                .toSet()
        }
    }

    private fun calculateApkHash(apkPath: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        FileInputStream(apkPath).use { fis ->
            DigestInputStream(fis, md).use { dis ->
                dis.transferTo(OutputStream.nullOutputStream())
            }
        }
        return md.digest().joinToString("") { "%02x".format(it) }
    }

    private fun findWritableDir(context: Context): File {
        val pathsToTry = listOf(
            { File(context.dataDir, NATIVE_DIR) },

            // Device encrypted storage (/user_de/) - available before user unlocks device
            // GMS starts early, so /user_de/ is required for pre-unlock operations
            { File(context.dataDir.path.replace("/user/", "/user_de/"), NATIVE_DIR) },
        )

        for (pathProvider in pathsToTry) {
            try {
                val dir = pathProvider()

                if (!dir.exists()) {
                    if (dir.mkdirs()) return dir
                } else {
                    if (dir.canWrite()) return dir
                }
            } catch (_: Exception) {}
        }

        val fallback = File(context.dataDir, NATIVE_DIR)
        fallback.mkdirs()
        return fallback
    }
}