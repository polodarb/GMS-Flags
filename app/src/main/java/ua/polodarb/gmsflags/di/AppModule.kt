package ua.polodarb.gmsflags.di

import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ua.polodarb.gmsflags.GMSApplication

val appModule = module {
    single { androidApplication().applicationContext as GMSApplication }
}
