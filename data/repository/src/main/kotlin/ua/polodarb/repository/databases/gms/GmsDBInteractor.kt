package ua.polodarb.repository.databases.gms

interface GmsDBInteractor {

    suspend fun overrideFlag(
        packageName: String,
        name: String,
        flagType: Int = 0,
        intVal: String? = null,
        boolVal: String? = null,
        floatVal: String? = null,
        stringVal: String? = null,
        extensionVal: String? = null,
        committed: Int = 0,
        clearData: Boolean = true,
        usersList: List<String>
    )

    suspend fun clearPhenotypeCache(pkgName: String)

}