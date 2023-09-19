package ua.polodarb.gmsflags.data.databases.local.enities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "saved_packages")
data class SavedPackages(
    @ColumnInfo(name = "pkg_name") val pkgName: String?
)