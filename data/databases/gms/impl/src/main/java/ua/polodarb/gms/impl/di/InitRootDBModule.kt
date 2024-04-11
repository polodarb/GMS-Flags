package ua.polodarb.gms.impl.di

import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import ua.polodarb.gms.impl.init.InitRootDBImpl
import ua.polodarb.gms.init.InitRootDB

val initRootDBModule = module {
    single { InitRootDBImpl(get()) } bind InitRootDB::class
}