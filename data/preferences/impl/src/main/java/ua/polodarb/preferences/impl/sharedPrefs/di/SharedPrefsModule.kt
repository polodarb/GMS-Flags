package ua.polodarb.preferences.impl.sharedPrefs.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.polodarb.preferences.impl.sharedPrefs.PreferencesManagerImpl
import ua.polodarb.preferences.sharedPrefs.PreferencesManager

val sharedPrefsModule = module {
    single {
        PreferencesManagerImpl(
            context = androidContext()
        )
    } bind PreferencesManager::class
}