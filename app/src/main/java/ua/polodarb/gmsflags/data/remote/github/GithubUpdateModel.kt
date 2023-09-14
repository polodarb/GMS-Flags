package ua.polodarb.gmsflags.data.remote.github

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubUpdateModel(
    @SerialName("tag_name") val tagName: String
)