package ua.polodarb.domain.suggestedFlags.models

import ua.polodarb.repository.suggestedFlags.models.SuggestedFlagsRepoModel

data class SuggestedFlagsModel(
    val flag: SuggestedFlagsRepoModel,
    val enabled: Boolean = false,
)