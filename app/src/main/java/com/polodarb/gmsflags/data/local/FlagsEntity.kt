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
)