package ua.polodarb.gmsflags.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ua.polodarb.gmsflags.ui.screens.flagChangeScreen.FlagChangeScreenViewModel
import ua.polodarb.gmsflags.ui.screens.packagesScreen.PackagesScreenViewModel

val viewModelsModule = module {

    viewModel<PackagesScreenViewModel> {
        PackagesScreenViewModel(
            repository = get()
        )
    }

    viewModel<FlagChangeScreenViewModel> {
        FlagChangeScreenViewModel(
            pkgName = get(),
            repository = get()
        )
    }

}