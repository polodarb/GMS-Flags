package ua.polodarb.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_packages")
data class SavedPackagesEntity(
    @PrimaryKey @ColumnInfo(name = "pkg_name") val pkgName: String
)