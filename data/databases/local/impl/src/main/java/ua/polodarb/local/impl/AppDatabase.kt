package ua.polodarb.local.impl

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.polodarb.local.entities.SavedFlagsEntity
import ua.polodarb.local.entities.SavedPackagesEntity
import ua.polodarb.local.impl.dao.FlagsDAO
import ua.polodarb.local.impl.dao.PackagesDAO

@Database(
    entities = [SavedPackagesEntity::class, SavedFlagsEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun packagesDao(): PackagesDAO

    abstract fun flagsDao(): FlagsDAO

}