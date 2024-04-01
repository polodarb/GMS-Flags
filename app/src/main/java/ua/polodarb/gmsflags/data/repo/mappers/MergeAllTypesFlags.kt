package ua.polodarb.gmsflags.data.repo.mappers

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.flow
import ua.polodarb.gmsflags.GMSApplication
import ua.polodarb.repository.uiStates.UiStates

class MergeFlagsMapper(
    context: Context
) {

    private val gmsApplication = (context as GMSApplication)

    fun getMergedOverriddenFlagsByPackage(pkg: String): MergedAllTypesOverriddenFlags {

        val boolFlags =
            gmsApplication.getRootDatabase().getOverriddenBoolFlagsByPackage(pkg)
        val intFlags = gmsApplication.getRootDatabase().getOverriddenIntFlagsByPackage(pkg)
        val floatFlags =
            gmsApplication.getRootDatabase().getOverriddenFloatFlagsByPackage(pkg)
        val stringFlags =
            gmsApplication.getRootDatabase().getOverriddenStringFlagsByPackage(pkg)

        return (MergedAllTypesOverriddenFlags(
            boolFlag = boolFlags,
            intFlag = intFlags,
            floatFlag = floatFlags,
            stringFlag = stringFlags
        ))

    }

    fun getMergedAllFlags() = flow<ua.polodarb.repository.uiStates.UiStates<MergedAllTypesFlags>> {

        gmsApplication.databaseInitializationStateFlow.collect { isInitialized ->
            if (isInitialized.isInitialized) {
                val boolFlags: Map<String, String> =
                    gmsApplication.getRootDatabase().allBoolFlags
                val intFlags: Map<String, String> = gmsApplication.getRootDatabase().allIntFlags
                val floatFlags: Map<String, String> =
                    gmsApplication.getRootDatabase().allFloatFlags
                val stringFlags: Map<String, String> =
                    gmsApplication.getRootDatabase().allStringFlags

                val mergedBoolFlags = boolFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, "bool")
                }

                Log.d("initAllFlags1", "mergedBoolFlags: ${boolFlags}")

                val mergedIntFlags = intFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, "int")
                }

                val mergedFloatFlags = floatFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, "float")
                }

                val mergedStringFlags = stringFlags.map { (pkgName, flagName) ->
                    FlagDetails(pkgName, flagName, "string")
                }

                emit(
                    ua.polodarb.repository.uiStates.UiStates.Success(
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

data class MergedAllTypesOverriddenFlags(
    val boolFlag: Map<String, String>,
    val intFlag: Map<String, String>,
    val floatFlag: Map<String, String>,
    val stringFlag: Map<String, String>,
)

data class MergedAllTypesFlags(
    val boolFlag: List<FlagDetails>,
    val intFlag: List<FlagDetails>,
    val floatFlag: List<FlagDetails>,
    val stringFlag: List<FlagDetails>
) {
    fun isNotEmpty() = boolFlag.isNotEmpty() && intFlag.isNotEmpty() && floatFlag.isNotEmpty() && stringFlag.isNotEmpty()
}

data class FlagDetails(
    val pkgName: String,
    val flagName: String,
    val type: String
)
