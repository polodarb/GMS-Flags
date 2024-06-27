package ua.polodarb.protobuf.di

import org.koin.dsl.module
import ua.polodarb.protobuf.ProtobufManager

val protobufModule = module {
    single { ProtobufManager() }
}