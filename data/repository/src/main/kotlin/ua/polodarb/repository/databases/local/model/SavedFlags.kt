package ua.polodarb.repository.databases.local.model

import ua.polodarb.common.FlagsTypes

data class SavedFlags(
    val pkgName: String,
    val flagName: String,
    val type: FlagsTypes,
)