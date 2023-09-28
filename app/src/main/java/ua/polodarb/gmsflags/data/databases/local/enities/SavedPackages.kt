package ua.polodarb.gmsflags.data.databases.local.enities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_packages")
data class SavedPackages(
    @PrimaryKey @ColumnInfo(name = "pkg_name") val pkgName: String
)