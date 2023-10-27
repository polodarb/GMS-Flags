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
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import ua.polodarb.gmsflags.di.appModule
import ua.polodarb.gmsflags.di.databaseModule
import ua.polodarb.gmsflags.di.remoteModule
import ua.polodarb.gmsflags.di.repositoryModule
import ua.polodarb.gmsflags.di.viewModelsModule
import ua.polodarb.gmsflags.ui.CrashActivity
import ua.polodarb.gmsflags.ui.ExceptionHandler
import ua.polodarb.gmsflags.utils.Constants

class GMSApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG)
            ExceptionHandler.initialize(this, CrashActivity::class.java)

        DynamicColors.applyToActivitiesIfAvailable(this)

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@GMSApplication)
            modules(listOf(appModule, viewModelsModule, databaseModule, repositoryModule, remoteModule))
        }
    }

}
