package ua.polodarb.gmsflags.data.remote.flags

import ua.polodarb.gmsflags.data.remote.Resource
import ua.polodarb.gmsflags.data.remote.flags.dto.SuggestedFlags

interface FlagsApiService {
    suspend fun getSuggestedFlags(): Resource<SuggestedFlags>
}


