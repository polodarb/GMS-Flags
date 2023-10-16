package ua.polodarb.gmsflags.ui.screens.suggestionsScreen

import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlagInfo

data class SuggestedFlag(
    val flag: SuggestedFlagInfo,
    val enabled: Boolean
)
