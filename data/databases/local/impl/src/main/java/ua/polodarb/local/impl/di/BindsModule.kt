package ua.polodarb.local.impl.di

import org.koin.dsl.module
import ua.polodarb.local.impl.source.LocalDBSourceImpl
import ua.polodarb.local.source.LocalDBSource

val localDBBindsModule = module {

    single<LocalDBSource> { LocalDBSourceImpl(
        packagesDao = get(),
        flagsDao = get()
    ) }

}