package com.polodarb.gmsflags.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FlagsEntity::class], version = 1) // exportSchema = false?
abstract class AppDatabase : RoomDatabase() {

    abstract fun flagsDao(): FlagsDAO

}