package ua.polodarb.gmsflags.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.polodarb.gmsflags.ui.screens.appsScreen.AppsScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreenViewModel
import ua.polodarb.gmsflags.ui.screens.savedScreen.SavedScreenViewModel
import ua.polodarb.gmsflags.ui.screens.settingsScreen.SettingsViewModel
import ua.polodarb.gmsflags.ui.screens.suggestionsScreen.SuggestionScreenViewModel

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
            roomRepository = get()
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

}