package ua.polodarb.gms.init

import kotlinx.coroutines.flow.Flow
import ua.polodarb.gms.IRootDatabase

data class DatabaseInitializationState(val isInitialized: Boolean)

interface InitRootDB {

    val databaseInitializationStateFlow: Flow<DatabaseInitializationState>

    var isRootDatabaseInitialized: Boolean

    fun setDatabaseInitialized(isInitialized: Boolean)

    fun initDB()

    fun getRootDatabase(): IRootDatabase

}