package ua.polodarb.gmsflags.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.databases.local.AppDatabase
import ua.polodarb.gmsflags.data.databases.local.dao.PackagesDAO

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            AppDatabase::class.java,
            "gms_flags_database"
        ).build()
    }

    single<PackagesDAO> {
        val database = get<AppDatabase>()
        database.packagesDao()
    }

}