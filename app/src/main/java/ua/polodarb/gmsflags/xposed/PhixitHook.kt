package ua.polodarb.gmsflags.xposed

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.OpenParams
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.os.Process
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.luckypray.dexkit.DexKitBridge
import ua.polodarb.gmsflags.BuildConfig
import java.io.File
import java.io.FileInputStream
import java.lang.reflect.Modifier
import java.security.DigestInputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.atomic.AtomicReference
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

@Suppress("unused")
class PhixitHook : IXposedHookLoadPackage {

    companion object {
        private const val TAG = "PhixitHook"

        private const val TARGET_DATABASE_NAME = "phenotype.db"
        private const val MINIMAL_PHENOTYPE_VERSION = 1034

        private const val DEXKIT_LIB = "dexkit"
        private const val PHIXIT_HOOK_LIB = "phixit_hook"

        private const val HASH_FILE = "apk_hash"

        private const val LIBS_DIR = "libs"
        private const val LOGS_DIR = "logs"
        private const val LOG_FILE_SUFFIX = "_xposed.log"
        private const val XPOSED_DIR = "gmsflags_xposed"
        private const val XPOSED_STATUS_FILE = "hook_status"
        private const val PROC_STAT_START_TIME_INDEX = 19

        private val TARGET_PACKAGES = setOf(
            "com.google.android.gms" to "com.google.android.gms.persistent",
            "com.android.vending" to "com.android.vending",
        )

        private val LOG_START_TIME = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

        private val LOG_LOCK = Any()
        private val PENDING_LOGS = mutableListOf<String>()
        private val STARTED_TARGETS = mutableSetOf<String>() // TODO: ужас какой-то
        private var debugLogFile: File? = null
    }

    init {
        logD("Module initialized")
    }

    external fun nativeHandlePhenotype(connectionPtr: Long)
    external fun nativeSetDebugLogPath(path: String?)

    private fun logD(message: String) {
        Log.d(TAG, message)
        writeDebugLog("D", message)
    }

    private fun logI(message: String) {
        Log.i(TAG, message)
        writeDebugLog("I", message)
    }

    private fun logW(message: String) {
        Log.w(TAG, message)
        writeDebugLog("W", message)
    }

    private fun logE(message: String, throwable: Throwable? = null) {
        if (throwable == null) {
            Log.e(TAG, message)
        } else {
            Log.e(TAG, message, throwable)
        }
        writeDebugLog("E", message, throwable)
    }

    // TODO: Move to other class all logs or I am killing myself
    private fun writeDebugLog(level: String, message: String, throwable: Throwable? = null) {
        if (!BuildConfig.DEBUG) return

        val line = buildString {
            append(System.currentTimeMillis())
            append(' ')
            append(level)
            append(' ')
            append('[')
            append(Thread.currentThread().name)
            append("] ")
            append(message)
            if (throwable != null) {
                append('\n')
                append(Log.getStackTraceString(throwable))
            }
        }

        synchronized(LOG_LOCK) {
            val file = debugLogFile
            if (file == null) {
                PENDING_LOGS += line
                return
            }

            if (PENDING_LOGS.isNotEmpty()) {
                file.appendText(PENDING_LOGS.joinToString(separator = "\n", postfix = "\n"))
                PENDING_LOGS.clear()
            }
            file.appendText("$line\n")
        }
    }

    private fun initDebugFileLogging(context: Context, lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!BuildConfig.DEBUG) return

        val logDir = File(xposedDir(context), LOGS_DIR)
        if (!logDir.exists() && !logDir.mkdirs()) {
            logE("Failed to create debug log directory: ${logDir.path}")
            return
        }

        val logFile = File(logDir, "$LOG_START_TIME$LOG_FILE_SUFFIX")
        synchronized(LOG_LOCK) {
            debugLogFile = logFile
        }
        logI("Debug file logging enabled: ${logFile.path} for ${lpparam.packageName} (${lpparam.processName})")
    }

    private fun writeHookStatus(context: Context, lpparam: XC_LoadPackage.LoadPackageParam) {
        runCatching {
            val xposedDir = xposedDir(context)
            if (!xposedDir.exists() && !xposedDir.mkdirs()) {
                logE("Failed to create Xposed directory: ${xposedDir.path}")
                return
            }

            File(xposedDir, XPOSED_STATUS_FILE).writeText(
                buildString {
                    append("package=${lpparam.packageName}\n")
                    append("process=${lpparam.processName}\n")
                    append("pid=${Process.myPid()}\n")
                    append("processStartTime=${currentProcessStartTime()}\n")
                    append("time=${System.currentTimeMillis()}\n")
                }
            )
            logI("Xposed hook status written for ${lpparam.packageName} (${lpparam.processName})")
        }.onFailure {
            logE("Failed to write Xposed hook status", it)
        }
    }

    private fun currentProcessStartTime(): String {
        return runCatching {
            File("/proc/self/stat").readText().processStartTime()
        }.getOrDefault("")
    }

    private fun xposedDir(context: Context): File {
        return try {
            listOf(
                File(context.dataDir, XPOSED_DIR),
                File(context.dataDir.path.replace("/user/", "/user_de/"), XPOSED_DIR),
            )
                .distinctBy { it.absolutePath }
                .firstOrNull { dir ->
                    runCatching {
                        (dir.exists() || dir.mkdirs()) && dir.canWrite()
                    }.getOrDefault(false)
                }
        } catch (e: Exception) {
            null
        } ?: File(context.dataDir, XPOSED_DIR)
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) return

        logD("Received ${lpparam.packageName} (${lpparam.processName})")

        if (lpparam.packageName to lpparam.processName !in TARGET_PACKAGES) return

        val targetKey = "${lpparam.packageName}:${lpparam.processName}"
        synchronized(STARTED_TARGETS) {
            if (!STARTED_TARGETS.add(targetKey)) {
                logD("Skipping duplicate setup for ${lpparam.packageName} (${lpparam.processName})")
                return
            }
        }

        logI("Required package has been found, waiting for context")

        hookApplication(
            lpparam = lpparam,
            onCreate = { context ->
                initDebugFileLogging(context, lpparam)
                logI("Context received, starting extracting libraries")

                try {
                    extractAndLoadNativeLibrary(context, DEXKIT_LIB)
                    hookConflictingFlags(lpparam)
                } catch (t: Throwable) {
                    logE("Failed to set up conflicting flags hook", t)
                }

                try {
                    extractAndLoadNativeLibrary(context, PHIXIT_HOOK_LIB)
                    if (BuildConfig.DEBUG) {
                        nativeSetDebugLogPath(debugLogFile?.absolutePath)
                    }
                } catch (t: Throwable) {
                    logE("Failed to prepare native library", t)
                    return@hookApplication
                }

                logI("Native libraries extracted, installing hooks")

                hookOpenDatabase(
                    lpparam = lpparam,
                    onOpenDatabase = { db ->
                        if (File(db.path).name != TARGET_DATABASE_NAME)
                            return@hookOpenDatabase

                        handlePhenotype(db)
                    }
                )

                writeHookStatus(context, lpparam)
                logI("All done for ${lpparam.packageName} (${lpparam.processName})")
            }
        )
    }

    private fun hookApplication(
        lpparam: XC_LoadPackage.LoadPackageParam,
        onCreate: (context: Context) -> Unit,
    ) {
        val application = XposedHelpers.findClass("android.app.Application", lpparam.classLoader)
        var isLaunched = false

        safeFindAndHookMethod(
            application,
            "onCreate",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (isLaunched) return

                    val application = param.thisObject as Application
                    val context = application.applicationContext

                    @Suppress("AssignedValueIsNeverRead")
                    isLaunched = true

                    onCreate(context)
                }
            }
        )
    }

    private fun handlePhenotype(db: SQLiteDatabase) {
        if (!db.isPhixit) {
            logE("Phenotype version is too low for Phixit (${db.version})")
            return
        }

        logI("Phenotype contains Phixit schema, trying to create survival trigger")

        val ptr = getConnectionPtr(db)

        if (ptr == null || ptr == 0L) {
            logE("Failed to obtain native connection pointer")
            return
        }

        logI("SQLite native connectionPtr = $ptr")

        nativeHandlePhenotype(connectionPtr = ptr)
    }

    private fun getConnectionPtr(db: SQLiteDatabase): Long? {
        return try {
            // SQLiteDatabase -> mConnectionPoolLocked
            val dbClass = SQLiteDatabase::class.java
            val poolField = dbClass.getDeclaredField("mConnectionPoolLocked")
            poolField.isAccessible = true
            val pool = poolField.get(db) ?: return null

            // SQLiteConnectionPool -> mAvailablePrimaryConnection
            val poolClass = pool.javaClass
            val primaryConnField = poolClass.getDeclaredField("mAvailablePrimaryConnection")
            primaryConnField.isAccessible = true
            val connection = primaryConnField.get(pool) ?: return null

            // SQLiteConnection -> mConnectionPtr
            val connClass = connection.javaClass
            val ptrField = connClass.getDeclaredField("mConnectionPtr")
            ptrField.isAccessible = true

            ptrField.getLong(connection)
        } catch (t: Throwable) {
            logE("Reflection failed", t)
            null
        }
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    private fun extractAndLoadNativeLibrary(
        context: Context,
        libName: String,
        packageName: String = BuildConfig.APPLICATION_ID
    ) {
        val nativeDir = findWritableNativeDir(context)
        val hashFile = File(nativeDir, HASH_FILE)
        val apkFile = context.getApkFile(packageName)
        val deviceAbi = requireNotNull(getBestMatchingAbi(apkFile.path)) {
            "No compatible ABI found for APK: ${apkFile.path}"
        }

        logI("Preparing native libraries for $packageName ($deviceAbi)")

        val currentHash = "${calculateApkHash(apkFile.path)}:$deviceAbi:$libName"
        if (!nativeDir.exists() || !hashFile.exists() ||
            hashFile.bufferedReader().use { it.readLine() } != currentHash
        ) {
            nativeDir.deleteRecursively()
            require(nativeDir.mkdirs()) { "Failed to create lib directory" }
            extractLibraryFromApk(apkFile, deviceAbi, libName, nativeDir)
            hashFile.writeText(currentHash)
        } else {
            logI("Native libs already up-to-date")
        }

        val libFile = nativeLibraryFile(nativeDir, libName)
        if (!libFile.exists()) {
            throw UnsatisfiedLinkError("Native library not found: $libFile")
        }

        System.load(libFile.absolutePath)
        logI("Loaded library: $libName")
    }

    private fun nativeLibraryFile(nativeDir: File, libName: String): File {
        val filename = if (libName.startsWith("lib")) libName else "lib$libName"
        return File(nativeDir, if (filename.endsWith(".so")) filename else "$filename.so")
    }

    private fun extractLibraryFromApk(
        apkFile: File,
        deviceAbi: String,
        libName: String,
        nativeDir: File
    ) {
        ZipFile(apkFile).use { apkZip ->
            val entryName = "lib/$deviceAbi/${nativeLibraryFile(nativeDir, libName).name}"
            val entry = requireNotNull(apkZip.getEntry(entryName)) {
                "Native library not found in APK: $entryName"
            }
            extractLibrary(apkZip, entry, nativeDir)
        }
    }

    @SuppressLint("SetWorldReadable")
    private fun extractLibrary(
        apkZip: ZipFile,
        entry: ZipEntry,
        nativeDir: File
    ) {
        val output = File(nativeDir, entry.name.substringAfterLast('/'))
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

        logI("Extracted: ${output.name} (${output.path})")
    }

    private fun Context.getApkFile(packageName: String): File {
        return File(packageManager.getApplicationInfo(packageName, 0).sourceDir)
    }

    private fun getBestMatchingAbi(apkPath: String): String? {
        val deviceAbis = Build.SUPPORTED_64_BIT_ABIS + Build.SUPPORTED_32_BIT_ABIS
        val apkAbis = getAbisFromApk(apkPath)
        return deviceAbis.firstOrNull { it in apkAbis }
    }

    private fun getAbisFromApk(apkPath: String): Set<String> {
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
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (dis.read(buffer) != -1) { /* Unit */ }
            }
        }
        return md.digest().joinToString("") { "%02x".format(it) }
    }

    private fun String.processStartTime(): String {
        val fieldsAfterName = substringAfterLast(") ")
            .split(' ')
        return fieldsAfterName.getOrNull(PROC_STAT_START_TIME_INDEX).orEmpty()
    }

    private fun findWritableNativeDir(context: Context): File {
        // TODO: Refactor me
        val pathsToTry = listOf(
            { File(File(context.dataDir, XPOSED_DIR), LIBS_DIR) },
            { File(File(context.dataDir.path.replace("/user/", "/user_de/"), XPOSED_DIR), LIBS_DIR) },
        )

        for (pathProvider in pathsToTry) {
            try {
                val dir = pathProvider()
                if (!dir.exists()) {
                    if (dir.mkdirs()) return dir
                } else if (dir.canWrite()) {
                    return dir
                }
            } catch (_: Exception) {}
        }

        return File(xposedDir(context), LIBS_DIR).apply { mkdirs() }
    }

    // TODO: Do I even need this many? It looks like complete crap, but sometimes some hooks don't work?
    private fun hookOpenDatabase(
        lpparam: XC_LoadPackage.LoadPackageParam,
        onOpenDatabase: (SQLiteDatabase) -> Unit,
    ) {
        fun handle(param: XC_MethodHook.MethodHookParam) {
            var result = param.result
            if (result !is SQLiteDatabase) {
                try {
                    result = param.args[0]
                    if (result !is SQLiteDatabase)
                        return
                } catch (_: Throwable) { return }
            }
            onOpenDatabase(result)
        }

        val hook = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) = handle(param)
        }

        safeFindAndHookMethod(
            SQLiteDatabase::class.java,
            "openDatabase",
            String::class.java, SQLiteDatabase.CursorFactory::class.java, Int::class.java,
            hook
        )

        safeFindAndHookMethod(
            SQLiteDatabase::class.java,
            "openDatabase",
            String::class.java, SQLiteDatabase.CursorFactory::class.java, Int::class.java, DatabaseErrorHandler::class.java,
            hook
        )

        // True hook for phenotype, other hooks are for safety
        safeFindAndHookMethod(
            SQLiteDatabase::class.java,
            "openDatabase",
            File::class.java, OpenParams::class.java,
            hook
        )

        safeFindAndHookMethod(
            SQLiteDatabase::class.java,
            "openOrCreateDatabase",
            String::class.java, SQLiteDatabase.CursorFactory::class.java,
            hook
        )

        safeFindAndHookMethod(
            SQLiteDatabase::class.java,
            "openOrCreateDatabase",
            File::class.java, SQLiteDatabase.CursorFactory::class.java,
            hook
        )

        safeFindAndHookMethod(
            SQLiteDatabase::class.java,
            "openOrCreateDatabase",
            String::class.java, SQLiteDatabase.CursorFactory::class.java, DatabaseErrorHandler::class.java,
            hook
        )

        val contextImpl = XposedHelpers.findClass("android.app.ContextImpl", null)

        safeFindAndHookMethod(
            contextImpl,
            "openOrCreateDatabase",
            String::class.java, Int::class.java, SQLiteDatabase.CursorFactory::class.java,
            hook
        )

        safeFindAndHookMethod(
            contextImpl,
            "openOrCreateDatabase",
            String::class.java, Int::class.java, SQLiteDatabase.CursorFactory::class.java, DatabaseErrorHandler::class.java,
            hook
        )

        safeFindAndHookMethod(
            ContextWrapper::class.java,
            "openOrCreateDatabase",
            String::class.java, Int::class.java, SQLiteDatabase.CursorFactory::class.java,
            hook
        )

        safeFindAndHookMethod(
            ContextWrapper::class.java,
            "openOrCreateDatabase",
            String::class.java, Int::class.java, SQLiteDatabase.CursorFactory::class.java, DatabaseErrorHandler::class.java,
            hook
        )

        // onOpen is called rarer than getDatabaseLocked, so for SQLiteOpenHelper hook only this
        // TODO: Maybe also remove this??
        safeFindAndHookMethod(
            SQLiteOpenHelper::class.java,
            "onOpen",
            SQLiteDatabase::class.java,
            hook
        )
    }

    private fun hookConflictingFlags(lpparam: XC_LoadPackage.LoadPackageParam) {
        val apkPath = lpparam.appInfo.sourceDir ?: run {
            logE("No sourceDir for ${lpparam.packageName}")
            return
        }

        DexKitBridge.create(apkPath).use { bridge ->
            val mergeFlagSetsMethodData = bridge.findMethod {
                matcher {
                    usingStrings("Encountered conflicting flags. Expected flag count ")
                }
            }.singleOrNull() ?: run {
                logW("Conflicting flags merge method not found")
                return@use
            }

            logI("Found merge method: ${mergeFlagSetsMethodData.className}#${mergeFlagSetsMethodData.name}")

            val mergeFlagSetsMethod = mergeFlagSetsMethodData.getMethodInstance(lpparam.classLoader)
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
                val cls = runCatching { Class.forName(candidate.name, false, lpparam.classLoader) }.getOrNull() ?: continue
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
                logW("Flag list builder class not found")
                return@use
            }

            logI("Found flag list builder class: ${flagListBuilderClass.name}")

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
                        logD("Flag comparator captured: ${param.args[0]?.javaClass?.name}")
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

                        logD("Conflicting flags merged successfully")
                    } catch (t: Throwable) {
                        logE("Error in conflicting flags hook, letting original run", t)
                    }
                }
            })

            logI("Conflicting flags hook installed")
        }
    }

    fun safeFindAndHookMethod(
        clazz: Class<*>,
        methodName: String,
        vararg parameterTypesAndCallback: Any
    ): XC_MethodHook.Unhook? {
        return try {
            XposedHelpers.findAndHookMethod(clazz, methodName, *parameterTypesAndCallback)
        } catch (t: Throwable) {
            logE("Failed to hook $methodName in ${clazz.name}: ${t.message}", t)
            null
        }
    }

    // Minimum known version is 1034, the current one is 1035, I think this check is enough
    private val SQLiteDatabase.isPhixit
            get() = this.version >= MINIMAL_PHENOTYPE_VERSION
}
