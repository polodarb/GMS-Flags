package ua.polodarb.suggestions

import ua.polodarb.network.suggestedFlags.model.Primary
import ua.polodarb.network.suggestedFlags.model.Secondary

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
