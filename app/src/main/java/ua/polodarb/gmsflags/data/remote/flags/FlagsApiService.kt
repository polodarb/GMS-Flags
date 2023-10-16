package ua.polodarb.gmsflags.data.remote.flags

import ua.polodarb.gmsflags.data.remote.Resource
import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlagInfo

interface FlagsApiService {
    suspend fun getSuggestedFlags(): Resource<List<SuggestedFlagInfo>>
}


