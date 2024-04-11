package ua.polodarb.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_flags")
data class SavedFlagsEntity(
    @ColumnInfo(name = "pkg_name") val pkgName: String,
    @ColumnInfo(name = "flag_name") val flagName: String,
    @ColumnInfo(name = "flag_type") val type: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)