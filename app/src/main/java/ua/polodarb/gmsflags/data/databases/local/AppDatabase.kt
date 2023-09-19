package ua.polodarb.gmsflags.data.databases.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.polodarb.gmsflags.data.databases.local.dao.PackagesDAO
import ua.polodarb.gmsflags.data.databases.local.enities.SavedPackages

@Database(entities = [SavedPackages::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun packagesDao(): PackagesDAO

}