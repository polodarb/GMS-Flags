package ua.polodarb.gmsflags.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.remote.flags.FlagsApiServiceImpl
import ua.polodarb.gmsflags.ui.screens.search.SearchScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChange.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.flagChange.extScreens.AddMultipleFlagsViewModel
import ua.polodarb.gmsflags.ui.screens.packages.PackagesScreenViewModel
import ua.polodarb.gmsflags.ui.screens.settings.SettingsViewModel
import ua.polodarb.gmsflags.ui.screens.suggestions.SuggestionScreenViewModel
import ua.polodarb.saved.SavedScreenViewModel
import ua.polodarb.updates.UpdatesScreenViewModel

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
        SearchScreenViewModel(
            repository = get(),
            gmsRepository = get(),
            roomRepository = get(),
            mergeFlagsMapper = get(),
            gmsDBInteractor = get()
        )
    }

    viewModel {
        UpdatesScreenViewModel(
            repository = get()
//            googleAppUpdatesService = get(),
//            googleUpdatesMapper = get(),
//            sharedPrefs = get()
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
            localDBRepository = get()
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
