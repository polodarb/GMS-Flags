package ua.polodarb.repository.suggestedFlags.model

data class MergedAllTypesFlags(
    val boolFlag: List<FlagDetails>,
    val intFlag: List<FlagDetails>,
    val floatFlag: List<FlagDetails>,
    val stringFlag: List<FlagDetails>
) {
    fun isNotEmpty() =
        boolFlag.isNotEmpty() && intFlag.isNotEmpty() && floatFlag.isNotEmpty() && stringFlag.isNotEmpty()
}