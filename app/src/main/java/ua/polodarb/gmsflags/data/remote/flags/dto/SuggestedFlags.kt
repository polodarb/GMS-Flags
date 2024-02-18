package ua.polodarb.gmsflags.data.remote.flags.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SuggestedFlags(
    @SerialName("primary") val primary: List<SuggestedPrimary>,
    @SerialName("secondary") val secondary: List<SuggestedSecondary>
)
