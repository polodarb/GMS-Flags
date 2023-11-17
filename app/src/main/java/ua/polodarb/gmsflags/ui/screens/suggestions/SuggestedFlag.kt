package ua.polodarb.gmsflags.ui.screens.suggestions

import ua.polodarb.gmsflags.data.remote.flags.dto.Primary
import ua.polodarb.gmsflags.data.remote.flags.dto.Secondary

data class SuggestedFlag(
    val primary: List<PrimarySuggestedFlag>,
    val secondary: List<SecondarySuggestedFlag>
)

data class PrimarySuggestedFlag(
    val flag: Primary,
    val enabled: Boolean = false
)

data class SecondarySuggestedFlag(
    val flag: Secondary,
    val enabled: Boolean = false
)
