package ua.polodarb.platform.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.polodarb.platform.providers.LocalFilesProvider

val platformModule = module {
    single {
        LocalFilesProvider(
            context = androidContext()
        )
    }
}