package ua.polodarb.repository.suggestedFlags.models

import kotlinx.serialization.SerialName
import ua.polodarb.network.suggestedFlags.model.FlagTypeNetModel

enum class FlagTypeRepoModel {
    @SerialName("bool") BOOL,
    @SerialName("int") INTEGER,
    @SerialName("float") FLOAT,
    @SerialName("string") STRING,
    @SerialName("extVal") EXTVAL;

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
