package ua.polodarb.gmsflags.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.polodarb.gmsflags.ui.screens.appsScreen.AppsScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreenViewModel
import ua.polodarb.gmsflags.ui.screens.suggestionsScreen.SuggestionScreenViewModel

val viewModelsModule = module {
    viewModel {
        PackagesScreenViewModel(
            repository = get()
        )
    }

    viewModel {
        FlagChangeScreenViewModel(
            pkgName = get(),
            repository = get()
        )
    }

    viewModel {
        AppsScreenViewModel(
            repository = get()
        )
    }

    viewModel {
        SuggestionScreenViewModel(
            repository = get()
        )
    }
}