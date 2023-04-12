package com.polodarb.gmsflags.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flags_table")
data class FlagsEntity(
    @PrimaryKey val packageName: String,
    @PrimaryKey val version: UInt,
    @PrimaryKey val flagType: Int,
    @PrimaryKey val partitionId: Int,
    @PrimaryKey val user: String,
    @PrimaryKey val name: String,
    val intVal: Int,
    val boolVal: Boolean,
    @ColumnInfo(typeAffinity = ColumnInfo.REAL) val floatVal: Float,
    val stringVal: String,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val extensionVal: ByteArray,
    @PrimaryKey val committed: Boolean
)