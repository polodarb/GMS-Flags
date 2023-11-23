package ua.polodarb.gmsflags.data.remote.flags

import ua.polodarb.gmsflags.data.remote.Resource
import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlagTypes

interface FlagsApiService {
    suspend fun getSuggestedFlags(): Resource<SuggestedFlagTypes>
}


