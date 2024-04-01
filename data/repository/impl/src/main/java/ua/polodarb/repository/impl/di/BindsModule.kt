package ua.polodarb.repository.impl.di

import org.koin.dsl.bind
import org.koin.dsl.module
import ua.polodarb.repository.googleUpdates.GoogleUpdatesRepository
import ua.polodarb.repository.googleUpdates.mapper.GoogleUpdatesMapper
import ua.polodarb.repository.impl.googleUpdates.GoogleUpdatesRepositoryImpl

val repositoryBindsModule = module {

    single { GoogleUpdatesRepositoryImpl(
        network = get(),
        mapper = GoogleUpdatesMapper()
    ) } bind GoogleUpdatesRepository::class

}