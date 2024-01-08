package ua.polodarb.gmsflags.data.repo.mappers

import android.content.Context
import ua.polodarb.gmsflags.GMSApplication

class MergeFlagsMapper(
    context: Context
) {

    private val gmsApplication = (context as GMSApplication)

    fun getMergedOverriddenFlagsByPackage(pkg: String): MergedAllTypesFlags {

        val boolFlags =
            gmsApplication.getRootDatabase().getOverriddenBoolFlagsByPackage(pkg)
        val intFlags = gmsApplication.getRootDatabase().getOverriddenIntFlagsByPackage(pkg)
        val floatFlags =
            gmsApplication.getRootDatabase().getOverriddenFloatFlagsByPackage(pkg)
        val stringFlags =
            gmsApplication.getRootDatabase().getOverriddenStringFlagsByPackage(pkg)

        return (MergedAllTypesFlags(
            boolFlag = boolFlags,
            intFlag = intFlags,
            floatFlag = floatFlags,
            stringFlag = stringFlags
        ))

    }

    fun getMergedAllFlags(): MergedAllTypesFlags {

        val boolFlags =
            gmsApplication.getRootDatabase().allBoolFlags
        val intFlags = gmsApplication.getRootDatabase().allIntFlags
        val floatFlags =
            gmsApplication.getRootDatabase().allFloatFlags
        val stringFlags =
            gmsApplication.getRootDatabase().allStringFlags

        return (MergedAllTypesFlags(
            boolFlag = boolFlags,
            intFlag = intFlags,
            floatFlag = floatFlags,
            stringFlag = stringFlags
        ))

    }

}

data class MergedAllTypesFlags(
    val boolFlag: Map<String, String>,
    val intFlag: Map<String, String>,
    val floatFlag: Map<String, String>,
    val stringFlag: Map<String, String>
)
