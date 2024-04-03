package ua.polodarb.network.impl.di

import io.ktor.client.engine.android.Android
import org.koin.dsl.module
import ua.polodarb.network.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.network.impl.googleUpdates.GoogleAppUpdatesServiceImpl

val networkBindsModule = module {
    single<GoogleAppUpdatesService> {
        GoogleAppUpdatesServiceImpl(
            engine = Android.create(),
        )
    }
}