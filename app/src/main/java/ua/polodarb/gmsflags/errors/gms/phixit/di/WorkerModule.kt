package ua.polodarb.gmsflags.errors.gms.phixit.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import ua.polodarb.gmsflags.errors.gms.stateCheck.GmsCrashesDetectWorker
import ua.polodarb.gmsflags.errors.gms.phixit.PhixitDetectWorker

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