package ua.polodarb.flagsfile.model

data class LoadedFlagsUI(
    val packageName: String,
    val flags: List<LoadedFlagUI>
)

data class LoadedFlagUI(
    val name: String,
    val type: String,
    val value: Any,
    var override: Boolean = true
)
