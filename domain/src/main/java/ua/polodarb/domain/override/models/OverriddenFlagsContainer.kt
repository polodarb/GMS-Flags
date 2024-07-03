package ua.polodarb.domain.override.models

data class OverriddenFlagsContainer(
    val boolValues: Map<String, String?>? = null,
    val intValues: Map<String, String?>? = null,
    val floatValues: Map<String, String?>? = null,
    val stringValues: Map<String, String?>? = null,
    val extValues: Map<String, String?>? = null,
)