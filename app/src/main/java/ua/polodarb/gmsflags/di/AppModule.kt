package ua.polodarb.gmsflags.di

import org.koin.dsl.module
import ua.polodarb.gmsflags.data.db.RootDatabase
import ua.polodarb.gmsflags.data.repo.Repository

val appModule = module {

//    single {
//        RootDatabase()
//    }

    single {
        Repository(
            context = get()
        )
    }

}