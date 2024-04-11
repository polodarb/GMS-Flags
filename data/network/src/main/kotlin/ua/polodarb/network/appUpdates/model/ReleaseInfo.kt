package ua.polodarb.network.appUpdates.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseInfo(
    @SerialName("tag_name") val tagName: String
)
