package com.polodarb.gmsflags.data.repository

import com.polodarb.gmsflags.data.local.FlagsDAO
import javax.inject.Inject

class Repository @Inject constructor(
    val localDB: FlagsDAO
) {

}