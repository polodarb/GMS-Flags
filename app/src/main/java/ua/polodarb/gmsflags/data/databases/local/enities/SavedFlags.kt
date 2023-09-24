package ua.polodarb.gmsflags.data.databases.local.enities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_flags")
data class SavedFlags(
    @PrimaryKey @ColumnInfo(name = "pkg_name") val pkgName: String,
    @ColumnInfo(name = "flag_name") val flagName: String,
    @ColumnInfo(name = "flag_type") val type: String
)