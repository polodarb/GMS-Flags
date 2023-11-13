package ua.polodarb.gmsflags.ui.screens.suggestions

import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlagInfo
import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlagTypes

data class SuggestedFlag(
    val flag: SuggestedFlagInfo,
    val enabled: Boolean
)

data class NewSuggestedFlag(
    val flag: SuggestedFlagTypes,
    val enabled: Boolean
)
