package ua.polodarb.gmsflags.di

import org.koin.androidx.compose.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.polodarb.flagsChange.FlagChangeScreenViewModel
import ua.polodarb.flagsChange.extScreens.AddMultipleFlagsViewModel
import ua.polodarb.flagsfile.LoadFileScreenViewModel
import ua.polodarb.network.impl.suggestedFlags.SuggestedFlagsApiServiceImpl
import ua.polodarb.saved.SavedScreenViewModel
import ua.polodarb.search.SearchScreenViewModel
import ua.polodarb.settings.SettingsViewModel
import ua.polodarb.suggestions.SuggestionScreenViewModel
import ua.polodarb.updates.UpdatesScreenViewModel

val viewModelsModule = module {

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
            gmsDBInteractor = get(),
            mergedFlags = get()
        )
    }

    viewModel {
        UpdatesScreenViewModel(
            repository = get()
        )
    }

    viewModel {
        SuggestionScreenViewModel(
            application = get(),
            repository = get(),
            appsRepository = get(),
            flagsApiService = get<SuggestedFlagsApiServiceImpl>(),
            interactor = get(),
            mergedFlags = get(),
            localFilesProvider = get()
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

    viewModel {
        LoadFileScreenViewModel(
            fileUri = get(),
            gmsDBInteractor = get(),
            gmsDBRepository = get(),
            repository = get()
        )
    }
}