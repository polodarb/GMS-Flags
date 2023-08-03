package ua.polodarb.gmsflags.di

import android.app.Application
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.topjohnwu.superuser.ipc.RootService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ua.polodarb.gmsflags.IRootDatabase
import ua.polodarb.gmsflags.data.db.RootDatabase

class GMSApplication : Application() {

    private var isRootDatabaseInitialized = false
    private lateinit var rootDatabase: IRootDatabase

    override fun onCreate() {
        super.onCreate()

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

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GMSApplication)
            modules(listOf(appModule, viewModelsModule))
        }
    }

    fun getRootDatabase(): IRootDatabase {
        if (!isRootDatabaseInitialized) {
            throw IllegalStateException("RootDatabase is not initialized yet.")
        }
        return rootDatabase
    }
}
