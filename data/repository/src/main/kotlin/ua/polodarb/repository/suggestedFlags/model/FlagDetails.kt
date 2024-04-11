package ua.polodarb.repository.suggestedFlags.model

import ua.polodarb.common.FlagsTypes

data class FlagDetails(
    val pkgName: String,
    val flagName: String,
    val type: FlagsTypes
)
