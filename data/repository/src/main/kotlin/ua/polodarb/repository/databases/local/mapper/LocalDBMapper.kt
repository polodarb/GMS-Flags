package ua.polodarb.repository.databases.local.mapper

import ua.polodarb.common.fagsTypes.FlagsTypes
import ua.polodarb.local.entities.SavedFlagsEntity
import ua.polodarb.repository.databases.local.model.SavedFlags

fun List<SavedFlagsEntity>.toSavedFlags(): List<SavedFlags> = map {
    SavedFlags(it.pkgName, it.flagName, when (it.type) {
        "BOOLEAN" -> FlagsTypes.BOOLEAN
        "FLOAT" -> FlagsTypes.FLOAT
        "INT" -> FlagsTypes.INTEGER
        "STRING" -> FlagsTypes.STRING
        "EXTENSION" -> FlagsTypes.EXTENSION
        else -> FlagsTypes.UNKNOWN
    })
}

fun SavedFlags.toSavedFlagsEntity(): SavedFlagsEntity =
    SavedFlagsEntity(pkgName, flagName, when (type) {
        FlagsTypes.BOOLEAN -> "BOOLEAN"
        FlagsTypes.FLOAT -> "FLOAT"
        FlagsTypes.INTEGER -> "INT"
        FlagsTypes.STRING -> "STRING"
        FlagsTypes.EXTENSION -> "EXTENSION"
        else -> "UNKNOWN"
    })