package ua.polodarb.gmsflags.di

import io.ktor.client.engine.android.Android
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.remote.github.GithubApiServiceImpl

val remoteModule = module {
    single { GithubApiServiceImpl(engine = Android.create()) }
}
