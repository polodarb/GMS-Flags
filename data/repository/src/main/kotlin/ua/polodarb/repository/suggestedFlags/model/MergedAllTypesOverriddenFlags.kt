package ua.polodarb.repository.suggestedFlags.model

data class MergedAllTypesOverriddenFlags(
    val boolFlag: Map<String, String>,
    val intFlag: Map<String, String>,
    val floatFlag: Map<String, String>,
    val stringFlag: Map<String, String>,
)