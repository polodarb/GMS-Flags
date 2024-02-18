package ua.polodarb.gmsflags.ui.screens.suggestions

import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedPrimary
import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedSecondary

data class SuggestedFlag(
    val primary: List<PrimarySuggestedFlag>,
    val secondary: List<SecondarySuggestedFlag>
)

data class PrimarySuggestedFlag(
    val flag: SuggestedPrimary,
    val enabled: Boolean = false
)

data class SecondarySuggestedFlag(
    val flag: SuggestedSecondary,
    val enabled: Boolean = false
)
