package ua.polodarb.gmsflags.di

import org.koin.dsl.module
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.data.repo.DatabaseRepository

val appModule = module {

    single {
        DatabaseRepository(
            context = get()
        )
    }

    single {
        AppsListRepository(
            context = get()
        )
    }

}