package ua.polodarb.local.impl.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import ua.polodarb.local.impl.AppDatabase

private object LocalConstants {
    const val DATABASE_NAME = "gms_flags_database"
}

val localDatabaseModule = module {
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
