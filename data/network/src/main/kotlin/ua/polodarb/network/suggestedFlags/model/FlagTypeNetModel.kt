package ua.polodarb.network.suggestedFlags.model

import kotlinx.serialization.SerialName

enum class FlagTypeNetModel {
    @SerialName("bool") BOOL,
    @SerialName("int") INTEGER,
    @SerialName("float") FLOAT,
    @SerialName("string") STRING,
    @SerialName("extVal") EXTVAL
}
