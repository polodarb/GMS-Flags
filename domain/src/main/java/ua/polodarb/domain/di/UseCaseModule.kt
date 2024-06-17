package ua.polodarb.domain.di

import org.koin.dsl.module
import ua.polodarb.domain.OverrideFlagsUseCase

val useCaseModule = module {
    single { OverrideFlagsUseCase(repository = get(), interactor = get()) }
}