package ua.polodarb.gmsflags.errors.phixit.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import ua.polodarb.domain.OverrideFlagsUseCase
import ua.polodarb.gmsflags.errors.gms.GmsCrashesDetectWorker
import ua.polodarb.gmsflags.errors.phixit.PhixitDetectWorker
import ua.polodarb.repository.googleUpdates.mapper.GoogleUpdatesMapper

val workerPhixitModule = module {
    worker {
        PhixitDetectWorker(
            context = androidContext(),
            workerParameters = get(),
            rootDB = get(),
            repository = get(),
            datastore = get(),
            overrideUseCase = get()
        )
    }

    worker {
        GmsCrashesDetectWorker(
            context = androidContext(),
            workerParameters = get()
        )
    }
}