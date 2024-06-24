package ua.polodarb.network.suggestedFlags.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlagInfoNetModel(
    @SerialName("name") val name: String,
    @SerialName("type") val type: FlagTypeNetModel,
    @SerialName("value") val value: String,
)