package ua.polodarb.gmsflags.di

import org.koin.dsl.module
import ua.polodarb.gmsflags.data.repo.interactors.GmsDBInteractor

val interactorsModule = module {

    single {
        GmsDBInteractor(
            context = get(),
            repository = get()
        )
    }

}