package ua.polodarb.network.suggestedFlags

import ua.polodarb.network.Resource
import ua.polodarb.network.suggestedFlags.model.SuggestedFlagsNetModel

interface SuggestedFlagsApiService {
    suspend fun getSuggestedFlags(): Resource<List<SuggestedFlagsNetModel>>
}


