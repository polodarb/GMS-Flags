package ua.polodarb.gmsflags.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.databases.local.AppDatabase

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            AppDatabase::class.java,
            "gms_flags_database"
        ).build()
    }

    single {
        val database = get<AppDatabase>()
        database.packagesDao()
    }

    single {
        val database = get<AppDatabase>()
        database.flagsDao()
    }
}
