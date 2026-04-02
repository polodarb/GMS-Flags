package ua.polodarb.gmsflags.xposed

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.OpenParams
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

@Suppress("unused")
class PhixitHook : IXposedHookLoadPackage { // TODO: Need more tests!

    companion object {
        private const val TAG = "PhixitHook"

        private const val TARGET_PACKAGE_NAME = "com.google.android.gms"
        // This didn't work on Pixel 9 running Android 17 with ReLSPosed
        private const val TARGET_PROCESS_NAME = "com.google.android.gms.persistent" // This worked on Meizu 18s running Android 13 with original LSPosed

        private const val TARGET_DATABASE_NAME = "phenotype.db"
        private const val MINIMAL_PHENOTYPE_VERSION = 1034

        private const val TARGET_NATIVE_LIB = "phixit_hook"
    }

    init {
        Log.d(TAG, "Module initialized")
    }

    external fun nativeHandlePhenotype(connectionPtr: Long, handler: FlagsContentHandler)

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (lpparam == null) return

        Log.d(TAG, "Received ${lpparam.packageName} (${lpparam.processName})")

        // TODO: Do I need special guard by process name or can I ignore it and take all GMS processes?
        if (!(lpparam.packageName == TARGET_PACKAGE_NAME &&
                    (/*BuildConfig.DEBUG ||*/ lpparam.processName == TARGET_PROCESS_NAME))) return

        Log.i(TAG, "Required package has been found, waiting for context")

        hookApplication(
            lpparam = lpparam,
            onCreate = { context ->
                Log.i(TAG, "Context received, starting extracting libraries")

                // TODO: Explain
                val extractor = NativeLibExtractor(context)
                extractor.loadLibrary(TARGET_NATIVE_LIB)

                Log.i(TAG, "Native libraries extracted, installing hooks")

                hookOpenDatabase(
                    lpparam = lpparam,
                    onOpenDatabase = { db ->
                        if (File(db.path).name != TARGET_DATABASE_NAME)
                            return@hookOpenDatabase

                        handlePhenotype(db)
                    }
                )

                Log.i(TAG, "All done for ${lpparam.packageName} (${lpparam.processName})")
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
            Log.e(TAG, "Phenotype version is too low for Phixit (${db.version})")
            return
        }

        Log.i(TAG, "Phenotype contains Phixit schema, trying to create survival trigger")

        val ptr = getConnectionPtr(db)

        if (ptr == null || ptr == 0L) {
            Log.e(TAG, "Failed to obtain native connection pointer")
            return
        }

        Log.i(TAG, "SQLite native connectionPtr = $ptr")

        nativeHandlePhenotype(
            connectionPtr = ptr,
            handler = object : FlagsContentHandler {
                // TODO: Merge and return
                override fun handle(packageId: Int, content: ByteArray): ByteArray {
                    return ByteArray(0)
                }
            }
        )
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
            Log.e(TAG, "Reflection failed", t)
            null
        }
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

    fun safeFindAndHookMethod(
        clazz: Class<*>,
        methodName: String,
        vararg parameterTypesAndCallback: Any
    ): XC_MethodHook.Unhook? {
        return try {
            XposedHelpers.findAndHookMethod(clazz, methodName, *parameterTypesAndCallback)
        } catch (t: Throwable) {
            Log.e(TAG, "Failed to hook $methodName in ${clazz.name}: ${t.message}")
            t.printStackTrace()
            null
        }
    }

    // Minimum known version is 1034, the current one is 1035, I think this check is enough
    private val SQLiteDatabase.isPhixit
            get() = this.version >= MINIMAL_PHENOTYPE_VERSION
}