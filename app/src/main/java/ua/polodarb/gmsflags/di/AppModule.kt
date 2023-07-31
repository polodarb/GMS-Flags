package ua.polodarb.gmsflags.di

import org.koin.dsl.module
import ua.polodarb.gmsflags.data.repo.DatabaseRepository

val appModule = module {

//    single {
//        RootDatabase()
//    }

    single {
        DatabaseRepository(
            context = get()
        )
    }

}