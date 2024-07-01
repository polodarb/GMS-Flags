package ua.polodarb.byteUtils.di

import org.koin.dsl.module
import ua.polodarb.byteUtils.ByteUtils

val byteUtilsModule = module {
    single { ByteUtils() }
}