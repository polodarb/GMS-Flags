package ua.polodarb.network.suggestedFlags.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedFlagTypes(
    @SerialName("primary") val primary: List<Primary>,
    @SerialName("secondary") val secondary: List<Secondary>
)

@Serializable
data class Primary(
    @SerialName("primaryTag") val primaryTag: String,
    @SerialName("name") val name: String,
    @SerialName("source") val source: String?,
    @SerialName("note") val note: String?,
    @SerialName("flags") val flags: List<FlagInfo>,
    @SerialName("flagPackage") val flagPackage: String,
    @SerialName("appPackage") val appPackage: String,
    @SerialName("minAppVersionCode") val minVersionCode: Int?,
    @SerialName("minAndroidSdkCode") val minAndroidSdkCode: Int?,
    @SerialName("details") val details: String?,
    @SerialName("enabled") val enabled: Boolean
)

@Serializable
data class Secondary(
    @SerialName("name") val name: String,
    @SerialName("source") val source: String,
    @SerialName("note") val note: String?,
    @SerialName("flags") val flags: List<FlagInfo>,
    @SerialName("flagPackage") val flagPackage: String,
    @SerialName("appPackage") val appPackage: String,
    @SerialName("minAppVersionCode") val minVersionCode: Int?,
    @SerialName("minAndroidSdkCode") val minAndroidSdkCode: Int?,
    @SerialName("details") val details: String?,
    @SerialName("enabled") val enabled: Boolean
)

@Serializable
data class FlagInfo(
    @SerialName("tag") val tag: String,
    @SerialName("type") val type: FlagType,
    @SerialName("value") val value: String,
)

enum class FlagType {
    @SerialName("bool") BOOL,
    @SerialName("int") INTEGER,
    @SerialName("float") FLOAT,
    @SerialName("string") STRING
}

