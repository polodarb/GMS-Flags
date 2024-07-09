package ua.polodarb.domain.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ua.polodarb.domain.countryIso.SimCountryIsoUseCase
import ua.polodarb.domain.override.OverrideFlagsUseCase
import ua.polodarb.domain.suggestedFlags.SuggestedFlagsUseCase

val useCaseModule = module {
    single { OverrideFlagsUseCase(repository = get(), interactor = get(), byteUtils = get()) }
    single { SuggestedFlagsUseCase(repository = get(), appsRepository = get()) }
    single { SimCountryIsoUseCase(context = androidContext()) }
}