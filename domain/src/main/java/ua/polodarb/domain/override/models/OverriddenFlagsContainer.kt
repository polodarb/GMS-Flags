package ua.polodarb.domain.override.models

import ua.polodarb.repository.suggestedFlags.models.FlagTypeRepoModel

data class OverriddenFlagsContainer(
    val boolValues: Map<String, String>? = null,
    val intValues: Map<String, String>? = null,
    val floatValues: Map<String, String>? = null,
    val stringValues: Map<String, String>? = null,
    val extValues: Map<String, String>? = null,
) {
//    companion object {
//        fun Map<String, String>.toAbc(vararg types: FlagTypeRepoModel) =
//            OverriddenFlagsContainer
//    }
}