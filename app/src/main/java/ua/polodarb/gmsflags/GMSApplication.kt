package ua.polodarb.gmsflags

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.topjohnwu.superuser.Shell
import com.topjohnwu.superuser.ipc.RootService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ua.polodarb.gmsflags.data.databases.gms.RootDatabase
import ua.polodarb.gmsflags.di.appModule
import ua.polodarb.gmsflags.di.viewModelsModule

class GMSApplication : Application() {

    var isRootDatabaseInitialized = false
    private lateinit var rootDatabase: IRootDatabase

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GMSApplication)
            modules(listOf(appModule, viewModelsModule))
        }
    }

    fun initShell() {
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR or Shell.FLAG_MOUNT_MASTER)
                .setTimeout(10)
        )
    }

    fun initDB() {
        val intent = Intent(this, RootDatabase::class.java)
        val service = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                rootDatabase = IRootDatabase.Stub.asInterface(service)
                isRootDatabaseInitialized = true
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                TODO("Not yet implemented")
            }
        }
        RootService.bind(intent, service)
    }

    fun getRootDatabase(): IRootDatabase {
        if (!isRootDatabaseInitialized) {
            throw IllegalStateException("RootDatabase is not initialized yet.")
        }
        return rootDatabase
    }
}
