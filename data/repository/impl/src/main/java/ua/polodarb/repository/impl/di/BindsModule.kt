package ua.polodarb.repository.impl.di

import org.koin.dsl.module
import ua.polodarb.repository.databases.local.LocalDBRepository
import ua.polodarb.repository.googleUpdates.GoogleUpdatesRepository
import ua.polodarb.repository.impl.databases.local.LocalDBRepositoryImpl
import ua.polodarb.repository.impl.googleUpdates.GoogleUpdatesRepositoryImpl

val repositoryBindsModule = module {

    single<GoogleUpdatesRepository> { GoogleUpdatesRepositoryImpl(get(), get()) }

    single<LocalDBRepository> { LocalDBRepositoryImpl(get()) }

}