package ua.polodarb.gmsflags.data.remote.flags.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class FlagType {
    @SerialName("bool") BOOL,
    @SerialName("int") INTEGER,
    @SerialName("float") FLOAT,
    @SerialName("string") STRING
}

@Serializable
data class FlagInfo(
    @SerialName("tag") val tag: String,
    @SerialName("type") val type: FlagType,
    @SerialName("value") val value: String,
)

@Serializable
data class SuggestedFlagInfo(
    @SerialName("name") val name: String,
    @SerialName("author") val author: String,
    @SerialName("flags") val flags: List<FlagInfo>,
    @SerialName("package") val packageName: String,
    @SerialName("isArchive") val isArchive: Boolean
)


