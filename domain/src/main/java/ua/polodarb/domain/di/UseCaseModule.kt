package ua.polodarb.domain.di

import org.koin.dsl.module
import ua.polodarb.domain.override.OverrideFlagsUseCase
import ua.polodarb.domain.suggestedFlags.SuggestedFlagsUseCase

val useCaseModule = module {
    single { OverrideFlagsUseCase(repository = get(), interactor = get()) }
    single { SuggestedFlagsUseCase(repository = get(), appsRepository = get()) }
}