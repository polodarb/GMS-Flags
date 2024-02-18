package ua.polodarb.gmsflags.data.remote.flags.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedPrimary(
    @SerialName("primaryTag") val primaryTag: String,
    @SerialName("name") override val name: String,
    @SerialName("source") override val source: String?,
    @SerialName("note")  override val note: String?,
    @SerialName("flags") override val flags: List<FlagInfo>,
    @SerialName("flagPackage")  override val flagPackage: String,
    @SerialName("appPackage") override val appPackage: String,
    @SerialName("minAppVersionCode") override val minVersionCode: Int?,
    @SerialName("minAndroidSdkCode") override val minAndroidSdkCode: Int?,
    @SerialName("details") override val details: String?,
    @SerialName("enabled") override val enabled: Boolean
) : Suggested()
