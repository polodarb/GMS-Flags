package ua.polodarb.repository.impl.databases.gms

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.invoke
import ua.polodarb.gms.init.InitRootDB
import ua.polodarb.repository.databases.gms.GmsDBRepository
import ua.polodarb.repository.uiStates.UiStates

class GmsDBRepositoryImpl(
    private val context: Context,
    private val rootDB: InitRootDB
): GmsDBRepository {

    private val rootDatabase get() = rootDB.getRootDatabase()

    override fun overrideFlag(
        packageName: String?,
        user: String?,
        name: String?,
        flagType: Int,
        intVal: String?,
        boolVal: String?,
        floatVal: String?,
        stringVal: String?,
        extensionVal: ByteArray?,
        committed: Int
    ) {
        rootDB.getRootDatabase().overrideFlag(
            packageName,
            user,
            name,
            flagType,
            intVal,
            boolVal,
            floatVal,
            stringVal,
            extensionVal,
            committed
        )
    }

    override suspend fun getGmsPackages() = flow<UiStates<Map<String, String>>> {
        delay(150)

        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val list = Dispatchers.IO { rootDatabase.gmsPackages }
                if (list.isNotEmpty()) emit(UiStates.Success(list))
                else emit(UiStates.Error())
            }
        }
    }

    override fun getBoolFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolFlags = Dispatchers.IO { rootDatabase.getBoolFlags(packageName) }
                emit(UiStates.Success(boolFlags))
            }
        }
    }

    override fun getIntFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val intFlags = Dispatchers.IO { rootDatabase.getIntFlags(packageName) }
                emit(UiStates.Success(intFlags))
            }
        }
    }

    override fun getFloatFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val floatFlags = Dispatchers.IO { rootDatabase.getFloatFlags(packageName) }
                emit(UiStates.Success(floatFlags))
            }
        }
    }

    override fun getStringFlags(
        packageName: String, delay: Boolean
    ) = flow<UiStates<Map<String, String>>> {
        if (delay) delay(200)
        emit(UiStates.Loading())

        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val stringFlags = Dispatchers.IO { rootDatabase.getStringFlags(packageName) }
                emit(UiStates.Success(stringFlags))
            }
        }
    }

    override fun getOverriddenBoolFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenBoolFlagsByPackage(packageName)
                }
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    override fun getOverriddenIntFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenIntFlagsByPackage(packageName)
                }
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    override fun getOverriddenFloatFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = Dispatchers.IO {
                    rootDatabase.getOverriddenFloatFlagsByPackage(packageName)
                }
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    override fun getOverriddenStringFlagsByPackage(
        packageName: String
    ) = flow<UiStates<Map<String, String>>> {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags =
                    rootDatabase.getOverriddenStringFlagsByPackage(packageName)
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    override fun getAllOverriddenBoolFlags() = flow<UiStates<Map<String, String>>> {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolOverriddenFlags = rootDatabase.allOverriddenBoolFlags
                emit(UiStates.Success(boolOverriddenFlags))
            }
        }
    }

    override fun getUsers() = flow<MutableList<String>> {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(Dispatchers.IO { rootDatabase.users })
            }
        }
    }

    override suspend fun isPhixitSchemaUsed(): Flow<Boolean> = flow {
        Log.e("repo", "init method")
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            Log.e("repo", "isInitialized - ${isInitialized.isInitialized}")
            if (isInitialized.isInitialized) {
                Log.e("repo", "data - ${rootDatabase.isPhixitSchemaUsed()}")
                emit(rootDatabase.isPhixitSchemaUsed())
            }
        }
    }

    override suspend fun isDbFullyRecreated(): Flow<Boolean> = flow {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(rootDatabase.isDbFullyRecreated())
            }
        }
    }

    override suspend fun isFlagOverridesTableEmpty(): Flow<Boolean> = flow {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(rootDatabase.isFlagOverridesTableEmpty())
            }
        }
    }

    override suspend fun getAndroidPackage(pkgName: String) = flow<String> {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                emit(Dispatchers.IO {
                    rootDatabase.androidPackage(pkgName)
                })
            }
        }
    }

    override suspend fun deleteRowByFlagName(packageName: String, name: String) = Dispatchers.IO {
        rootDatabase.deleteRowByFlagName(packageName, name)
    }

    override suspend fun deleteOverriddenFlagByPackage(packageName: String) = Dispatchers.IO {
        rootDatabase.deleteOverriddenFlagByPackage(packageName)
    }
}