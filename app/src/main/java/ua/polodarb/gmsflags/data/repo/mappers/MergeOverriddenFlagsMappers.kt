package ua.polodarb.gmsflags.data.repo.mappers

import android.content.Context
import ua.polodarb.gmsflags.GMSApplication

class MergeOverriddenFlagsInteractor(
    private val context: Context
) {

    private val gmsApplication = (context as GMSApplication)

    fun getMergedOverriddenFlagsByPackage(pkg: String): MergedOverriddenFlag {

                val boolFlags =
                    gmsApplication.getRootDatabase().getOverriddenBoolFlagsByPackage(pkg)
                val intFlags = gmsApplication.getRootDatabase().getOverriddenIntFlagsByPackage(pkg)
                val floatFlags =
                    gmsApplication.getRootDatabase().getOverriddenFloatFlagsByPackage(pkg)
                val stringFlags =
                    gmsApplication.getRootDatabase().getOverriddenStringFlagsByPackage(pkg)

                return(
                    MergedOverriddenFlag(
                        boolFlag = boolFlags,
                        intFlag = intFlags,
                        floatFlag = floatFlags,
                        stringFlag = stringFlags
                    )
                )

    }
}

data class MergedOverriddenFlag(
    val boolFlag: Map<String, String>,
    val intFlag: Map<String, String>,
    val floatFlag: Map<String, String>,
    val stringFlag: Map<String, String>
)