package ua.polodarb.network.impl.di

import io.ktor.client.engine.android.Android
import org.koin.dsl.bind
import org.koin.dsl.module
import ua.polodarb.network.suggestedFlags.SuggestedFlagsApiService
import ua.polodarb.network.impl.suggestedFlags.SuggestedFlagsApiServiceImpl
import ua.polodarb.network.appUpdates.AppUpdatesApiService
import ua.polodarb.network.impl.appUpdates.AppUpdatesApiServiceImpl
import ua.polodarb.network.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.network.impl.googleUpdates.GoogleAppUpdatesServiceImpl

val networkBindsModule = module {
    single {
        GoogleAppUpdatesServiceImpl(
            engine = Android.create(),
        )
    } bind GoogleAppUpdatesService::class

    single {
        SuggestedFlagsApiServiceImpl(
            engine = Android.create(),
        )
    } bind SuggestedFlagsApiService::class

    single {
        AppUpdatesApiServiceImpl(
            engine = Android.create(),
        )
    } bind AppUpdatesApiService::class
}