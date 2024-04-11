package ua.polodarb.network.suggestedFlags

import ua.polodarb.network.suggestedFlags.model.SuggestedFlagTypes
import ua.polodarb.network.Resource

interface SuggestedFlagsApiService {
    suspend fun getSuggestedFlags(): Resource<SuggestedFlagTypes>
}


