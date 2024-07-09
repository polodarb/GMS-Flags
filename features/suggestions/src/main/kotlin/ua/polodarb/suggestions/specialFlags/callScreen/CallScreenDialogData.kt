package ua.polodarb.suggestions.specialFlags.callScreen

import ua.polodarb.repository.suggestedFlags.models.FlagInfoRepoModel

data class CallScreenDialogData(
    val flags: List<FlagInfoRepoModel>,
    val callScreenPackage: String,
)
