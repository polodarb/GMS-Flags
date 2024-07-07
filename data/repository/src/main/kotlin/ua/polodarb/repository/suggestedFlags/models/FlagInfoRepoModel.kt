package ua.polodarb.repository.suggestedFlags.models

import ua.polodarb.network.suggestedFlags.model.FlagInfoNetModel
import ua.polodarb.repository.suggestedFlags.models.FlagTypeRepoModel.Companion.toRepoModel

data class FlagInfoRepoModel(
    val name: String,
    val type: FlagTypeRepoModel,
    val value: String,
) {
    companion object {
        fun FlagInfoNetModel.toRepoModel() =
            FlagInfoRepoModel(
                name = name,
                type = type.toRepoModel(),
                value = value
            )
    }
}