package ua.polodarb.repository.suggestedFlags.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.polodarb.network.suggestedFlags.model.FlagInfoNetModel
import ua.polodarb.repository.suggestedFlags.models.FlagTypeRepoModel.Companion.toRepoModel

@Serializable
data class FlagInfoRepoModel(
    @SerialName("name") val name: String,
    @SerialName("type") val type: FlagTypeRepoModel,
    @SerialName("value") val value: String,
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