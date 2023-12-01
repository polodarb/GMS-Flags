package ua.polodarb.gmsflags.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.remote.flags.FlagsApiServiceImpl
import ua.polodarb.gmsflags.ui.screens.apps.AppsScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChange.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChange.extScreens.AddMultipleFlagsViewModel
import ua.polodarb.gmsflags.ui.screens.packages.PackagesScreenViewModel
import ua.polodarb.gmsflags.ui.screens.saved.SavedScreenViewModel
import ua.polodarb.gmsflags.ui.screens.settings.SettingsViewModel
import ua.polodarb.gmsflags.ui.screens.suggestions.SuggestionScreenViewModel

val viewModelsModule = module {

    viewModel {
        PackagesScreenViewModel(
            gmsRepository = get(),
            roomRepository = get()
        )
    }

    viewModel {
        FlagChangeScreenViewModel(
            pkgName = get(),
            repository = get(),
            roomRepository = get(),
            gmsDBInteractor = get()
        )
    }

    viewModel {
        AppsScreenViewModel(
            repository = get()
        )
    }

    viewModel {
        SuggestionScreenViewModel(
            application = get(),
            repository = get(),
            appsRepository = get(),
            flagsApiService = get<FlagsApiServiceImpl>(),
            mapper = get(),
            interactor = get()
        )
    }

    viewModel {
        SavedScreenViewModel(
            roomRepository = get()
        )
    }

    viewModel {
        SettingsViewModel(
            settingsRepository = get()
        )
    }

    viewModel {
        AddMultipleFlagsViewModel(
            pkgName = get(),
            repository = get(),
            gmsDBInteractor = get()
        )
    }
}
