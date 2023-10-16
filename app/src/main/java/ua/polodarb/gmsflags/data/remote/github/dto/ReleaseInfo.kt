package ua.polodarb.gmsflags.data.remote.github.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReleaseInfo(
    @SerialName("tag_name") val tagName: String
)
