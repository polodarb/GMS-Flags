package ua.polodarb.gmsflags.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.workers.GoogleUpdatesCheckWorker

val workerModule = module {

    worker {
        GoogleUpdatesCheckWorker(
            context = androidContext(),
            workerParameters = get(),
            googleAppUpdatesService = get(),
            googleUpdatesMapper = get(),
            sharedPrefs = get()
        )
    }

}