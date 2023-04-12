package com.polodarb.gmsflags.di

import android.app.Application
import androidx.room.Room
import com.polodarb.gmsflags.data.local.AppDatabase
import com.polodarb.gmsflags.data.local.FlagsDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        app: Application
    ): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "flags_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDao(
        appDatabase: AppDatabase
    ): FlagsDAO {
        return appDatabase.flagsDao()
    }

}