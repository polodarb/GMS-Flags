package ua.polodarb.gmsflags.errors.gms.stateCheck.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import ua.polodarb.gmsflags.errors.gms.stateCheck.GmsCrashesDetectWorker

val stateCheckWorkerModule = module {
    worker {
        GmsCrashesDetectWorker(
            context = androidContext(),
            workerParameters = get()
        )
    }
}
