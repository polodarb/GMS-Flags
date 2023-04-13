package com.polodarb.gmsflags.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "flags_table",
    primaryKeys = ["packageName", "version", "flagType", "partitionId", "user", "name", "committed"]
)
data class FlagsEntity(
    val packageName: String,
    val version: Int,
    val flagType: Int,
    val partitionId: Int,
    val user: String,
    val name: String,
    val intVal: Int,
    val boolVal: Boolean,
    @ColumnInfo(typeAffinity = ColumnInfo.REAL) val floatVal: Float,
    val stringVal: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val extensionVal: ByteArray,
    val committed: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FlagsEntity

        if (packageName != other.packageName) return false
        if (version != other.version) return false
        if (flagType != other.flagType) return false
        if (partitionId != other.partitionId) return false
        if (user != other.user) return false
        if (name != other.name) return false
        if (intVal != other.intVal) return false
        if (boolVal != other.boolVal) return false
        if (floatVal != other.floatVal) return false
        if (stringVal != other.stringVal) return false
        if (!extensionVal.contentEquals(other.extensionVal)) return false
        if (committed != other.committed) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageName.hashCode()
        result = 31 * result + version
        result = 31 * result + flagType
        result = 31 * result + partitionId
        result = 31 * result + user.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + intVal
        result = 31 * result + boolVal.hashCode()
        result = 31 * result + floatVal.hashCode()
        result = 31 * result + stringVal.hashCode()
        result = 31 * result + extensionVal.contentHashCode()
        result = 31 * result + committed.hashCode()
        return result
    }
}