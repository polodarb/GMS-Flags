package ua.polodarb.gmsflags.data.remote.flags.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FlagInfo(
    @SerialName("tag") val tag: String,
    @SerialName("type") val type: FlagType,
    @SerialName("value") val value: String,
)
