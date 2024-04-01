package ua.polodarb.gmsflags

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.google.android.material.color.DynamicColors
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ipc.RootService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ua.polodarb.gmsflags.data.databases.gms.RootDatabase
import ua.polodarb.gmsflags.di.appModule
import ua.polodarb.gmsflags.di.databaseModule
import ua.polodarb.gmsflags.di.interactorsModule
import ua.polodarb.gmsflags.di.remoteModule
import ua.polodarb.gmsflags.di.repositoryModule
import ua.polodarb.gmsflags.di.viewModelsModule
import ua.polodarb.gmsflags.di.workerModule
import ua.polodarb.gmsflags.ui.CrashActivity
import ua.polodarb.gmsflags.ui.ExceptionHandler
import ua.polodarb.gmsflags.utils.Constants
import ua.polodarb.network.impl.di.networkBindsModule
import ua.polodarb.repository.impl.di.repositoryBindsModule

data class DatabaseInitializationState(val isInitialized: Boolean)

class GMSApplication : Application() {
    private companion object {
        const val SHELL_TIMEOUT = 10L
    }

    private val shellConfig = Shell.Builder.create()
        .setFlags(Shell.FLAG_REDIRECT_STDERR or Shell.FLAG_MOUNT_MASTER)
        .setTimeout(SHELL_TIMEOUT)

    private val _databaseInitializationStateFlow =
        MutableStateFlow(DatabaseInitializationState(false))
    val databaseInitializationStateFlow: Flow<DatabaseInitializationState> =
        _databaseInitializationStateFlow

    fun setDatabaseInitialized(isInitialized: Boolean) {
        _databaseInitializationStateFlow.value = DatabaseInitializationState(isInitialized)
    }

    var isRootDatabaseInitialized = false
    private lateinit var rootDatabase: IRootDatabase

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG)
            ExceptionHandler.initialize(this, CrashActivity::class.java)

        DynamicColors.applyToActivitiesIfAvailable(this)

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@GMSApplication)
            workManagerFactory()
            modules(
                listOf(
                    appModule,
                    viewModelsModule,
                    databaseModule,
                    repositoryModule,
                    remoteModule,
                    interactorsModule,
                    workerModule,
                    networkBindsModule,
                    repositoryBindsModule
                )
            )
        }
    }

    fun initShell() {
        try {
            Shell.setDefaultBuilder(shellConfig)
        } catch (_: IllegalStateException) {
            /* When application configuration changed (ex. orientation changed)
             * this function are called again, since onCreate in MainActivity
             * are called again, and since shell instance can be already created
             * it throws runtime exception, so just ignore it, since this call is ok.
             */
        }
    }

    fun initDB() {
        val intent = Intent(this, RootDatabase::class.java)
        val service = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                rootDatabase = IRootDatabase.Stub.asInterface(service)
                isRootDatabaseInitialized = true
                setDatabaseInitialized(true)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                isRootDatabaseInitialized = false
                setDatabaseInitialized(false)
            }
        }
        RootService.bind(intent, service)
    }

    fun getRootDatabase(): IRootDatabase {
        check(isRootDatabaseInitialized) { Constants.GMS_DATABASE_CRASH_MSG }
        return rootDatabase
    }


}
