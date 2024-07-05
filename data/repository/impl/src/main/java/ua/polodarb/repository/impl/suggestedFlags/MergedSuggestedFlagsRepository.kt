package ua.polodarb.repository.impl.suggestedFlags

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ua.polodarb.common.FlagsTypes
import ua.polodarb.gms.init.InitRootDB
import ua.polodarb.network.Resource
import ua.polodarb.network.suggestedFlags.SuggestedFlagsApiService
import ua.polodarb.network.suggestedFlags.model.SuggestedFlagsNetModel
import ua.polodarb.platform.providers.LocalFilesProvider
import ua.polodarb.repository.suggestedFlags.SuggestedFlagsRepository
import ua.polodarb.repository.suggestedFlags.models.FlagDetails
import ua.polodarb.repository.suggestedFlags.models.MergedAllTypesFlags
import ua.polodarb.repository.suggestedFlags.models.MergedAllTypesOverriddenFlags
import ua.polodarb.repository.suggestedFlags.models.SuggestedFlagsRepoModel
import ua.polodarb.repository.suggestedFlags.models.SuggestedFlagsRepoModel.Companion.toRepoModel
import ua.polodarb.repository.uiStates.UiStates

class SuggestedFlagsRepositoryImpl(
    private val rootDB: InitRootDB,
    private val localFilesProvider: LocalFilesProvider
) : SuggestedFlagsRepository {

    private val rootDatabase by lazy {
        rootDB.getRootDatabase()
    }

    override suspend fun loadSuggestedFlags(): List<SuggestedFlagsRepoModel>? {
        return withContext(Dispatchers.IO) {
            try {
                val localFlagsFile = localFilesProvider.getLocalSuggestedFlagsFile()

                if (localFlagsFile.exists() && localFlagsFile.length() > 0) {
                    val pkgContent = localFilesProvider.getSuggestedFlagsData()
                    val result = Json.decodeFromString<List<SuggestedFlagsNetModel>>(pkgContent)
                        .map { it.toRepoModel() }
                    return@withContext result
                } else {
                    val pkgContent = localFilesProvider.getSuggestedFlagsDataFromAssets()

                    localFlagsFile.writeText(pkgContent)

                    val result = Json.decodeFromString<List<SuggestedFlagsNetModel>>(pkgContent)
                        .map { it.toRepoModel() }
                    return@withContext result
                }
            } catch (e: Exception) {
                Log.e("gmsf", "Error loading suggested flags: ${e.message}")
                return@withContext null
            }
        }
    }


    override suspend fun getMergedOverriddenFlagsByPackage(pkg: String): Flow<MergedAllTypesOverriddenFlags> =
        flow {
            rootDB.databaseInitializationStateFlow.collect { isInitialized ->
                if (isInitialized.isInitialized) {

                    val boolFlags = rootDatabase.getOverriddenBoolFlagsByPackage(pkg)
                    val intFlags = rootDatabase.getOverriddenIntFlagsByPackage(pkg)
                    val floatFlags = rootDatabase.getOverriddenFloatFlagsByPackage(pkg)
                    val stringFlags = rootDatabase.getOverriddenStringFlagsByPackage(pkg)

                    emit(
                        MergedAllTypesOverriddenFlags(
                            boolFlag = boolFlags,
                            intFlag = intFlags,
                            floatFlag = floatFlags,
                            stringFlag = stringFlags
                        )
                    )
                }
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun getMergedAllFlags(): Flow<UiStates<MergedAllTypesFlags>> = flow {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolFlags: Map<String, String> =
                    rootDatabase.allBoolFlags
                val intFlags: Map<String, String> = rootDatabase.allIntFlags
                val floatFlags: Map<String, String> =
                    rootDatabase.allFloatFlags
                val stringFlags: Map<String, String> =
                    rootDatabase.allStringFlags

                val mergedBoolFlags = boolFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, FlagsTypes.BOOLEAN)
                }

                val mergedIntFlags = intFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, FlagsTypes.INTEGER)
                }

                val mergedFloatFlags = floatFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, FlagsTypes.FLOAT)
                }

                val mergedStringFlags = stringFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, FlagsTypes.STRING)
                }

                emit(
                    UiStates.Success(
                        MergedAllTypesFlags(
                            boolFlag = mergedBoolFlags,
                            intFlag = mergedIntFlags,
                            floatFlag = mergedFloatFlags,
                            stringFlag = mergedStringFlags
                        )
                    )
                )
            }
        }
    }.flowOn(Dispatchers.IO)
}