package ua.polodarb.repository.databases.gms

import kotlinx.coroutines.flow.Flow
import ua.polodarb.repository.uiStates.UiStates

interface GmsDBRepository {
    fun overrideFlag(
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
    )

    suspend fun getGmsPackages(): Flow<UiStates<Map<String, String>>>

    fun getBoolFlags(packageName: String, delay: Boolean): Flow<UiStates<Map<String, String>>>

    fun getIntFlags(packageName: String, delay: Boolean): Flow<UiStates<Map<String, String>>>

    fun getFloatFlags(packageName: String, delay: Boolean): Flow<UiStates<Map<String, String>>>

    fun getStringFlags(packageName: String, delay: Boolean): Flow<UiStates<Map<String, String>>>

    fun getOverriddenBoolFlagsByPackage(packageName: String): Flow<UiStates<Map<String, String>>>

    fun getOverriddenIntFlagsByPackage(packageName: String): Flow<UiStates<Map<String, String>>>

    fun getOverriddenFloatFlagsByPackage(packageName: String): Flow<UiStates<Map<String, String>>>

    fun getOverriddenStringFlagsByPackage(packageName: String): Flow<UiStates<Map<String, String>>>

    fun getAllOverriddenBoolFlags(): Flow<UiStates<Map<String, String>>>

    fun getUsers(): Flow<MutableList<String>>

    suspend fun isPhixitSchemaUsed(): Flow<Boolean>

    suspend fun isDbFullyRecreated(): Flow<Boolean>

    suspend fun isFlagOverridesTableEmpty(): Flow<Boolean>

    suspend fun getAndroidPackage(pkgName: String): Flow<String>

    suspend fun deleteRowByFlagName(packageName: String, name: String)

    suspend fun deleteOverriddenFlagByPackage(packageName: String)

}
