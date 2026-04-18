package ua.polodarb.gmsflags.xposed

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.luckypray.dexkit.DexKitBridge
import ua.polodarb.gmsflags.BuildConfig
import java.io.File
import java.io.FileInputStream
import java.lang.ref.WeakReference
import java.lang.reflect.Modifier
import java.security.DigestInputStream
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

@Suppress("unused")
class PhixitHook : IXposedHookLoadPackage {

    companion object {
        private const val TARGET_DATABASE_NAME = "phenotype.db"
        private const val DEXKIT_LIB = "dexkit"
        private const val PHIXIT_HOOK_LIB = "phixit_hook"

        private const val HASH_FILE = "apk_hash"

        private const val LIBS_DIR = "libs"
        private const val XPOSED_DIR = "gmsflags_xposed"
        private const val XPOSED_STATUS_FILE = "hook_status"
        private const val PROC_STAT_START_TIME_INDEX = 19

        private val TARGET_PACKAGES = setOf(
            "com.google.android.gms" to "com.google.android.gms.persistent",
            "com.android.vending" to "com.android.vending",
        )
    }

    init {
        XposedLogger.logD("Module initialized")
    }

    external fun nativeHandlePhenotype(connectionPtr: Long)
    external fun nativeSetDebugLogPath(path: String?)

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) return

        XposedLogger.logD("Received ${lpparam.packageName} (${lpparam.processName})")

        if (lpparam.packageName to lpparam.processName !in TARGET_PACKAGES) return

        XposedLogger.logI("Required package has been found, installing hooks")

        val nativeReady = AtomicBoolean(false)
        val pendingConnections = mutableListOf<WeakReference<Any>>()
        val processedPtrs = HashSet<Long>()

        val connectionClass = XposedHelpers.findClass(
            "android.database.sqlite.SQLiteConnection", null
        )
        val poolClass = XposedHelpers.findClass(
            "android.database.sqlite.SQLiteConnectionPool", null
        )
        val configClass = XposedHelpers.findClass(
            "android.database.sqlite.SQLiteDatabaseConfiguration", null
        )

        val ptrField = connectionClass.getDeclaredField("mConnectionPtr")
            .apply { isAccessible = true }
        val configField = connectionClass.getDeclaredField("mConfiguration")
            .apply { isAccessible = true }
        val pathField = configClass.getDeclaredField("path")
            .apply { isAccessible = true }

        fun handleConnection(connection: Any, source: String) {
            val config = configField.get(connection) ?: return
            val path = pathField.get(config) as? String ?: return
            if (File(path).name != TARGET_DATABASE_NAME) return

            val ptr = ptrField.getLong(connection)
            if (ptr == 0L) return

            synchronized(pendingConnections) {
                if (!processedPtrs.add(ptr)) return

                if (nativeReady.get()) {
                    XposedLogger.logI("Phenotype connection via $source, ptr=$ptr")
                    nativeHandlePhenotype(connectionPtr = ptr)
                } else {
                    XposedLogger.logI("Buffering phenotype connection via $source")
                    pendingConnections.add(WeakReference(connection))
                }
            }
        }

        safeHookMethod(connectionClass.getDeclaredMethod("open"), object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                handleConnection(param.thisObject, "open()")
            }
        })

        val cancelClass = XposedHelpers.findClass("android.os.CancellationSignal", null)
        safeFindAndHookMethod(
            poolClass, "acquireConnection",
            String::class.java, Integer.TYPE, cancelClass,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    handleConnection(param.result ?: return, "acquireConnection()")
                }
            }
        )

        hookApplication(
            lpparam = lpparam,
            onCreate = { context, classLoader ->
                XposedLogger.initFileLogging(xposedDir(context), lpparam)
                XposedLogger.logI("Context received, starting extracting libraries")

                try {
                    extractAndLoadNativeLibrary(context, DEXKIT_LIB)
                    hookConflictingFlags(lpparam, classLoader)
                } catch (t: Throwable) {
                    XposedLogger.logE("Failed to set up conflicting flags hook", t)
                }

                try {
                    extractAndLoadNativeLibrary(context, PHIXIT_HOOK_LIB)
                    if (BuildConfig.DEBUG) {
                        nativeSetDebugLogPath(XposedLogger.logFilePath)
                    }
                } catch (t: Throwable) {
                    XposedLogger.logE("Failed to prepare native library", t)
                    return@hookApplication
                }

                XposedLogger.logI("Native library loaded, processing pending connections")

                synchronized(pendingConnections) {
                    nativeReady.set(true)
                    for (ref in pendingConnections) {
                        val connection = ref.get() ?: continue
                        val ptr = ptrField.getLong(connection)
                        if (ptr == 0L) continue
                        XposedLogger.logI("Processing buffered phenotype connection, ptr=$ptr")
                        nativeHandlePhenotype(connectionPtr = ptr)
                    }
                    pendingConnections.clear()
                }

                writeHookStatus(context, lpparam)
                XposedLogger.logI("All done for ${lpparam.packageName} (${lpparam.processName})")
            }
        )
    }

    private fun hookApplication(
        lpparam: XC_LoadPackage.LoadPackageParam,
        onCreate: (context: Context, classLoader: ClassLoader) -> Unit,
    ) {
        val application = XposedHelpers.findClass("android.app.Application", lpparam.classLoader)
        val isLaunched = AtomicBoolean(false)

        safeFindAndHookMethod(
            application,
            "onCreate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (!isLaunched.compareAndSet(false, true)) return

                    val application = param.thisObject as Application
                    val context = application.applicationContext
                    val classLoader = application.javaClass.classLoader ?: lpparam.classLoader

                    onCreate(context, classLoader)
                }
            }
        )
    }

    private fun writeHookStatus(context: Context, lpparam: XC_LoadPackage.LoadPackageParam) {
        runCatching {
            val xposedDir = xposedDir(context)
            if (!xposedDir.exists() && !xposedDir.mkdirs()) {
                XposedLogger.logE("Failed to create Xposed directory: ${xposedDir.path}")
                return
            }

            File(xposedDir, XPOSED_STATUS_FILE).writeText(
                buildString {
                    append("package=${lpparam.packageName}\n")
                    append("process=${lpparam.processName}\n")
                    append("pid=${Process.myPid()}\n")
                    append("time=${currentProcessStartTime()}\n")
                }
            )
            XposedLogger.logI("Xposed hook status written for ${lpparam.packageName} (${lpparam.processName})")
        }.onFailure {
            XposedLogger.logE("Failed to write Xposed hook status", it)
        }
    }

    private fun currentProcessStartTime(): String {
        return runCatching {
            File("/proc/self/stat").readText().processStartTime()
        }.getOrDefault("")
    }

    private fun xposedDirCandidates(context: Context): List<File> =
        listOf(
            File(context.dataDir, XPOSED_DIR),
            File(context.dataDir.path.replace("/user/", "/user_de/"), XPOSED_DIR),
        ).distinctBy { it.absolutePath }

    private fun xposedDir(context: Context): File {
        return try {
            xposedDirCandidates(context).firstOrNull { dir ->
                runCatching { (dir.exists() || dir.mkdirs()) && dir.canWrite() }.getOrDefault(false)
            }
        } catch (e: Exception) {
            null
        } ?: File(context.dataDir, XPOSED_DIR)
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    private fun extractAndLoadNativeLibrary(
        context: Context,
        libName: String,
        packageName: String = BuildConfig.APPLICATION_ID
    ) {
        val nativeDir = findWritableNativeDir(context)
        val hashFile = File(nativeDir, "${HASH_FILE}_$libName")
        val apkFile = context.getApkFile(packageName)
        val libFile = nativeLibraryFile(nativeDir, libName)

        val apkHash = calculateApkHash(apkFile.path)

        val storedLine = hashFile.takeIf { it.exists() }?.bufferedReader()?.use { it.readLine() }
        val colonIdx = storedLine?.lastIndexOf(':') ?: -1
        val storedAbi = storedLine?.substring(colonIdx + 1)?.takeIf { it.isNotEmpty() }

        if (storedLine?.substring(0, colonIdx) == apkHash && storedAbi != null && libFile.exists()) {
            XposedLogger.logI("Native lib $libName already up-to-date")
        } else {
            if (!nativeDir.exists()) require(nativeDir.mkdirs()) { "Failed to create lib directory" }

            val deviceAbi = ZipFile(apkFile).use { zip ->
                val abi = requireNotNull(getBestMatchingAbi(zip)) {
                    "No compatible ABI found for APK: ${apkFile.path}"
                }
                XposedLogger.logI("Preparing $libName for $packageName ($abi)")
                extractLibraryFromApk(zip, abi, libName, nativeDir)
                abi
            }

            hashFile.writeText("$apkHash:$deviceAbi")
        }

        if (!libFile.exists()) throw UnsatisfiedLinkError("Native library not found: $libFile")
        System.load(libFile.absolutePath)
        XposedLogger.logI("Loaded library: $libName")
    }

    private fun nativeLibraryFile(nativeDir: File, libName: String): File {
        val filename = if (libName.startsWith("lib")) libName else "lib$libName"
        return File(nativeDir, if (filename.endsWith(".so")) filename else "$filename.so")
    }

    private fun extractLibraryFromApk(
        apkZip: ZipFile,
        deviceAbi: String,
        libName: String,
        nativeDir: File
    ) {
        val entryName = "lib/$deviceAbi/${nativeLibraryFile(nativeDir, libName).name}"
        val entry = requireNotNull(apkZip.getEntry(entryName)) {
            "Native library not found in APK: $entryName"
        }
        extractLibrary(apkZip, entry, nativeDir)
    }

    @SuppressLint("SetWorldReadable")
    private fun extractLibrary(
        apkZip: ZipFile,
        entry: ZipEntry,
        nativeDir: File
    ) {
        val output = File(nativeDir, entry.name.substringAfterLast('/'))
        output.delete()
        apkZip.getInputStream(entry).use { input ->
            output.outputStream().use { outputStream ->
                input.copyTo(outputStream, bufferSize = 64 * 1024)
            }
        }

        output.apply {
            setExecutable(true, false)
            setReadable(true, false)
            setWritable(false, false)
        }

        XposedLogger.logI("Extracted: ${output.name} (${output.path})")
    }

    private fun Context.getApkFile(packageName: String): File {
        return File(packageManager.getApplicationInfo(packageName, 0).sourceDir)
    }

    private fun getBestMatchingAbi(apkZip: ZipFile): String? {
        val deviceAbis = Build.SUPPORTED_64_BIT_ABIS + Build.SUPPORTED_32_BIT_ABIS
        val apkAbis = apkZip.entries().asSequence()
            .filter { it.name.startsWith("lib/") && it.name.endsWith(".so") }
            .mapNotNull { it.name.split('/').getOrNull(1) }
            .toSet()
        return deviceAbis.firstOrNull { it in apkAbis }
    }

    private val apkHashCache = HashMap<String, String>()

    private fun calculateApkHash(apkPath: String): String {
        return apkHashCache.getOrPut(apkPath) {
            val md = MessageDigest.getInstance("SHA-256")
            FileInputStream(apkPath).use { fis ->
                DigestInputStream(fis, md).use { dis ->
                    val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                    while (dis.read(buffer) != -1) { /* drain */ }
                }
            }
            md.digest().joinToString("") { "%02x".format(it) }
        }
    }

    private fun String.processStartTime(): String {
        val fieldsAfterName = substringAfterLast(") ").split(' ')
        return fieldsAfterName.getOrNull(PROC_STAT_START_TIME_INDEX).orEmpty()
    }

    private fun findWritableNativeDir(context: Context): File {
        for (candidate in xposedDirCandidates(context)) {
            try {
                val dir = File(candidate, LIBS_DIR)
                if (!dir.exists()) {
                    if (dir.mkdirs()) return dir
                } else if (dir.canWrite()) {
                    return dir
                }
            } catch (_: Exception) {}
        }
        return File(xposedDir(context), LIBS_DIR).apply { mkdirs() }
    }

    private fun hookConflictingFlags(
        lpparam: XC_LoadPackage.LoadPackageParam,
        runtimeClassLoader: ClassLoader,
    ) {
        val apkPath = lpparam.appInfo.sourceDir ?: run {
            XposedLogger.logE("No sourceDir for ${lpparam.packageName}")
            return
        }

        DexKitBridge.create(apkPath).use { bridge ->
            val mergeFlagSetsMethodData = bridge.findMethod {
                matcher {
                    usingStrings("Encountered conflicting flags. Expected flag count ")
                }
            }.singleOrNull() ?: run {
                XposedLogger.logW("Conflicting flags merge method not found")
                return@use
            }

            XposedLogger.logI("Found merge method: ${mergeFlagSetsMethodData.className}#${mergeFlagSetsMethodData.name}")

            val className = mergeFlagSetsMethodData.className.replace('/', '.')
            val effectiveLoader = sequenceOf(runtimeClassLoader, lpparam.classLoader)
                .firstOrNull { cl -> runCatching { Class.forName(className, false, cl) }.isSuccess }
                ?: run {
                    XposedLogger.logW("Conflicting flags class not loadable in any classloader, skipping")
                    return@use
                }

            val mergeFlagSetsMethod = mergeFlagSetsMethodData.getMethodInstance(effectiveLoader)
            val flagSetClass = mergeFlagSetsMethod.declaringClass

            val flagListField = flagSetClass.declaredFields
                .first { !Modifier.isStatic(it.modifiers) }
                .apply { isAccessible = true }

            val flagSetConstructor = flagSetClass.declaredConstructors
                .first { it.parameterCount == 1 }
                .apply { isAccessible = true }

            val flagListBuilderCandidates = bridge.findClass {
                matcher {
                    methods {
                        add {
                            name("<init>")
                            paramCount(1)
                            paramTypes("java.util.Comparator")
                        }
                    }
                }
            }
            var flagListBuilderClass: Class<*>? = null
            for (candidate in flagListBuilderCandidates) {
                val cls = runCatching { Class.forName(candidate.name, false, effectiveLoader) }.getOrNull() ?: continue
                val isFlagListBuilder = cls.declaredMethods.any { m ->
                    m.parameterCount == 0 && m.returnType != Void.TYPE
                } && cls.declaredMethods.any { m ->
                    m.parameterCount == 1 &&
                    m.returnType == Void.TYPE &&
                    m.parameterTypes[0] == Iterable::class.java
                }
                if (isFlagListBuilder) { flagListBuilderClass = cls; break }
            }
            flagListBuilderClass ?: run {
                XposedLogger.logW("Flag list builder class not found")
                return@use
            }

            XposedLogger.logI("Found flag list builder class: ${flagListBuilderClass.name}")

            val flagListBuilderConstructor = flagListBuilderClass.declaredConstructors
                .first { it.parameterCount == 1 }
                .apply { isAccessible = true }

            val buildFlagListMethod = flagListBuilderClass.declaredMethods
                .first { it.parameterCount == 0 && it.returnType != Void.TYPE }
                .apply { isAccessible = true }

            val appendFlagListMethod = flagListBuilderClass.declaredMethods
                .first {
                    it.parameterCount == 1 &&
                    it.returnType == Void.TYPE &&
                    it.parameterTypes[0] == Iterable::class.java
                }
                .apply { isAccessible = true }

            val flagComparator = AtomicReference<Any?>(null)
            var comparatorCaptureHook: XC_MethodHook.Unhook?
            comparatorCaptureHook = XposedBridge.hookMethod(flagListBuilderConstructor, object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (flagComparator.compareAndSet(null, param.args[0])) {
                        XposedLogger.logD("Flag comparator captured: ${param.args[0]?.javaClass?.name}")
                    }
                }
            })

            XposedBridge.hookMethod(mergeFlagSetsMethod, object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    val comparator = flagComparator.get() ?: return

                    try {
                        val flagSets = param.args[0] as? Iterable<*> ?: return
                        val builder = flagListBuilderConstructor.newInstance(comparator)

                        for (flagSet in flagSets) {
                            val flagList = flagListField.get(flagSet) ?: continue
                            appendFlagListMethod.invoke(builder, flagList)
                        }

                        val mergedFlagList = buildFlagListMethod.invoke(builder)
                        param.result = flagSetConstructor.newInstance(mergedFlagList)

                        comparatorCaptureHook?.unhook()
                        @Suppress("AssignedValueIsNeverRead")
                        comparatorCaptureHook = null

                        XposedLogger.logD("Conflicting flags merged successfully")
                    } catch (t: Throwable) {
                        XposedLogger.logE("Error in conflicting flags hook, letting original run", t)
                    }
                }
            })

            XposedLogger.logI("Conflicting flags hook installed")
        }
    }

    private fun safeFindAndHookMethod(
        clazz: Class<*>,
        methodName: String,
        vararg parameterTypesAndCallback: Any
    ): XC_MethodHook.Unhook? {
        return try {
            XposedHelpers.findAndHookMethod(clazz, methodName, *parameterTypesAndCallback)
        } catch (t: Throwable) {
            XposedLogger.logE("Failed to hook $methodName in ${clazz.name}: ${t.message}", t)
            null
        }
    }

    private fun safeHookMethod(
        method: java.lang.reflect.Member,
        callback: XC_MethodHook
    ): XC_MethodHook.Unhook? {
        return try {
            XposedBridge.hookMethod(method, callback)
        } catch (t: Throwable) {
            XposedLogger.logE("Failed to hook ${method}: ${t.message}", t)
            null
        }
    }
}
