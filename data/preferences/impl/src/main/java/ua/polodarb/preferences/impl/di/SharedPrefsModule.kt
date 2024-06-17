package ua.polodarb.preferences.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.polodarb.preferences.datastore.DatastoreManager
import ua.polodarb.preferences.impl.datastore.DatastoreManagerImpl
import ua.polodarb.preferences.impl.sharedPrefs.PreferencesManagerImpl
import ua.polodarb.preferences.sharedPrefs.PreferencesManager

val prefsModule = module {
    single {
        PreferencesManagerImpl(
            context = androidContext()
        )
    } bind PreferencesManager::class

    single {
        DatastoreManagerImpl(
            context = androidContext()
        )
    } bind DatastoreManager::class
}