package ua.polodarb.repository.impl.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.polodarb.repository.appsList.AppsListRepository
import ua.polodarb.repository.databases.gms.GmsDBInteractor
import ua.polodarb.repository.databases.gms.GmsDBRepository
import ua.polodarb.repository.databases.local.LocalDBRepository
import ua.polodarb.repository.googleUpdates.GoogleUpdatesRepository
import ua.polodarb.repository.impl.appsList.AppsListRepositoryImpl
import ua.polodarb.repository.impl.databases.gms.GmsDBInteractorImpl
import ua.polodarb.repository.impl.databases.gms.GmsDBRepositoryImpl
import ua.polodarb.repository.impl.databases.local.LocalDBRepositoryImpl
import ua.polodarb.repository.impl.googleUpdates.GoogleUpdatesRepositoryImpl
import ua.polodarb.repository.impl.settings.SettingsRepositoryImpl
import ua.polodarb.repository.settings.SettingsRepository

val repositoryBindsModule = module {

    single {
        GoogleUpdatesRepositoryImpl(get(), get())
    } bind GoogleUpdatesRepository::class

    single {
        LocalDBRepositoryImpl(get())
    } bind LocalDBRepository::class

    single {
        GmsDBRepositoryImpl(
            context = androidContext(),
            rootDB = get()
        )
    } bind GmsDBRepository::class

    single {
        GmsDBInteractorImpl(
            repository = get()
        )
    } bind GmsDBInteractor::class

    single {
        AppsListRepositoryImpl(
            context = androidContext(),
            rootDB = get()
        )
    } bind AppsListRepository::class

    single {
        SettingsRepositoryImpl(
            context = androidContext(),
            localDB = get(),
            rootDB = get()
        )
    } bind SettingsRepository::class

}