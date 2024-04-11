package ua.polodarb.network.appUpdates

import ua.polodarb.network.Resource
import ua.polodarb.network.appUpdates.model.ReleaseInfo

interface AppUpdatesApiService {
    suspend fun getLatestRelease(): Resource<ReleaseInfo>
}
