package ua.polodarb.repository.impl.suggestedFlags

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ua.polodarb.common.FlagsTypes
import ua.polodarb.gms.init.InitRootDB
import ua.polodarb.repository.suggestedFlags.MergedSuggestedFlagsRepository
import ua.polodarb.repository.suggestedFlags.model.FlagDetails
import ua.polodarb.repository.suggestedFlags.model.MergedAllTypesFlags
import ua.polodarb.repository.suggestedFlags.model.MergedAllTypesOverriddenFlags
import ua.polodarb.repository.uiStates.UiStates

class MergedSuggestedFlagsRepositoryImpl(
    private val context: Context,
    private val rootDB: InitRootDB
): MergedSuggestedFlagsRepository {

    private val rootDatabase = rootDB.getRootDatabase()

    override suspend fun getMergedOverriddenFlagsByPackage(pkg: String): MergedAllTypesOverriddenFlags {
        val boolFlags =
            rootDatabase.getOverriddenBoolFlagsByPackage(pkg)
        val intFlags = rootDatabase.getOverriddenIntFlagsByPackage(pkg)
        val floatFlags =
            rootDatabase.getOverriddenFloatFlagsByPackage(pkg)
        val stringFlags =
            rootDatabase.getOverriddenStringFlagsByPackage(pkg)

        return (MergedAllTypesOverriddenFlags(
            boolFlag = boolFlags,
            intFlag = intFlags,
            floatFlag = floatFlags,
            stringFlag = stringFlags
        ))
    }

    override suspend fun getMergedAllFlags(): Flow<UiStates<MergedAllTypesFlags>> = flow {
        rootDB.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolFlags: Map<String, String> =
                    rootDatabase.allBoolFlags
                val intFlags: Map<String, String> =rootDatabase.allIntFlags
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
    }
}