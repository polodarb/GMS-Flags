package ua.polodarb.gmsflags.di

import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.gmsflags.data.prefs.shared.PreferencesManager

val appModule = module {
    single { androidApplication().applicationContext as GMSApplication }
    single { PreferencesManager(androidContext()) }
}
