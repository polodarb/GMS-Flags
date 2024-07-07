package ua.polodarb.repository.suggestedFlags.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ua.polodarb.network.suggestedFlags.model.FlagTypeNetModel

@Serializable
enum class FlagTypeRepoModel {
    BOOL,
    INTEGER,
    FLOAT,
    STRING,
    EXTVAL;

   companion object {
       fun FlagTypeNetModel.toRepoModel(): FlagTypeRepoModel {
           return when (this) {
               FlagTypeNetModel.BOOL -> BOOL
               FlagTypeNetModel.INTEGER -> INTEGER
               FlagTypeNetModel.FLOAT -> FLOAT
               FlagTypeNetModel.STRING -> STRING
               FlagTypeNetModel.EXTVAL -> EXTVAL
           }
       }
   }

}
