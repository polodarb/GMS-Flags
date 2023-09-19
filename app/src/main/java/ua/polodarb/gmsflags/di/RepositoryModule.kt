package ua.polodarb.gmsflags.di

import org.koin.dsl.module
import ua.polodarb.gmsflags.data.repo.AppsListRepository
import ua.polodarb.gmsflags.data.repo.GmsDBRepository
import ua.polodarb.gmsflags.data.repo.RoomDBRepository

val repositoryModule = module {

    single {
        GmsDBRepository(
            context = get()
        )
    }

    single {
        AppsListRepository(
            context = get()
        )
    }

    single {
        RoomDBRepository(
            savedPackagesDao = get()
        )
    }

}