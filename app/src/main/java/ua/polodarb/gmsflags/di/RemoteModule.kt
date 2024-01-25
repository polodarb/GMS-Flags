package ua.polodarb.gmsflags.di

import io.ktor.client.engine.android.Android
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.remote.flags.FlagsApiServiceImpl
import ua.polodarb.gmsflags.data.remote.github.GithubApiServiceImpl
import ua.polodarb.gmsflags.data.remote.googleUpdates.GoogleAppUpdatesService
import ua.polodarb.gmsflags.data.remote.googleUpdates.GoogleAppUpdatesServiceImpl

val remoteModule = module {
    single { GithubApiServiceImpl(engine = Android.create()) }
    single { FlagsApiServiceImpl(engine = Android.create()) }
    single { GoogleAppUpdatesServiceImpl(engine = Android.create()) }
    single<GoogleAppUpdatesService> { GoogleAppUpdatesServiceImpl(engine = Android.create()) }
}
