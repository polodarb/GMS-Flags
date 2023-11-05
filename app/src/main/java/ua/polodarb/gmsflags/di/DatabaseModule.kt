package ua.polodarb.gmsflags.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ua.polodarb.gmsflags.data.databases.local.AppDatabase

private object LocalConstants {
    const val DATABASE_NAME = "gms_flags_database"
}

val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication().applicationContext,
            AppDatabase::class.java,
            LocalConstants.DATABASE_NAME
        ).build()
    }

    single { get<AppDatabase>().packagesDao()  }

    single { get<AppDatabase>().flagsDao() }
}
