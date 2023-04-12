package com.polodarb.gmsflags.data.local

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FlagsDAO {

    @Query("SELECT DISTINCT user FROM flags_table")
    fun getListOfAccounts(): List<FlagsEntity>

}