package ua.polodarb.gmsflags.di

import org.koin.androidx.compose.viewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.polodarb.flagsChange.FlagChangeScreenViewModel
import ua.polodarb.flagsChange.extScreens.AddMultipleFlagsViewModel
import ua.polodarb.flagsfile.LoadFileScreenViewModel
import ua.polodarb.gmsflags.errors.gms.phixit.PhixitDetectViewModel
import ua.polodarb.gmsflags.ui.MainActivity
import ua.polodarb.gmsflags.ui.MainActivityViewModel
import ua.polodarb.network.impl.suggestedFlags.SuggestedFlagsApiServiceImpl
import ua.polodarb.saved.SavedScreenViewModel
import ua.polodarb.search.SearchScreenViewModel
import ua.polodarb.settings.SettingsViewModel
import ua.polodarb.suggestions.SuggestionScreenViewModel
import ua.polodarb.updates.UpdatesScreenViewModel

val viewModelsModule = module {

    viewModel {
        MainActivityViewModel(
            gmsDBRepository = get()
        )
    }

    viewModel {
        FlagChangeScreenViewModel(
            pkgName = get(),
            repository = get(),
            roomRepository = get(),
            gmsDBInteractor = get(),
            flagsFromFileRepository = get()
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
            repository = get(),
            sharedPrefs = get(),
            datastore = get()
        )
    }

    viewModel {
        SuggestionScreenViewModel(
            repository = get(),
            appsRepository = get(),
            interactor = get(),
            flagsUseCase = get()
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
            repository = get(),
        )
    }

    viewModel {
        PhixitDetectViewModel(
            datastore = get()
        )
    }
}