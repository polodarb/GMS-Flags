package ua.polodarb.domain.suggestedFlags.models

import ua.polodarb.repository.suggestedFlags.models.SuggestedFlagsRepoModel

data class GroupedSuggestedFlagsModel(
    val primary: List<SuggestedFlagsModel>,
    val secondary: List<SuggestedFlagsModel>
)

data class SuggestedFlagsModel(
    val flag: SuggestedFlagsRepoModel,
    val enabled: Boolean = false,
)